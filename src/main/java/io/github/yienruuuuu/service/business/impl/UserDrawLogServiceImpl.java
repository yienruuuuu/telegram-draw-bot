package io.github.yienruuuuu.service.business.impl;

import io.github.yienruuuuu.bean.entity.UserDrawLog;
import io.github.yienruuuuu.repository.UserDrawLogRepository;
import io.github.yienruuuuu.service.business.UserDrawLogService;
import org.springframework.stereotype.Service;

/**
 * @author Eric.Lee
 * Date: 2024/11/21
 */
@Service("userDrawLogService")
public class UserDrawLogServiceImpl implements UserDrawLogService {
    private final UserDrawLogRepository userDrawLogRepository;

    public UserDrawLogServiceImpl(UserDrawLogRepository userDrawLogRepository) {
        this.userDrawLogRepository = userDrawLogRepository;
    }

    @Override
    public UserDrawLog save(UserDrawLog userDrawLog) {
        return userDrawLogRepository.save(userDrawLog);
    }
}
