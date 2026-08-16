package in.practice.urlshortner.cache;

import in.practice.urlshortner.entity.UrlMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CacheTemplate {

    private static final String CACHE_KEY_PREFIX = "shortcode:";
    private final RedisTemplate<String, String> redisTemplate;

    public CacheTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setCache(final String shortCode, final String longUrl, final LocalDateTime expiryAt) {
        try {
            long secondsUntilExpiry = Duration.between(LocalDateTime.now(), expiryAt).getSeconds();
            if (secondsUntilExpiry > 0) {
                redisTemplate.opsForValue()
                        .set(CACHE_KEY_PREFIX + shortCode, longUrl, secondsUntilExpiry, TimeUnit.SECONDS);
            } else {
                log.warn("Skipped caching short code {} — already at/past expiry", shortCode);
            }
        } catch (Exception e) {
            log.error("Error occurred while setting cache for short code {}", shortCode, e);
        }
    }

    public String getURLFromCache(final String shortCode) {
        try {
            return redisTemplate.opsForValue().get(CACHE_KEY_PREFIX + shortCode);
        } catch (Exception e) {
            log.error("Error occurred while getting short code {}", shortCode, e);
            return null;
        }
    }
}
