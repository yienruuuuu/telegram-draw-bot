package io.github.yienruuuuu.config;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.management.ManagementFactory;
import java.util.concurrent.Executor;

/**
 * @author Eric.Lee
 * Date: 2024/11/21
 */
@Configuration
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        int availableProcessors = osBean.getAvailableProcessors();

        // 動態調整核心線程和最大線程
        int corePoolSize = Math.max(1, availableProcessors / 2);
        int maxPoolSize = Math.max(2, availableProcessors);

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(20); // 隊列大小
        executor.setThreadNamePrefix("Dynamic-GCP-Thread-");
        executor.initialize();
        return executor;
    }
}