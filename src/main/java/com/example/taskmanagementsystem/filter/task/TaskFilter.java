package com.example.taskmanagementsystem.filter.task;

import com.example.taskmanagementsystem.models.enums.TaskPriority;
import com.example.taskmanagementsystem.models.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskFilter {
    private Long authorId;
    private Long assigneeId;
    private String keyword;
    private TaskStatus status;
    private TaskPriority priority;

    public boolean hasFilter(){
        return keyword != null || status != null || priority != null;
    }
}

