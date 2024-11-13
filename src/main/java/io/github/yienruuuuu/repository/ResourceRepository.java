package io.github.yienruuuuu.repository;

import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.bean.enums.FileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Integer> {
    Optional<Resource> findByUniqueId(String uniqueId);

    List<Resource> findByFileType(FileType type);

    void deleteByUniqueId(String uniqueId);
}