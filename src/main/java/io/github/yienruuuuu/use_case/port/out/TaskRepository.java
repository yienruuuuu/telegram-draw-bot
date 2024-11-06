package io.github.yienruuuuu.use_case.port.out;

import io.github.yienruuuuu.use_case.port.in.Task;

import java.util.List;

/**
 * @author Eric.Lee
 * Date: 2024/11/6
 */
public interface TaskRepository {
    Task findById(Long id);
    void save(Task task);
    List<Task> findAll();
}
