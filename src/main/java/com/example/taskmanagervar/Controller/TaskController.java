package com.example.taskmanagervar.Controller;

import com.example.taskmanagervar.Service.CategoryService;
import com.example.taskmanagervar.Service.SubtaskService;
import com.example.taskmanagervar.Service.TaskService;
import com.example.taskmanagervar.entity.Priority;
import com.example.taskmanagervar.entity.Status;
import com.example.taskmanagervar.model.Category;
import com.example.taskmanagervar.model.Subtask;
import com.example.taskmanagervar.model.Task;
import com.example.taskmanagervar.repository.CategoryRepository;
import com.example.taskmanagervar.repository.TaskRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final CategoryService categoryService;
    private final SubtaskService subtaskService;

    public TaskController(TaskService taskService , CategoryService categoryService , SubtaskService subtaskService) {
        this.taskService = taskService;
        this.categoryService = categoryService;
        this.subtaskService = subtaskService;
    }


    @PostMapping("/save")
    public String saveTask(@ModelAttribute Task task , HttpServletRequest request) {
        if (task.getId() == null) {
            taskService.saveTask(task);
        } else {
            taskService.updateTask(task);
        }
        String[] subtasks = request.getParameterValues("subtask");
        if (subtasks != null ) {
            for (String subtask : subtasks) {
                if (subtask != null && !subtask.isBlank()) {
                    subtaskService.addSubtask(task.getId(), subtask);
                }
            }
        }
        return "redirect:/";
    }


    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return "redirect:/";
    }


    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("statuses", Status.values());
        return "task-edit";
    }


    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute Task task) {
        task.setId(id);
        taskService.updateTask(task);
        return "redirect:/";
    }
    @GetMapping("/view/{id}")
    public String viewTask(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id);
        List<Subtask> subtasks = subtaskService.getSubtasksByTaskId(id);
        double progress = subtaskService.getProgress(id);
        model.addAttribute("task", task);
        model.addAttribute("subtasks", subtasks);
        model.addAttribute("progress", progress);
        return "task-view";
    }
}

