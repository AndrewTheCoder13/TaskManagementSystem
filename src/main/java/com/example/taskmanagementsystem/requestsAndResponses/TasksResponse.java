package com.example.taskmanagementsystem.requestsAndResponses;

import com.example.taskmanagementsystem.models.DTO.TaskDTO;
import com.example.taskmanagementsystem.models.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TasksResponse {

    private long count;
    private List<TaskDTO> posts;

    public static TasksResponse getEmptyResponse() {
        TasksResponse response = new TasksResponse();
        response.setCount(0);
        return response;
    }

    public TasksResponse(Page<Task> receivedPosts){
        posts = new ArrayList<>();
        count = receivedPosts.getTotalElements();
        receivedPosts.forEach(post -> {
            TaskDTO postResponse = new TaskDTO(post);
            posts.add(postResponse);
        });
    }
}
