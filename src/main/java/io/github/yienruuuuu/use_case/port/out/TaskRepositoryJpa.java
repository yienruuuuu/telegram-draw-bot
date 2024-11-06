package io.github.yienruuuuu.use_case.port.out;

import io.github.yienruuuuu.adapter.out.repository.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Eric.Lee
 * Date: 2024/11/6
 */
@Repository
public interface TaskRepositoryJpa extends JpaRepository<TaskEntity, Long>{
}
