package com.example.taskmanagementsystem.repository;

import com.example.taskmanagementsystem.models.Task;
import com.example.taskmanagementsystem.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    Page<Task> findByAssignee(User assignee, Pageable pageable, Specification<Task> specification);
    Page<Task> findByAssignee(User assignee, Pageable pageable);

    @Override
    Page<Task> findAll(Specification<Task> specification, Pageable pageable);
}

