package com.example.taskmanagervar.Controller;

import com.example.taskmanagervar.Service.CategoryService;
import com.example.taskmanagervar.Service.TaskService;
import com.example.taskmanagervar.model.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/archive")
public class ArchiveController {
    public final TaskService taskService;
    public final CategoryService categoryService;
    public ArchiveController(TaskService taskService , CategoryService categoryService) {
        this.taskService = taskService;
        this.categoryService = categoryService;
    }
    @GetMapping
    public String showArchiveTasks(Model model) {
        List<Task> archivedTasks = taskService.getArchivedTasks();
        model.addAttribute("tasks", archivedTasks);
        return "archive";
    }
    @PostMapping("/unarchive/{id}")
    public String unArchiveTasks(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        task.setArchived(false);
        taskService.updateTask(task);
        return "redirect:/archive";
    }
}
