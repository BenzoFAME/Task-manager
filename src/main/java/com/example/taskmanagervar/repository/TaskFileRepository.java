package com.example.taskmanagervar.repository;

import com.example.taskmanagervar.model.TaskFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskFileRepository extends JpaRepository<TaskFile, Long> {
    List<TaskFile> findByTaskId(Long taskId);
}
