package com.example.taskmanagementsystem.controllers;

import com.example.taskmanagementsystem.models.DTO.TaskDTO;
import com.example.taskmanagementsystem.models.enums.TaskPriority;
import com.example.taskmanagementsystem.models.enums.TaskStatus;
import com.example.taskmanagementsystem.filter.task.TaskFilter;
import com.example.taskmanagementsystem.responseAndAnswers.CommentRequest;
import com.example.taskmanagementsystem.responseAndAnswers.CommentResponse;
import com.example.taskmanagementsystem.responseAndAnswers.TaskResponse;
import com.example.taskmanagementsystem.responseAndAnswers.TasksResponse;
import com.example.taskmanagementsystem.services.CommentService;
import com.example.taskmanagementsystem.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Controller class that handles HTTP requests related to tasks.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private CommentService commentService;

    /**
     * Creates a new task.
     *
     * @param taskDTO the task data transfer object containing the task details
     * @return a ResponseEntity containing the task response
     */
    @PostMapping
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskDTO taskDTO) {
        return taskService.createTask(taskDTO);
    }

    /**
     * Get a task by its ID.
     *
     * @param taskId The ID of the task to retrieve.
     * @return ResponseEntity<TaskResponse> The response entity containing the task response.
     */
    @GetMapping("/{taskId}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long taskId) {
        return taskService.getTaskResponse(taskId);
    }

    /**
     * Updates a task with the given task ID.
     *
     * @param taskId   The ID of the task to be updated.
     * @param taskDTO  The updated task details.
     * @return A response entity containing the updated task response.
     * @throws AccessDeniedException If the user does not have the necessary authority to update the task.
     */
    @PutMapping("/{taskId}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long taskId, @RequestBody TaskDTO taskDTO) throws AccessDeniedException {
        return taskService.updateTask(taskId, taskDTO);
    }

    /**
     * Updates the status of a task.
     *
     * @param taskId The ID of the task to update.
     * @param status The new status of the task.
     * @return A ResponseEntity containing the updated task response if successful.
     * @throws AccessDeniedException If the user does not have the required authority to update the task status.
     */
    @PutMapping("/{taskId}/status")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam(required = true) TaskStatus status) throws AccessDeniedException {
        return taskService.updateTaskStatus(taskId, status);
    }

    /**
     * Assigns a task to a user.
     *
     * @param taskId     The ID of the task to be assigned.
     * @param assigneeId The ID of the user to whom the task will be assigned.
     * @return The response entity containing the updated task information.
     * @throws AccessDeniedException If the user does not have the required authority to perform the operation.
     */
    @PutMapping("/{taskId}/assignee")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<TaskResponse> assignTaskToUser(
            @PathVariable Long taskId,
            @RequestParam(required = true) Long assigneeId) throws AccessDeniedException {
        return taskService.assignTaskToUser(taskId, assigneeId);
    }

    /**
     * Deletes a task with the given taskId.
     *
     * @param taskId the ID of the task to be deleted
     * @return a ResponseEntity object representing the status of the deletion operation
     * @throws AccessDeniedException if the user does not have the necessary authority to delete tasks
     */
    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) throws AccessDeniedException {
        return taskService.deleteTask(taskId);
    }
    /**
     * Retrieves tasks created by a specific author.
     *
     * @param authorId    the ID of the author whose tasks to retrieve
     * @param offset      the starting index of the tasks to retrieve (default value is 0 if not provided)
     * @param limit       the maximum number of tasks to retrieve (default value is 10 if not provided)
     * @param keyword     the keyword to filter tasks by (optional)
     * @param status      the status to filter tasks by (optional)
     * @param priority    the priority to filter tasks by (optional)
     * @return a ResponseEntity containing the TasksResponse that matches the specified criteria
     */
    @GetMapping("/byAuthor/{authorId}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<TasksResponse> getTasksByAuthor(@PathVariable Long authorId,
                                                          @RequestParam(required = false, defaultValue = "0") int offset,
                                                          @RequestParam(required = false, defaultValue = "10") int limit,
                                                          @RequestParam(required = false) String keyword,
                                                          @RequestParam(required = false) TaskStatus status,
                                                          @RequestParam(required = false) TaskPriority priority) {
        return taskService.getTasksByAuthor(authorId, offset, limit, new TaskFilter(authorId, null,keyword, status, priority));
    }

    /**
     * Retrieves tasks assigned to a specific assignee.
     *
     * @param assigneeId The ID of the assignee.
     * @param offset The offset of the tasks to retrieve. (Optional, default value is 0)
     * @param limit The maximum number of tasks to retrieve. (Optional, default value is 10)
     * @param keyword The keyword to filter tasks by. (Optional)
     * @param status The status of the tasks to retrieve. (Optional)
     * @param priority The priority of the tasks to retrieve. (Optional)
     * @return A ResponseEntity containing a TasksResponse object that contains the retrieved tasks.
     */
    @GetMapping("/byAssignee/{assigneeId}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<TasksResponse> getTasksByAssignee(@PathVariable Long assigneeId,
                                                            @RequestParam(required = false, defaultValue = "0") int offset,
                                                            @RequestParam(required = false, defaultValue = "10") int limit,
                                                            @RequestParam(required = false) String keyword,
                                                            @RequestParam(required = false) TaskStatus status,
                                                            @RequestParam(required = false) TaskPriority priority) {
        return taskService.getTasksByAssignee(assigneeId, offset, limit, new TaskFilter(null, assigneeId, keyword, status, priority));
    }

    /**
     * Adds a comment to a task.
     *
     * @param taskId         The ID of the task to add the comment to.
     * @param commentRequest The comment request containing the text of the comment.
     * @return The response entity containing the added comment.
     */
    @PostMapping("/{taskId}/comments")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<CommentResponse> addCommentToTask(
            @PathVariable Long taskId,
            @RequestBody CommentRequest commentRequest) {
        CommentResponse comment = commentService.addComment(taskId, commentRequest.getText());
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    /**
     * Retrieves the comments for a task.
     *
     * @param taskId the ID of the task for which to retrieve the comments
     * @return a ResponseEntity containing a list of CommentResponse objects representing the comments for the task
     */
    @GetMapping("/{taskId}/comments")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<List<CommentResponse>> getTaskComments(@PathVariable Long taskId) {
        List<CommentResponse> comments = commentService.getTaskComments(taskId);
        return ResponseEntity.ok(comments);
    }
}


