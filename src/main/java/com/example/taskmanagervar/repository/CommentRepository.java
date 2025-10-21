package com.example.taskmanagervar.repository;

import com.example.taskmanagervar.model.Comment;
import com.example.taskmanagervar.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTask(Task task);
}
