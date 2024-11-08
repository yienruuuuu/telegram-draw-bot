package io.github.yienruuuuu.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.yienruuuuu.bean.entity.Language;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Configuration
public class CacheConfig {
    @Bean
    public Cache<String, Language> languageCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES) // 10 分鐘後過期
                .maximumSize(5)
                .build();
    }
}
