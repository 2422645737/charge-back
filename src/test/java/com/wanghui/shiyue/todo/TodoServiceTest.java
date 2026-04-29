package com.wanghui.shiyue.todo;

import com.wanghui.shiyue.todo.convert.SubTaskConvert;
import com.wanghui.shiyue.todo.convert.TodoConvert;
import com.wanghui.shiyue.todo.dao.SubTaskMapper;
import com.wanghui.shiyue.todo.dao.TodoLogMapper;
import com.wanghui.shiyue.todo.dao.TodoMapper;
import com.wanghui.shiyue.todo.entity.dto.SubTaskDTO;
import com.wanghui.shiyue.todo.entity.dto.TodoDTO;
import com.wanghui.shiyue.todo.entity.po.SubTaskPO;
import com.wanghui.shiyue.todo.entity.po.TodoLogPO;
import com.wanghui.shiyue.todo.entity.po.TodoPO;
import com.wanghui.shiyue.todo.service.impl.TodoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
    @InjectMocks
    private TodoServiceImpl todoService;
    @Mock
    private TodoMapper todoMapper;
    @Mock
    private SubTaskMapper subTaskMapper;
    @Mock
    private TodoLogMapper todoLogMapper;
    @Mock
    private TodoConvert todoConvert;
    @Mock
    private SubTaskConvert subTaskConvert;

    @Test
    void testValidationTooLongRemark() {
        TodoDTO dto = new TodoDTO();
        dto.setTitle("t");
        dto.setRemark("a".repeat(501));
        dto.setDueTime(new Date());
        dto.setPriority(1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> todoService.save(dto));
    }

    @Test
    void testListSubTasksNested() {
        List<SubTaskPO> list = new ArrayList<>();
        SubTaskPO a = new SubTaskPO();
        a.setSubTaskId(1L); a.setTodoId(10L); a.setName("a");
        SubTaskPO b = new SubTaskPO();
        b.setSubTaskId(2L); b.setTodoId(10L); b.setParentId(1L); b.setName("b");
        list.add(a); list.add(b);
        Mockito.when(subTaskMapper.listByTodoId(10L)).thenReturn(list);
        SubTaskDTO ad = new SubTaskDTO(); ad.setSubTaskId(1L); ad.setTodoId(10L); ad.setName("a");
        SubTaskDTO bd = new SubTaskDTO(); bd.setSubTaskId(2L); bd.setTodoId(10L); bd.setParentId(1L); bd.setName("b");
        Mockito.when(subTaskConvert.posToDto(list)).thenReturn(Arrays.asList(ad, bd));
        List<SubTaskDTO> roots = todoService.listSubTasks(10L);
        Assertions.assertEquals(1, roots.size());
        Assertions.assertEquals(1, roots.get(0).getChildren().size());
    }

    @Test
    void testConcurrentUpdate() throws InterruptedException {
        TodoDTO dto = new TodoDTO();
        dto.setTodoId(99L);
        dto.setTitle("t");
        dto.setDueTime(new Date());
        dto.setPriority(2);
        Mockito.when(todoConvert.dtoToPo(Mockito.any())).thenAnswer(i -> {
            TodoPO po = new TodoPO();
            po.setTodoId(dto.getTodoId());
            po.setTitle(dto.getTitle());
            po.setPriority(dto.getPriority());
            po.setDueTime(dto.getDueTime());
            return po;
        });
        Mockito.when(todoMapper.updateById(Mockito.any(TodoPO.class))).thenReturn(1);
        Mockito.when(todoMapper.selectById(99L)).thenReturn(new TodoPO());
        Mockito.when(todoConvert.poToDto(Mockito.any())).thenReturn(new TodoDTO());
        ExecutorService pool = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(4);
        for (int i = 0; i < 4; i++) {
            pool.submit(() -> {
                try {
                    todoService.update(dto);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await(5, TimeUnit.SECONDS);
        pool.shutdown();
        Mockito.verify(todoMapper, Mockito.atLeast(4)).updateById(Mockito.any(TodoPO.class));
    }
}
