package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.filter.task.TaskSpecifications;
import com.example.taskmanagementsystem.models.DTO.TaskDTO;
import com.example.taskmanagementsystem.models.Task;
import com.example.taskmanagementsystem.models.User;
import com.example.taskmanagementsystem.models.enums.TaskStatus;
import com.example.taskmanagementsystem.filter.task.TaskFilter;
import com.example.taskmanagementsystem.repository.TaskRepository;
import com.example.taskmanagementsystem.repository.UserRepository;
import com.example.taskmanagementsystem.responseAndAnswers.TaskResponse;
import com.example.taskmanagementsystem.responseAndAnswers.TasksResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;

/**
 * This class provides the services for managing tasks.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new task based on the provided taskDTO.
     *
     * @param taskDTO The DTO object containing the task details.
     * @return A ResponseEntity containing the newly created task response.
     */
    public ResponseEntity<TaskResponse> createTask(TaskDTO taskDTO) {
        User author = getCurrentUser();
        Task task = convertToEntity(taskDTO, author);
        taskRepository.save(task);
        TaskResponse response = TaskResponse.convert(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves tasks by author.
     *
     * @param authorId The ID of the author.
     * @param offset   The offset for pagination.
     * @param limit    The limit for pagination.
     * @param filter   The filter to apply on tasks.
     * @return A ResponseEntity containing a TasksResponse object with tasks matching the specified criteria.
     */
    public ResponseEntity<TasksResponse> getTasksByAuthor(Long authorId, int offset, int limit, TaskFilter filter) {
        User author = getUserById(authorId, "Author");
        Pageable pageable = createPageable(offset, limit);
        Specification<Task> specification = TaskSpecifications.withFilter(filter);
        Page<Task> tasks = taskRepository.findAll(specification, pageable);
        TasksResponse response = tasks == null ? TasksResponse.getEmptyResponse() : new TasksResponse(tasks);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves tasks assigned to a specific assignee.
     *
     * @param assigneeId The ID of the assignee.
     * @param offset     The offset for paginated results.
     * @param limit      The maximum number of tasks to retrieve.
     * @param filter     The filter to apply when retrieving tasks.
     * @return A ResponseEntity containing a TasksResponse object with the retrieved tasks.
     */
    public ResponseEntity<TasksResponse> getTasksByAssignee(Long assigneeId, int offset, int limit, TaskFilter filter) {
        User assignee = getUserById(assigneeId, "Assignee");
        Pageable pageable = createPageable(offset, limit);
        Specification<Task> specification = TaskSpecifications.withFilter(filter);
        Page<Task> tasks = taskRepository.findAll(specification, pageable);
        TasksResponse response = tasks == null ? TasksResponse.getEmptyResponse() : new TasksResponse(tasks);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates the status of a task
     *
     * @param taskId the ID of the task
     * @param status the new status of the task
     * @return ResponseEntity containing the updated task response
     * @throws AccessDeniedException if the user does not have privilege to update the task
     */
    public ResponseEntity<TaskResponse> updateTaskStatus(Long taskId, TaskStatus status) throws AccessDeniedException {
        Task task = getTaskById(taskId);
        validateUserPrivilegeToUpdate(task);
        task.setStatus(status);
        taskRepository.save(task);
        TaskResponse response = TaskResponse.convert(task);
        return ResponseEntity.ok(response);
    }

    /**
     * Assigns a task to a user.
     *
     * @param taskId     the ID of the task to assign
     * @param assigneeId the ID of the user to assign the task to
     * @return a ResponseEntity containing a TaskResponse object representing the updated task
     * @throws AccessDeniedException if the user does not have the privilege to update the task
     */
    public ResponseEntity<TaskResponse> assignTaskToUser(Long taskId, Long assigneeId) throws AccessDeniedException {
        Task task = getTaskById(taskId);
        validateUserPrivilegeToUpdate(task);
        User assignee = getUserById(assigneeId, "Assignee");
        task.setAssignee(assignee);
        taskRepository.save(task);
        TaskResponse response = TaskResponse.convert(task);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the task response for the given task ID.
     *
     * @param taskId the ID of the task to retrieve the response for
     * @return the ResponseEntity containing the task response
     */
    public ResponseEntity<TaskResponse> getTaskResponse(Long taskId) {
        Task task = getTaskById(taskId);
        TaskResponse response = TaskResponse.convert(task);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing task with the given task id and task data.
     *
     * @param taskId  The id of the task to be updated.
     * @param taskDTO The task data to update the existing task with.
     * @return The ResponseEntity with the updated TaskResponse.
     * @throws AccessDeniedException If the current user does not have the privilege to update the task.
     */
    public ResponseEntity<TaskResponse> updateTask(Long taskId, TaskDTO taskDTO) throws AccessDeniedException {
        Task existingTask = getTaskById(taskId);
        validateUserPrivilegeToUpdate(existingTask);
        User author = getCurrentUser();

        updateTaskFromDto(existingTask, taskDTO, author);

        TaskResponse response = TaskResponse.convert(existingTask);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates the task with the provided taskDTO object, using the given task and author.
     *
     * @param task    the task to be updated
     * @param taskDTO the taskDTO object containing the updated task details
     * @param author  the user who is updating the task
     */
    private void updateTaskFromDto(Task task, TaskDTO taskDTO, User author) {
        Task updatedTask = convertToEntity(taskDTO, author);

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setStatus(updatedTask.getStatus());
        task.setPriority(updatedTask.getPriority());
        task.setAssignee(updatedTask.getAssignee());
    }

    /**
     * Deletes a task based on the provided taskId.
     *
     * @param taskId the unique identifier of the task to be deleted
     * @return a ResponseEntity with a status of 200 (OK) if the task was successfully deleted
     * @throws AccessDeniedException if the user does not have the privilege to delete the task
     */
    public ResponseEntity<Void> deleteTask(Long taskId) throws AccessDeniedException {
        Task existingTask = getTaskById(taskId);
        validateUserPrivilegeToUpdate(existingTask);
        taskRepository.delete(existingTask);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Converts a TaskDTO object to a Task entity object.
     *
     * @param taskDTO The TaskDTO object to be converted.
     * @param author  The User object who is the author of the task.
     * @return The converted Task entity object.
     */
    private Task convertToEntity(TaskDTO taskDTO, User author) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setPriority(taskDTO.getPriority());
        task.setStatus(taskDTO.getStatus() != null ? taskDTO.getStatus() : TaskStatus.TODO);

        task.setAuthor(author);

        if (taskDTO.getAssigneeId() != null && taskDTO.getAssigneeId() != 0) {
            User assignee = getUserById(taskDTO.getAssigneeId(), "Assignee");
            task.setAssignee(assignee);
        }
        return task;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId   The ID of the user.
     * @param userType The type of user (e.g., "Assignee").
     * @return The user corresponding to the given ID.
     * @throws EntityNotFoundException if no user is found with the given ID.
     */
    private User getUserById(Long userId, String userType) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userType + " not found with id: " + userId));
    }

    /**
     * Retrieves a {@link Task} object with the given ID from the task repository.
     *
     * @param taskId the ID of the task to retrieve
     * @return the task with the given ID
     * @throws EntityNotFoundException if no task is found with the given ID
     */
    private Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
    }

    /**
     * Validates the user privilege to update a task.
     *
     * @param task the task to be updated
     * @throws AccessDeniedException if the user does not have the privilege to update the task
     */
    private void validateUserPrivilegeToUpdate(Task task) throws AccessDeniedException {
        User user = getCurrentUser();
        if (!user.getId().equals(task.getAuthor().getId())) {
            throw new AccessDeniedException("Only the author can update this task");
        }
    }

    /**
     * Creates a Pageable object with the specified offset and limit.
     *
     * @param offset the start index of the page (zero-based)
     * @param limit  the maximum number of elements per page
     * @return a Pageable object representing the specifications for pagination
     */
    private Pageable createPageable(int offset, int limit) {
        return PageRequest.of(offset / limit, limit);
    }

    /**
     * Returns the currently authenticated user.
     *
     * @return the currently authenticated user.
     * @throws EntityNotFoundException if the user is not found in the repository.
     * @throws IllegalStateException   if there is no authenticated user in the authentication context.
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userDetails.getUsername()));
        }

        throw new IllegalStateException("Unable to get the current user from the authentication context");
    }
}