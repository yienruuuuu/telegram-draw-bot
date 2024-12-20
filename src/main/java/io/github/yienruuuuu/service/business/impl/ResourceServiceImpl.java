package io.github.yienruuuuu.service.business.impl;

import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.bean.enums.FileType;
import io.github.yienruuuuu.repository.ResourceRepository;
import io.github.yienruuuuu.service.business.ResourceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/13
 */
@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public Optional<Resource> findByUniqueId(String uniqueId) {
        return resourceRepository.findByUniqueId(uniqueId);
    }

    @Override
    public Optional<Resource> findById(Integer id) {
        return resourceRepository.findById(id);
    }

    @Override
    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    @Override
    public Page<Resource> findAllByPage(Pageable pageable) {
        return resourceRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Page<Resource> findAllByPageExcludingIds(Pageable pageable, List<Integer> excludedIds) {
        if (excludedIds.isEmpty()) {
            return resourceRepository.findAll(pageable);
        }
        return resourceRepository.findAllByIdNotInOrderByCreatedAtDesc(excludedIds, pageable);
    }

    @Override
    public List<Resource> findByType(FileType type) {
        return resourceRepository.findByFileType(type);
    }

    @Override
    public Resource save(Resource resource) {
        return resourceRepository.save(resource);
    }

    @Transactional
    @Override
    public void deleteById(String uniqueId) {
        resourceRepository.deleteByUniqueId(uniqueId);
    }
}
