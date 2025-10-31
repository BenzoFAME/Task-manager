package com.example.taskmanagervar.Service;

import com.example.taskmanagervar.entity.Priority;
import com.example.taskmanagervar.entity.Status;
import com.example.taskmanagervar.model.Category;
import com.example.taskmanagervar.model.Task;
import com.example.taskmanagervar.repository.CategoryRepository;
import com.example.taskmanagervar.repository.TaskRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;
    public TaskService(CategoryRepository categoryRepository, TaskRepository taskRepository) {
        this.categoryRepository = categoryRepository;
        this.taskRepository = taskRepository;
    }
    public List<Task> getArchivedTasks() {
        return taskRepository.findAllByArchivedTrue();
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

    public List<Task> getFilteredAndSortedTasks(Long categoryId, Priority priority, Status status,
                                                String filter, String sortBy, String keyword) {
        List<Task> tasks = taskRepository.findAllByArchivedFalse();

        if (keyword != null && !keyword.trim().isEmpty()) {
            tasks = tasks.stream()
                    .filter(t -> t.getName().toLowerCase().contains(keyword.toLowerCase())
                            || (t.getDescription() != null && t.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                    .collect(Collectors.toList());
        }
        if (categoryId != null) {
            tasks = tasks.stream()
                    .filter(t -> t.getCategory() != null && t.getCategory().getId().equals(categoryId))
                    .collect(Collectors.toList());
        }
        if (priority != null) {
            tasks = tasks.stream()
                    .filter(t -> t.getPriority() == priority)
                    .collect(Collectors.toList());
        }
        if (status != null) {
            tasks = tasks.stream()
                    .filter(t -> t.getStatus() == status)
                    .collect(Collectors.toList());
        }
        if ("overdue".equals(filter)) {
            tasks = tasks.stream()
                    .filter(t -> t.getDueDate() != null && t.getDueDate().isBefore(LocalDate.now()))
                    .collect(Collectors.toList());
        } else if ("completed".equals(filter)) {
            tasks = tasks.stream()
                    .filter(t -> t.getStatus() == Status.COMPLETED)
                    .collect(Collectors.toList());
        }
        if ("dueDate".equals(sortBy)) {
            tasks.sort(Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())));
        } else if ("priority".equals(sortBy)) {
            tasks.sort(Comparator.comparing(Task::getPriority));
        }
        return tasks;
    }


    public void archiveTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("not found"));
        task.setArchived(true);
        taskRepository.save(task);
    }
    public void unArchivedTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("not found"));
        task.setArchived(false);
        taskRepository.save(task);
    }
    public void changeStatus(Long taskId, Status newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("not found"));
        task.setStatus(newStatus);
        taskRepository.save(task);
    }

}
