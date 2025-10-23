package com.example.taskmanagervar.Controller;

import com.example.taskmanagervar.Service.SubtaskService;
import com.example.taskmanagervar.Service.TaskService;
import com.example.taskmanagervar.model.Subtask;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/subtasks")
public class SubtaskController {
    private final SubtaskService subtaskService;
    private final TaskService taskService;

    public SubtaskController(SubtaskService subtaskService, TaskService taskService) {
        this.subtaskService = subtaskService;
        this.taskService = taskService;
    }

    @PostMapping("/add/{taskId}")
    public String addSubtask(@PathVariable Long taskId, @RequestParam String name) {
        subtaskService.addSubtask(taskId, name);
        return "redirect:/tasks/view/" + taskId;
    }

    @PostMapping("/delete/{id}/{taskId}")
    public String deleteSubtask(@PathVariable Long id, @PathVariable Long taskId) {
        subtaskService.deleteSubtask(id);
        return "redirect:/tasks/view/" + taskId;
    }

    @PostMapping("/toggle/{id}")
    public String toggleSubtask(@PathVariable Long id) {
        Long taskId = subtaskService.toogleSubtask(id);
        return "redirect:/tasks/view/" + taskId;
    }
}

