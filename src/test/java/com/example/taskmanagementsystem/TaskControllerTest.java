package com.example.taskmanagementsystem;

import com.example.taskmanagementsystem.controllers.TaskController;
import com.example.taskmanagementsystem.filter.task.TaskFilter;
import com.example.taskmanagementsystem.models.DTO.TaskDTO;
import com.example.taskmanagementsystem.models.enums.TaskStatus;
import com.example.taskmanagementsystem.responseAndAnswers.CommentRequest;
import com.example.taskmanagementsystem.responseAndAnswers.CommentResponse;
import com.example.taskmanagementsystem.responseAndAnswers.TaskResponse;
import com.example.taskmanagementsystem.responseAndAnswers.TasksResponse;
import com.example.taskmanagementsystem.services.CommentService;
import com.example.taskmanagementsystem.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;
    private TaskDTO taskDTO;
    private CommentRequest commentRequest;
    private TaskResponse taskResponse;
    private CommentResponse commentResponse;
    private TasksResponse tasksResponse;
    private Long taskId;
    private Long userId;

    @BeforeEach
    public void setUp() throws AccessDeniedException {

        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .build();

        // Initialize your objects here
        taskDTO = new TaskDTO(); // set properties
        commentRequest = new CommentRequest(); // set properties
        taskResponse = new TaskResponse(); // set properties
        commentResponse = new CommentResponse(); // set properties
        tasksResponse = new TasksResponse(); // set properties

        taskId = 1L;
        userId = 1L;

        // Set up service methods to return dummy data
        doReturn(ResponseEntity.status(HttpStatus.CREATED).body(taskResponse)).when(taskService).createTask(any(TaskDTO.class));
        doReturn(ResponseEntity.ok(taskResponse)).when(taskService).getTaskResponse(anyLong());
        doReturn(ResponseEntity.ok(taskResponse)).when(taskService).updateTask(anyLong(), any(TaskDTO.class));
        doReturn(ResponseEntity.ok(taskResponse)).when(taskService).updateTaskStatus(anyLong(), any(TaskStatus.class));
        doReturn(ResponseEntity.ok(taskResponse)).when(taskService).assignTaskToUser(anyLong(), anyLong());
        doReturn(ResponseEntity.status(HttpStatus.OK).build()).when(taskService).deleteTask(anyLong());
        doReturn(ResponseEntity.ok(taskResponse)).when(taskService).getTasksByAuthor(anyLong(), anyInt(), anyInt(), any(TaskFilter.class));
        doReturn(ResponseEntity.ok(taskResponse)).when(taskService).getTasksByAssignee(anyLong(), anyInt(), anyInt(), any(TaskFilter.class));

        doReturn(commentResponse).when(commentService).addComment(anyLong(), anyString());
        doReturn(Arrays.asList(commentResponse)).when(commentService).getTaskComments(anyLong());
    }

    @Test
    public void createTaskTest() throws Exception {
        mockMvc.perform(
                        post("/api/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(taskDTO)))
                .andExpect(status().isCreated());

        verify(taskService, times(1)).createTask(any(TaskDTO.class));
    }

    @Test
    public void getTaskTest() throws Exception {
        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isOk());

        verify(taskService, times(1)).getTaskResponse(anyLong());
    }

}
