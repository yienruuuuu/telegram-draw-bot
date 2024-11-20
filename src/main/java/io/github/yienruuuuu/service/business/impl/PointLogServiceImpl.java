package io.github.yienruuuuu.service.business.impl;

import io.github.yienruuuuu.bean.entity.PointLog;
import io.github.yienruuuuu.repository.PointLogRepository;
import io.github.yienruuuuu.service.business.PointLogService;
import org.springframework.stereotype.Service;

/**
 * @author Eric.Lee
 * Date: 2024/11/20
 */
@Service("pointLogService")
public class PointLogServiceImpl implements PointLogService {
    private final PointLogRepository pointLogRepository;

    public PointLogServiceImpl(PointLogRepository pointLogRepository) {
        this.pointLogRepository = pointLogRepository;
    }

    @Override
    public PointLog save(PointLog pointLog) {
        return pointLogRepository.save(pointLog);
    }
}
