package com.example.taskmanagervar.Controller;

import com.example.taskmanagervar.Service.CommentService;
import com.example.taskmanagervar.Service.TaskService;
import com.example.taskmanagervar.model.Comment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final TaskService taskService;
    public CommentController(CommentService commentService, TaskService taskService) {
        this.commentService = commentService;
        this.taskService = taskService;
    }
    @PostMapping("/add/{taskId}")
    public String addComment(@PathVariable Long taskId, @RequestParam String text) {
        commentService.addComment(taskId, text);
        return "redirect:/tasks/view/" + taskId;
    }
    @PostMapping("/delete/{id}/{taskId}")
    public String deleteComment(@PathVariable Long id , @PathVariable Long taskId) {
        commentService.deleteComment(id);
        return "redirect:/tasks/view/" + taskId;
    }
}
