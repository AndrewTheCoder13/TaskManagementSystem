package com.example.taskmanagementsystem.repository;

import com.example.taskmanagementsystem.models.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
}
