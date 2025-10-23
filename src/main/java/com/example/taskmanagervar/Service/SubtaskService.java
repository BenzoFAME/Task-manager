package com.example.taskmanagervar.Service;

import com.example.taskmanagervar.model.Subtask;
import com.example.taskmanagervar.model.Task;
import com.example.taskmanagervar.repository.SubtaskRepository;
import com.example.taskmanagervar.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class SubtaskService {
    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;

    public SubtaskService(SubtaskRepository subtaskRepository, TaskRepository taskRepository) {
        this.subtaskRepository = subtaskRepository;
        this.taskRepository = taskRepository;
    }
    public List<Subtask> getSubtasksByTaskId(Long taskId) {
        return subtaskRepository.findByTaskId(taskId);
    }
    public void addSubtask(Long taskId , String name) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Subtask subtask = new Subtask(name , task);
        subtaskRepository.save(subtask);
    }
    public void deleteSubtask(Long subtaskId) {
        subtaskRepository.deleteById(subtaskId);
    }

    public Long toogleSubtask(Long subtaskId) {
        Subtask subtask = subtaskRepository.findById(subtaskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        subtask.setCompleted(!subtask.isCompleted());
        subtaskRepository.save(subtask);
        return subtask.getTask().getId();
    }

    public double getProgress(Long taskId) {
        List<Subtask> subtasks = subtaskRepository.findByTaskId(taskId);
        if (subtasks.isEmpty()) {
            return 0;
        }
        long done = subtasks.stream().filter(Subtask::isCompleted).count();
        return (done * 100) / subtasks.size();
    }
}
