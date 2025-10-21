package com.example.taskmanagervar.Service;

import com.example.taskmanagervar.entity.Priority;
import com.example.taskmanagervar.entity.Status;
import com.example.taskmanagervar.model.Category;
import com.example.taskmanagervar.model.Task;
import com.example.taskmanagervar.repository.CategoryRepository;
import com.example.taskmanagervar.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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

    public List<Task> getFilteredAndSortedTasks(Long categoryId , Priority priority , Status status, String filter , String sortBy) {
        List<Task> tasks = taskRepository.findAll();
        if (categoryId != null) {
            tasks = tasks.stream()
                    .filter(t -> t.getCategory() != null && t.getCategory().getId().equals(categoryId)).toList();
        }
        if (priority != null) {
            tasks = tasks.stream()
                    .filter(t -> t.getPriority() == priority).toList();
        }
        if (status != null) {
            tasks = tasks.stream()
                    .filter(t -> t.getStatus() == status).toList();
        }
        if ("overdue".equals(filter)) {
            tasks = tasks.stream()
                    .filter(t -> t.getDueDate() != null && t.getDueDate().isBefore(LocalDate.now())).toList();
        } else if ("completed".equals(filter)) {
            tasks = tasks.stream()
                    .filter(t->t.getStatus() == Status.COMPLETED).toList();
        }
        if (sortBy == null) {
            sortBy = "createdAt"; // сортировка по умолчанию
        }

        Comparator<Task> comparator = switch (sortBy) {
            case "dueDate" -> Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()));
            case "priority" -> Comparator.comparing(Task::getPriority);
            default -> Comparator.comparing(Task::getCreatedAt);
        };
        return tasks.stream().sorted(comparator).toList();
    }
}
