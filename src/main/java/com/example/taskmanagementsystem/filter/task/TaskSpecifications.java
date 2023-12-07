package com.example.taskmanagementsystem.filter.task;

import com.example.taskmanagementsystem.models.Task;
import com.example.taskmanagementsystem.filter.task.TaskFilter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * A set of specifications for filtering tasks.
 */
public interface TaskSpecifications {
    /**
     * Generates a Specification object for filtering tasks based on the given TaskFilter.
     * The Specification object can be used to build a query in JPA.
     *
     * @param filter the TaskFilter object containing the filter criteria for the tasks.
     * @return a Specification object for filtering tasks.
     */
    static Specification<Task> withFilter(TaskFilter filter) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            predicate = addEqualPredicate(predicate, root, criteriaBuilder, "author", filter.getAuthorId());
            predicate = addEqualPredicate(predicate, root, criteriaBuilder, "assignee", filter.getAssigneeId());
            predicate = addLikePredicate(predicate, root, criteriaBuilder, "title", filter.getKeyword());
            predicate = addEqualPredicate(predicate, root, criteriaBuilder, "status", filter.getStatus());
            predicate = addEqualPredicate(predicate, root, criteriaBuilder, "priority", filter.getPriority());

            return predicate;
        };
    }

    /**
     * Adds a like predicate to the given predicate.
     *
     * @param predicate         The current predicate to add the like predicate to.
     * @param root              The root entity.
     * @param criteriaBuilder   The criteria builder.
     * @param field             The field to apply the like predicate on.
     * @param value             The value to match against.
     * @return The updated predicate with the added like predicate.
     */
    private static Predicate addLikePredicate(Predicate predicate, Root<Task> root, CriteriaBuilder criteriaBuilder,
                                              String field, String value) {
        if (value != null && !value.isEmpty()) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(field)),
                            "%" + value.toLowerCase() + "%"
                    )
            );
        }
        return predicate;
    }

    /**
     * Adds an equal predicate to the given predicate.
     *
     * @param predicate       the existing predicate
     * @param root            the root object of the query
     * @param criteriaBuilder the criteria builder
     * @param field           the field of the object to compare
     * @param value           the value to compare with
     * @return the updated predicate with the added equal predicate
     */
    private static Predicate addEqualPredicate(Predicate predicate, Root<Task> root, CriteriaBuilder criteriaBuilder,
                                               String field, Object value) {
        if (value != null) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.equal(root.get(field), value)
            );
        }
        return predicate;
    }
}

