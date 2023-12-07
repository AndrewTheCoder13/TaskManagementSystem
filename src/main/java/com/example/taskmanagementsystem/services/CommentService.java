package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.models.Comment;
import com.example.taskmanagementsystem.models.DTO.CommentDTO;
import com.example.taskmanagementsystem.models.Task;
import com.example.taskmanagementsystem.models.User;
import com.example.taskmanagementsystem.repository.CommentRepository;
import com.example.taskmanagementsystem.repository.TaskRepository;
import com.example.taskmanagementsystem.repository.UserRepository;
import com.example.taskmanagementsystem.responseAndAnswers.CommentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * A service class for managing comments related operations.
 */
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Adds a comment to a task.
     *
     * @param taskId The ID of the task to add the comment to.
     * @param text   The text of the comment to add.
     * @return A CommentResponse object containing the newly added comment.
     * @throws EntityNotFoundException If the task with the given ID is not found in the task repository.
     */
    public CommentResponse addComment(Long taskId, String text) {
        User currentUser = getCurrentUser();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

        Comment comment = new Comment();
        comment.setText(text);
        comment.setUser(currentUser);
        comment.setTask(task);
        commentRepository.save(comment);

        return new CommentResponse(comment);
    }

    /**
     * Retrieves the comments associated with a given task.
     *
     * @param taskId The ID of the task to retrieve the comments for.
     * @return A list of CommentResponse objects representing the comments associated with the task.
     * @throws EntityNotFoundException if the task cannot be found in the database.
     */
    public List<CommentResponse> getTaskComments(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
        List<CommentResponse> responses = CommentResponse.getList(task.getComments());
        return responses;
    }

    /**
     * Retrieves the currently authenticated user from the authentication context.
     *
     * @return the currently authenticated user
     * @throws EntityNotFoundException if the user is not found in the database
     * @throws IllegalStateException if unable to get the current user from the authentication context
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userDetails.getUsername()));
        }

        throw new IllegalStateException("Unable to get current user from authentication context");
    }
}

