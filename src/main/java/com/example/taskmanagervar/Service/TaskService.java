package com.example.taskmanagervar.Service;

import com.example.taskmanagervar.model.Category;
import com.example.taskmanagervar.model.Task;
import com.example.taskmanagervar.repository.CategoryRepository;
import com.example.taskmanagervar.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;
    public TaskService(CategoryRepository categoryRepository, TaskRepository taskRepository) {
        this.categoryRepository = categoryRepository;
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
    }
    public Task saveTask(Task task) {
        if (task.getCategory() != null && task.getCategory().getId() != null) {
            Category category = categoryRepository.findById(task.getCategory().getId()).get();
            task.setCategory(category);
        }
        return taskRepository.save(task);
    }
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
    public List<Task> getTasksByCategory(Category category) {
        return taskRepository.findByCategory(category);
    }

    public Task updateTask(Task task) {
        Task task1 = taskRepository.findById(task.getId()).orElseThrow(() -> new RuntimeException("not found"));
        task1.setName(task.getName());
        task1.setDescription(task.getDescription());
        task1.setDueDate(task.getDueDate());
        task1.setPriority(task.getPriority());
        task1.setStatus(task.getStatus());

        if (task.getCategory() != null && task.getCategory().getId() != null) {
            Category category = categoryRepository.findById(task.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category with id " + task.getCategory().getId() + " not found"));
            task1.setCategory(category);
        }else {
            task1.setCategory(null);
        }
        return taskRepository.save(task1);
    }
}
