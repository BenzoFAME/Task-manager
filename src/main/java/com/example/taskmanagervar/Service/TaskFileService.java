package com.example.taskmanagervar.Service;

import com.example.taskmanagervar.model.Task;
import com.example.taskmanagervar.model.TaskFile;
import com.example.taskmanagervar.repository.TaskFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class TaskFileService {
    private TaskFileRepository taskFileRepository;
    private final String uploadDir = "uploads/";
    public TaskFileService(TaskFileRepository taskFileRepository) {
        this.taskFileRepository = taskFileRepository;
    }

    public TaskFile saveFile(MultipartFile file , Task task) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir,fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        TaskFile newTaskFile = new TaskFile();
        newTaskFile.setFileName(file.getOriginalFilename());
        newTaskFile.setFileType(file.getContentType());
        newTaskFile.setFilePath(filePath.toString());
        newTaskFile.setTask(task);

        return taskFileRepository.save(newTaskFile);
    }

    public List<TaskFile> getTaskFilesById(Long taskId) {
        return taskFileRepository.findByTaskId(taskId);
    }

    public TaskFile getById(Long id) {
        return taskFileRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
    }
}
