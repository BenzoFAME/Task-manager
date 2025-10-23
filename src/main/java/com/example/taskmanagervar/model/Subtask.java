package com.example.taskmanagervar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "subtask")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subtask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private boolean completed = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id" , nullable = false)
    private Task task;

    public Subtask(String name, Task task) {
        this.name = name;
        this.task = task;
        this.createdAt = LocalDateTime.now();
    }
}
