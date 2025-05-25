package com.vueones.config;

import com.vueones.service.impl.UsageRecordServiceImpl;
import com.vueones.service.impl.ChemicalServiceImpl;
import com.vueones.service.impl.StorageRecordServiceImpl;
import com.vueones.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 应用启动监听器
 * 在应用启动时执行一些初始化操作，如清除缓存
 */
@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(ApplicationStartupListener.class);
    private static boolean isInitialized = false;
    
    @Autowired
    private UsageRecordServiceImpl usageRecordService;
    
    @Autowired(required = false)
    private ChemicalServiceImpl chemicalService;
    
    @Autowired(required = false)
    private StorageRecordServiceImpl storageRecordService;
    
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 确保只执行一次
        if (!isInitialized) {
            log.info("应用启动，执行初始化操作");
            
            // 清除所有缓存，避免ID重复问题
            clearAllCaches();
            
            isInitialized = true;
            log.info("初始化操作完成");
        }
    }
    
    /**
     * 清除所有缓存
     */
    private void clearAllCaches() {
        log.info("清除所有缓存");
        
        try {
            // 清除UsageRecord相关缓存
            usageRecordService.clearAllUsageRecordCaches();
            
            // 清除Chemical相关缓存（如果存在）
            if (chemicalService != null) {
                // 如果ChemicalServiceImpl有类似的清除缓存方法，可以调用
                // chemicalService.clearAllChemicalCaches();
                
                // 手动清除chemical相关缓存
                clearCachesByPattern("chemical:*");
                clearCachesByPattern("chemicalList:*");
            }
            
            // 清除StorageRecord相关缓存（如果存在）
            if (storageRecordService != null) {
                // 如果StorageRecordServiceImpl有类似的清除缓存方法，可以调用
                // storageRecordService.clearAllStorageRecordCaches();
                
                // 手动清除storageRecord相关缓存
                clearCachesByPattern("storage_record:*");
                clearCachesByPattern("storage_record_list:*");
            }
            
            log.info("所有缓存清除完成");
        } catch (Exception e) {
            log.error("清除缓存时发生错误", e);
        }
    }
    
    /**
     * 根据模式清除缓存
     * @param pattern 缓存键模式
     */
    private void clearCachesByPattern(String pattern) {
        try {
            // 获取并删除所有匹配模式的键
            java.util.Set<String> keys = redisUtil.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                for (String key : keys) {
                    redisUtil.del(key);
                }
                log.info("已清除{}个{}缓存", keys.size(), pattern);
            }
        } catch (Exception e) {
            log.error("清除{}缓存时发生错误", pattern, e);
        }
    }
}