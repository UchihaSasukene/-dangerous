package com.vueones.service.impl;

import com.vueones.entity.Chemical;
import com.vueones.mapper.ChemicalMapper;
import com.vueones.service.IChemicalService;
import com.vueones.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ChemicalServiceImpl implements IChemicalService {
    
    private static final Logger log = LoggerFactory.getLogger(ChemicalServiceImpl.class);
    
    // 缓存相关常量
    private static final String CACHE_KEY_CHEMICAL = "chemical:";
    private static final String CACHE_KEY_CHEMICAL_LIST = "chemical_list:";
    private static final String CACHE_KEY_CHEMICAL_COUNT = "chemical_count:";
    private static final long CACHE_EXPIRE_TIME = 30; // 缓存过期时间（分钟）
    
    @Autowired
    private ChemicalMapper chemicalMapper;
    
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询全部危化品数据
     * @return List<Chemical>
     */
    @Override
    @Cacheable(value = "chemicalList", key = "'all'", unless = "#result == null || #result.isEmpty()")
    public List<Chemical> listChemical() {
        log.info("从数据库查询所有化学品");
        
        // 尝试从缓存获取
        String cacheKey = CACHE_KEY_CHEMICAL_LIST + "all";
        Object cachedChemicals = redisUtil.get(cacheKey);
        if (cachedChemicals != null) {
            log.info("从缓存获取所有化学品");
            return (List<Chemical>) cachedChemicals;
        }
        
        // 从数据库获取
        List<Chemical> chemicals = chemicalMapper.listChemical(null, null, null, null);
        
        // 放入缓存
        if (chemicals != null && !chemicals.isEmpty()) {
            redisUtil.set(cacheKey, chemicals, TimeUnit.MINUTES.toSeconds(CACHE_EXPIRE_TIME));
        }
        
        return chemicals;
    }
    
    /**
     * 分页查询危化品数据
     * @param name 名称（可选）
     * @param category 类别（可选）
     * @param page 页码
     * @param size 每页大小
     * @return List<Chemical>
     */
    @Override
    public List<Chemical> listChemical(String name, String category, Integer page, Integer size) {
        // 添加日志输出
        System.out.println("Service层接收到的参数: name=" + name + ", category=" + category + ", page=" + page + ", size=" + size);
        
        // 确保分页参数有效
        if (page == null || page < 0) {
            page = 0;
        }
        if (size == null || size <= 0) {
            size = 10;
        }
        
        // 添加日志输出
        System.out.println("Service层处理后的参数: name=" + name + ", category=" + category + ", page=" + page + ", size=" + size);
        
        try {
            List<Chemical> result = chemicalMapper.listChemical(name, category, page, size);
            
            // 添加日志输出
            System.out.println("Service层查询结果: result.size=" + (result != null ? result.size() : "null"));
            
            return result;
        } catch (Exception e) {
            System.err.println("查询出错: " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    /**
     * 根据 id 查询一条 危化品 数据
     * @param id
     * @return Chemical
     */
    @Override
    @Cacheable(value = "chemical", key = "#id", unless = "#result == null")
    public Chemical selectChemicalById(Integer id) {
        log.info("从数据库查询化学品, id: {}", id);
        
        // 尝试从缓存获取
        String cacheKey = CACHE_KEY_CHEMICAL + id;
        Object cachedChemical = redisUtil.get(cacheKey);
        if (cachedChemical != null) {
            log.info("从缓存获取化学品, id: {}", id);
            return (Chemical) cachedChemical;
        }
        
        // 从数据库获取
        Chemical chemical = chemicalMapper.selectChemicalById(id);
        
        // 放入缓存
        if (chemical != null) {
            redisUtil.set(cacheKey, chemical, TimeUnit.MINUTES.toSeconds(CACHE_EXPIRE_TIME));
        }
        
        return chemical;
    }

    /**
     * 新增一条 危化品 数据
     * @param chemical
     */
    @Override
    @Transactional
    @CacheEvict(value = {"chemicalList", "chemicalCount"}, allEntries = true)
    public int addChemical(Chemical chemical) {
        return chemicalMapper.addChemical(chemical);
    }

    /**
     * 根据 id 删除一条 危化品 
     * @param id
     */
    @Override
    @Transactional
    @CacheEvict(value = {"chemical", "chemicalList", "chemicalCount"}, allEntries = true)
    public int removeChemical(Integer id) {
        // 清除单个记录的缓存
        String cacheKey = CACHE_KEY_CHEMICAL + id;
        redisUtil.del(cacheKey);
        
        return chemicalMapper.removeChemical(id);
    }

    /**
     * 更新一条 危化品 数据
     * @param chemical
     */
    @Override
    @Transactional
    @CacheEvict(value = {"chemical", "chemicalList", "chemicalCount"}, allEntries = true)
    public int editChemical(Chemical chemical) {
        // 清除单个记录的缓存
        String cacheKey = CACHE_KEY_CHEMICAL + chemical.getId();
        redisUtil.del(cacheKey);
        
        return chemicalMapper.editChemical(chemical);
    }
    
    /**
     * 统计 Chemical 数据总数
     * @param params 查询参数
     * @return int
     */
    @Override
    @Cacheable(value = "chemicalCount", key = "'params_' + #params", unless = "#result == 0")
    public int countChemical(Map<String, Object> params) {
        log.info("从数据库统计化学品数量, params: {}", params);
        
        // 尝试从缓存获取
        String cacheKey = CACHE_KEY_CHEMICAL_COUNT + "params_" + (params != null ? params.hashCode() : "null");
        Object cachedCount = redisUtil.get(cacheKey);
        if (cachedCount != null) {
            log.info("从缓存获取化学品数量");
            return (int) cachedCount;
        }
        
        // 从数据库获取
        int count = chemicalMapper.countChemical(params);
        
        // 放入缓存
        redisUtil.set(cacheKey, count, TimeUnit.MINUTES.toSeconds(CACHE_EXPIRE_TIME));
        
        return count;
    }
    
    /**
     * 根据危险等级统计数量
     * @return List<Map<String, Object>>
     */
    @Override
    public List<Map<String, Object>> countByDangerLevel() {
        return chemicalMapper.countByDangerLevel();
    }
    
    /**
     * 根据存储条件统计数量
     * @return List<Map<String, Object>>
     */
    @Override
    public List<Map<String, Object>> countByStorageCondition() {
        return chemicalMapper.countByStorageCondition();
    }
    
    /**
     * 查询低于预警阈值的化学品
     * @return List<Chemical>
     */
    @Override
    public List<Chemical> listBelowWarningThreshold() {
        return chemicalMapper.listBelowWarningThreshold();
    }
}