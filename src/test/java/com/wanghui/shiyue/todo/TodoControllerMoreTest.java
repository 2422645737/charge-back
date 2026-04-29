package com.wanghui.shiyue.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TodoController.class)
public class TodoControllerMoreTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TodoService todoService;

    @Test
    void testUpdateAndDelete() throws Exception {
        TodoDTO dto = new TodoDTO();
        dto.setTodoId(2L);
        dto.setTitle("t2");
        dto.setDueTime(new Date());
        dto.setPriority(1);
        Mockito.when(todoService.update(Mockito.any())).thenReturn(dto);
        Mockito.when(todoService.delete(2L)).thenReturn(true);
        mockMvc.perform(post("/todo/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.todoId").value(2L));
        mockMvc.perform(post("/todo/delete")
                        .param("todoId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void testBatchAndSubOps() throws Exception {
        Mockito.when(todoService.deleteBatch(Mockito.anyList())).thenReturn(true);
        Mockito.when(todoService.listSubTasks(1L)).thenReturn(Collections.emptyList());
        SubTaskDTO sub = new SubTaskDTO();
        sub.setSubTaskId(11L);
        sub.setTodoId(1L);
        sub.setName("n1");
        Mockito.when(todoService.addSubTask(Mockito.any())).thenReturn(sub);
        Mockito.when(todoService.updateSubTask(Mockito.any())).thenReturn(sub);
        Mockito.when(todoService.deleteSubTask(11L)).thenReturn(true);
        mockMvc.perform(post("/todo/deleteBatch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Arrays.asList(1L, 2L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
        mockMvc.perform(post("/todo/sub/list")
                        .param("todoId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
        mockMvc.perform(post("/todo/sub/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sub)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.subTaskId").value(11L));
        mockMvc.perform(post("/todo/sub/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sub)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
        mockMvc.perform(post("/todo/sub/delete")
                        .param("subTaskId", "11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }
}
