package com.example.taskmanagementsystem.requestsAndResponses;

import com.example.taskmanagementsystem.models.Comment;
import com.example.taskmanagementsystem.models.DTO.TaskDTO;
import com.example.taskmanagementsystem.models.DTO.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Long id;

    private String text;
    private UserDTO user;
    private TaskDTO task;

    public CommentResponse(Comment comment){
        setId(comment.getId());
        setText(comment.getText());
        setUser(new UserDTO(comment.getUser()));
        setTask(new TaskDTO(comment.getTask()));
    }

    public static List<CommentResponse> getList(List<Comment> comments){
        List<CommentResponse> result = new ArrayList<>();
        comments.forEach(c -> result.add(new CommentResponse(c)));
        return result;
    }
}
