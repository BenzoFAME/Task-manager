package com.example.taskmanagervar.repository;

import com.example.taskmanagervar.model.Category;
import com.example.taskmanagervar.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCategory(Category category);
    List<Task> findAllByArchivedFalse();
    List<Task> findAllByArchivedTrue();
    @Query("SELECT t FROM Task t WHERE " +
            "LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Task> searchTasks(@Param("keyword") String keyword);
}
