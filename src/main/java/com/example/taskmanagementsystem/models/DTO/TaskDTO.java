package com.example.taskmanagementsystem.models.DTO;

import com.example.taskmanagementsystem.models.Task;
import com.example.taskmanagementsystem.models.enums.TaskPriority;
import com.example.taskmanagementsystem.models.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long assigneeId;
    private Long authorId;

    public TaskDTO(Task task){
        setTitle(task.getTitle());
        setDescription(task.getDescription());
        setStatus(task.getStatus());
        setPriority(task.getPriority());
        setAssigneeId(task.getAssignee() == null? null : task.getAssignee().getId());
        setAuthorId(task.getAuthor().getId());
    }
}
