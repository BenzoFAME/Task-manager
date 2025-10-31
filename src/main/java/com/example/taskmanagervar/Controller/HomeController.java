package com.example.taskmanagervar.Controller;

import com.example.taskmanagervar.Service.CategoryService;
import com.example.taskmanagervar.Service.TaskService;
import com.example.taskmanagervar.entity.Priority;
import com.example.taskmanagervar.entity.Status;
import com.example.taskmanagervar.model.Category;
import com.example.taskmanagervar.model.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Controller
public class HomeController {
    private final CategoryService categoryService;
    private final TaskService taskService;
    public HomeController(CategoryService categoryService, TaskService taskService) {
        this.categoryService = categoryService;
        this.taskService = taskService;
    }
    @GetMapping("/")
    public String home(@RequestParam(required = false) Long categoryId ,
                       @RequestParam(required = false) Priority priority ,
                       @RequestParam(required = false) Status status, String filter,
                       @RequestParam(required = false) String sortBy ,@RequestParam(required = false) String keyword,
                       Model model) {
        List<Task> tasks = taskService.getFilteredAndSortedTasks(categoryId, priority, status, filter, sortBy , keyword);
        model.addAttribute("tasks", tasks);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("taskForm", new Task());
        model.addAttribute("categoryForm", new Category());
        model.addAttribute("priorities" , Priority.values());
        model.addAttribute("statuses" , Status.values());
        model.addAttribute("keyword", keyword);

        return "index";
    }
    @GetMapping("/clear")
    public String cleanFilter(){
        return "redirect:/";
    }
}
