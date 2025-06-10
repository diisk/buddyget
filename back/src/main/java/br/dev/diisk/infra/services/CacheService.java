package br.dev.diisk.infra.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import br.dev.diisk.application.shared.services.ICacheService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CacheService implements ICacheService {

    private final CacheManager cacheManager;

    public void evictCache(String value, String startsWith) {
        Cache cache = cacheManager.getCache(value);
        if (cache != null) {
            for (Object key : getAllCacheKeys(cache)) {
                if (key.toString().startsWith(startsWith))
                    cache.evict(key);

            }
        }
    }

    private List<Object> getAllCacheKeys(Cache cache) {
        if (cache.getNativeCache() instanceof com.github.benmanes.caffeine.cache.Cache) {
            com.github.benmanes.caffeine.cache.Cache<?, ?> nativeCache = (com.github.benmanes.caffeine.cache.Cache<?, ?>) cache
                    .getNativeCache();
            return new ArrayList<>(nativeCache.asMap().keySet());
        }
        return new ArrayList<>();
    }

}
