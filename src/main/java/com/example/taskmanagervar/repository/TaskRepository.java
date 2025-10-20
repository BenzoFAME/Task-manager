package com.example.taskmanagervar.repository;

import com.example.taskmanagervar.model.Category;
import com.example.taskmanagervar.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCategory(Category category);
}
