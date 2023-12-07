package com.example.taskmanagementsystem.responseAndAnswers;

import com.example.taskmanagementsystem.models.Comment;
import com.example.taskmanagementsystem.models.DTO.CommentDTO;
import com.example.taskmanagementsystem.models.DTO.UserDTO;
import com.example.taskmanagementsystem.models.Task;

import com.example.taskmanagementsystem.models.enums.TaskPriority;
import com.example.taskmanagementsystem.models.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private UserDTO author;
    private UserDTO assignee;
    private List<CommentDTO> comments = new ArrayList<>();


    public static TaskResponse convert(Task task){
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setAuthor(new UserDTO(task.getAuthor()));
        response.setAssignee(task.getAssignee() == null? null : new UserDTO(task.getAssignee()));
        response.setComments(CommentDTO.getList(task.getComments()));
        return response;
    }
}
