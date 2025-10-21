package com.example.taskmanagervar.Service;

import com.example.taskmanagervar.model.Comment;
import com.example.taskmanagervar.model.Task;
import com.example.taskmanagervar.repository.CommentRepository;
import com.example.taskmanagervar.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private TaskRepository taskRepository;
    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
    }

    public List<Comment> getCommentsByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        return commentRepository.findByTask(task);
    }

    public Comment addComment(Long taskId , String text) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        Comment comment = new Comment();
        comment.setTask(task);
        comment.setText(text);
        commentRepository.save(comment);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
