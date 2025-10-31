package com.example.taskmanagervar.Controller;

import com.example.taskmanagervar.Service.CategoryService;
import com.example.taskmanagervar.Service.SubtaskService;
import com.example.taskmanagervar.Service.TaskFileService;
import com.example.taskmanagervar.Service.TaskService;
import com.example.taskmanagervar.entity.Priority;
import com.example.taskmanagervar.entity.Status;
import com.example.taskmanagervar.model.Category;
import com.example.taskmanagervar.model.Subtask;
import com.example.taskmanagervar.model.Task;
import com.example.taskmanagervar.model.TaskFile;
import com.example.taskmanagervar.repository.CategoryRepository;
import com.example.taskmanagervar.repository.TaskRepository;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final CategoryService categoryService;
    private final SubtaskService subtaskService;
    private final TaskFileService taskFileService;
    private final TaskRepository taskRepository;

    public TaskController(TaskService taskService , CategoryService categoryService , SubtaskService subtaskService ,  TaskFileService taskFileService , TaskRepository taskRepository) {
        this.taskService = taskService;
        this.categoryService = categoryService;
        this.subtaskService = subtaskService;
        this.taskFileService = taskFileService;
        this.taskRepository = taskRepository;
    }
    @PostMapping("/save")
    public String saveTask(@ModelAttribute Task task, HttpServletRequest request,
                           @RequestParam("files") MultipartFile[] files , String keyword) throws IOException {

        // Сохраняем или обновляем задачу
        Task savedTask = (task.getId() == null) ? taskService.saveTask(task) : taskService.updateTask(task);

        // Сохраняем подзадачи
        String[] subtasks = request.getParameterValues("subtask");
        if (subtasks != null) {
            for (String subtask : subtasks) {
                if (subtask != null && !subtask.isBlank()) {
                    subtaskService.addSubtask(savedTask.getId(), subtask);
                }
            }
        }
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                taskFileService.saveFile(file, savedTask);
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

    @PostMapping("/archive/{id}")
    public String archiveTask(@PathVariable Long id) {
        taskService.archiveTask(id);
        return "redirect:/";
    }

    @PostMapping("/changestatus/{id}")
    public String changeStatus(@PathVariable Long id, @RequestParam Status status) {
        taskService.changeStatus(id, status);
        return "redirect:/tasks/view/" + id;
    }
    @GetMapping("/files/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException {
        TaskFile file = taskFileService.getById(id);

        Path path = Paths.get(file.getFilePath());

        if (!Files.exists(path)) {
            throw new RuntimeException("Файл не найден на диске: " + file.getFileName());
        }

        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(resource);
    }
}

