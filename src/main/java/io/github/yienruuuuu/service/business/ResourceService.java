package io.github.yienruuuuu.service.business;

import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.bean.enums.FileType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
public interface ResourceService {
    Optional<Resource> findByUniqueId(String uniqueId);

    Optional<Resource> findById(Integer id);

    List<Resource> findAll();

    Page<Resource> findAllByPage(Pageable pageable);

    List<Resource> findByType(FileType type);

    Resource save(Resource resource);

    void deleteById(String uniqueId);
}
