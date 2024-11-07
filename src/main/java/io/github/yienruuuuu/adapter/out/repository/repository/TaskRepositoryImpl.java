package io.github.yienruuuuu.adapter.out.repository.repository;

import io.github.yienruuuuu.use_case.port.in.task.Task;
import io.github.yienruuuuu.adapter.out.repository.entity.TaskEntity;
import io.github.yienruuuuu.adapter.out.repository.mapper.TaskMapper;
import io.github.yienruuuuu.use_case.port.out.task.TaskRepository;
import io.github.yienruuuuu.use_case.port.out.task.TaskRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Eric.Lee
 * Date: 2024/11/6
 */
@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final TaskRepositoryJpa taskRepositoryJpa;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskRepositoryImpl(TaskRepositoryJpa taskRepositoryJpa, TaskMapper taskMapper) {
        this.taskRepositoryJpa = taskRepositoryJpa;
        this.taskMapper = taskMapper;
    }

    @Override
    public Task findById(Long id) {
        return taskRepositoryJpa.findById(id)
                .map(taskMapper::toDomain)
                .orElse(null);
    }

    @Override
    public void save(Task task) {
        TaskEntity entity = taskMapper.toEntity(task);
        taskRepositoryJpa.save(entity);
    }

    @Override
    public List<Task> findAll() {
        return taskRepositoryJpa.findAll().stream()
                .map(taskMapper::toDomain)
                .collect(Collectors.toList());
    }
}
