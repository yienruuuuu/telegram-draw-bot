package io.github.yienruuuuu.repository;

import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.bean.enums.FileType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Integer> {
    Page<Resource> findByInUsedOrderByCreatedAtDesc(boolean inUsed, Pageable pageable);

    Page<Resource> findAllByIdNotInOrderByCreatedAtDesc(List<Integer> excludedIds, Pageable pageable);

    Optional<Resource> findByUniqueId(String uniqueId);

    List<Resource> findByFileType(FileType type);

    void deleteByUniqueId(String uniqueId);
}