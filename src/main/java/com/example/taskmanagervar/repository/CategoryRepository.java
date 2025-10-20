package com.example.taskmanagervar.repository;

import com.example.taskmanagervar.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
