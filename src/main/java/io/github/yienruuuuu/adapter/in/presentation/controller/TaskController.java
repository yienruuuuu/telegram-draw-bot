package io.github.yienruuuuu.adapter.in.presentation.controller;

import io.github.yienruuuuu.use_case.service.TaskService;
import io.github.yienruuuuu.use_case.port.in.Task;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Eric.Lee
 * Date: 2024/11/6
 */
@Tag(name = "TEST Controller", description = "測試乾淨架構下流程")
@RestController
@RequestMapping("/tests")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    @PostMapping("/save")
    public ResponseEntity<Void> saveTask(@RequestBody Task task) {
        taskService.saveTask(task);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }
}
