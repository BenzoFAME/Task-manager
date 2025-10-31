package com.example.taskmanagervar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.support.SimpleTriggerContext;

@Entity
@Data
@Table(name = "task_files")
@AllArgsConstructor
@NoArgsConstructor
public class TaskFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String filePath;
    private String fileType;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
