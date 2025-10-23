package com.example.taskmanagervar.repository;

import com.example.taskmanagervar.model.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    List<Subtask> findByTaskId(Long taskId);
}
