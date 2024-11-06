package io.github.yienruuuuu.adapter.out.repository.mapper;

import io.github.yienruuuuu.use_case.port.in.Task;
import io.github.yienruuuuu.adapter.out.repository.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Eric.Lee
 * Date: 2024/11/6
 */
@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    Task toDomain(TaskEntity entity);
    TaskEntity toEntity(Task task);
}
