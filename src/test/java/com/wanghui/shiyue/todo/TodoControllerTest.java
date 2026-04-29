package com.wanghui.shiyue.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanghui.shiyue.comm.entity.ResponseResult;
import com.wanghui.shiyue.todo.controller.TodoController;
import com.wanghui.shiyue.todo.entity.dto.SubTaskDTO;
import com.wanghui.shiyue.todo.entity.dto.TodoDTO;
import com.wanghui.shiyue.todo.entity.dto.TodoQueryParam;
import com.wanghui.shiyue.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TodoController.class)
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TodoService todoService;

    @Test
    void testQuery() throws Exception {
        TodoDTO dto = new TodoDTO();
        dto.setTodoId(1L);
        dto.setTitle("t");
        dto.setDueTime(new Date());
        dto.setPriority(3);
        dto.setCompleted(false);
        Mockito.when(todoService.query(Mockito.any())).thenReturn(Collections.singletonList(dto));
        TodoQueryParam param = new TodoQueryParam();
        param.setOrderByPriority(true);
        mockMvc.perform(post("/todo/query")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].todoId").value(1L));
    }

    @Test
    void testAdd() throws Exception {
        TodoDTO input = new TodoDTO();
        input.setTitle("t");
        input.setDueTime(new Date());
        input.setPriority(2);
        TodoDTO saved = new TodoDTO();
        saved.setTodoId(100L);
        saved.setTitle("t");
        saved.setDueTime(new Date());
        saved.setPriority(2);
        Mockito.when(todoService.save(Mockito.any())).thenReturn(saved);
        mockMvc.perform(post("/todo/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.todoId").value(100L));
    }

    @Test
    void testSubAdd() throws Exception {
        SubTaskDTO sub = new SubTaskDTO();
        sub.setTodoId(1L);
        sub.setName("n");
        SubTaskDTO out = new SubTaskDTO();
        out.setSubTaskId(10L);
        out.setTodoId(1L);
        out.setName("n");
        Mockito.when(todoService.addSubTask(Mockito.any())).thenReturn(out);
        mockMvc.perform(post("/todo/sub/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sub)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.subTaskId").value(10L));
    }
}
