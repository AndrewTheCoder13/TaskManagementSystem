package com.example.taskmanagementsystem.models.DTO;

import com.example.taskmanagementsystem.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String text;
    private Long userId;
    private Long taskId;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.userId = comment.getUser().getId();
        this.taskId = comment.getTask().getId();
    }

    public static List<CommentDTO> getList(List<Comment> comments){
        List<CommentDTO> result = new ArrayList<>();
        comments.forEach(c -> result.add(new CommentDTO(c)));
        return result;
    }
}

