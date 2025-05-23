package com.vueones.service.impl;

import com.vueones.entity.StorageRecord;
import com.vueones.mapper.StorageRecordMapper;
import com.vueones.service.IStorageRecordService;
import com.vueones.mapper.ChemicalMapper;
import com.vueones.entity.Chemical;
import com.vueones.service.IInventoryService;
import com.vueones.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


@Service
public class StorageRecordServiceImpl implements IStorageRecordService {
    
    private static final Logger log = LoggerFactory.getLogger(StorageRecordServiceImpl.class);
    
    // 缓存相关常量
    private static final String CACHE_KEY_STORAGE_RECORD = "storage_record:";
    private static final String CACHE_KEY_STORAGE_RECORD_LIST = "storage_record_list:";
    private static final String CACHE_KEY_STORAGE_RECORD_COUNT = "storage_record_count:";
    private static final String CACHE_KEY_STORAGE_RECORD_SUM = "storage_record_sum:";
    private static final long CACHE_EXPIRE_TIME = 30; // 缓存过期时间（分钟）
    
    @Autowired
    private StorageRecordMapper storageRecordMapper;
    
    @Autowired
    private ChemicalMapper chemicalMapper;
    
    @Autowired
    private IInventoryService inventoryService;
    
    @Autowired
    private RedisUtil redisUtil;
    
    /**
     * 添加入库记录
     * @param record 入库记录
     * @return 是否成功
     */
    @Override
    @Transactional
    @CacheEvict(value = {"storageRecordList", "storageRecordCount", "storageRecordSum"}, allEntries = true)
    public int addStorageRecord(StorageRecord record) {
        try {
            log.info("Service层接收到添加入库记录请求");
            
            // 设置创建时间
            if (record.getCreateTime() == null) {
                record.setCreateTime(new Date());
            }
            
            // 设置入库时间，如果没有指定
            if (record.getStorageTime() == null) {
                record.setStorageTime(new Date());
            }
            
            // 如果提供了chemicalId但没有化学品名称，从数据库获取化学品名称
            if (record.getChemicalId() != null) {
                // 尝试从Chemical表获取化学品信息
                Chemical chemical = null;
                try {
                    chemical = chemicalMapper.selectChemicalById(record.getChemicalId());
                } catch (Exception e) {
                    log.warn("获取化学品信息失败", e);
                }
                
                if (chemical != null) {
                    // 设置化学品名称
                    record.setChemicalName(chemical.getName());
                    log.info("自动设置化学品名称: {}", chemical.getName());
                } else {
                    log.warn("无法找到ID为 {} 的化学品信息", record.getChemicalId());
                }
            }
            
            // 确保所有必要字段都有值
            if (record.getChemicalId() == null) {
                log.error("添加入库记录失败：缺少化学品ID");
                return 0;
            }
            
            if (record.getAmount() == null) {
                log.error("添加入库记录失败：缺少入库数量");
                return 0;
            }
            
            if (record.getUnit() == null || record.getUnit().isEmpty()) {
                log.error("添加入库记录失败：缺少计量单位");
                return 0;
            }
            
            // 记录即将插入的数据
            log.info("准备插入入库记录: chemicalId={}, chemicalName={}, amount={}, unit={}, supplier={}, operatorId={}", 
                    record.getChemicalId(), record.getChemicalName(), record.getAmount(), 
                    record.getUnit(), record.getSupplier(), record.getOperatorId());
            
            int result = storageRecordMapper.insert(record);
            log.info("插入入库记录结果: {}", result);
            
            // 如果入库成功，更新库存
            if (result > 0) {
                // 更新库存
                boolean inventoryUpdated = inventoryService.processStorageIn(record.getChemicalId(), record.getAmount());
                if (inventoryUpdated) {
                    log.info("入库成功后更新库存成功, 化学品ID: {}, 入库量: {}", record.getChemicalId(), record.getAmount());
                } else {
                    log.warn("入库成功但更新库存失败, 化学品ID: {}, 入库量: {}", record.getChemicalId(), record.getAmount());
                }
            }
            
            return result;
        } catch (Exception e) {
            log.error("添加入库记录异常", e);
            throw e;
        }
    }
    /**
     * 更新入库记录
     * @param record 入库记录
     * @return 是否成功
     */
    @Override
    @Transactional
    @CacheEvict(value = {"storageRecord", "storageRecordList", "storageRecordCount", "storageRecordSum"}, allEntries = true)
    public int updateStorageRecord(StorageRecord record) {
        // 清除单个记录的缓存
        String cacheKey = CACHE_KEY_STORAGE_RECORD + record.getId();
        redisUtil.del(cacheKey);
        
        return storageRecordMapper.update(record);
    }
    /**
     * 删除入库记录
     * @param id 入库记录id
     * @return 是否成功
     */
    @Override
    @Transactional
    @CacheEvict(value = {"storageRecord", "storageRecordList", "storageRecordCount", "storageRecordSum"}, allEntries = true)
    public int deleteStorageRecord(Integer id) {
        // 清除单个记录的缓存
        String cacheKey = CACHE_KEY_STORAGE_RECORD + id;
        redisUtil.del(cacheKey);
        
        return storageRecordMapper.deleteById(id);
    }
    /**
     * 根据id查询入库记录
     * @param id 入库记录id
     * @return 入库记录
     */
    @Override
    @Cacheable(value = "storageRecord", key = "#id", unless = "#result == null")
    public StorageRecord getStorageRecordById(Integer id) {
        log.info("从数据库查询入库记录, id: {}", id);
        
        // 尝试从缓存获取
        String cacheKey = CACHE_KEY_STORAGE_RECORD + id;
        Object cachedRecord = redisUtil.get(cacheKey);
        if (cachedRecord != null) {
            log.info("从缓存获取入库记录, id: {}", id);
            return (StorageRecord) cachedRecord;
        }
        
        // 从数据库获取
        StorageRecord record = storageRecordMapper.selectById(id);
        
        // 放入缓存
        if (record != null) {
            redisUtil.set(cacheKey, record, TimeUnit.MINUTES.toSeconds(CACHE_EXPIRE_TIME));
        }
        
        return record;
    }
    /**
     * 根据化学品id、化学品名称、供应商、入库时间查询入库记录
     * @param chemicalId 化学品id
     * @param chemicalName 化学品名称
     * @param supplier 供应商
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param offset 偏移量
     * @param size 分页大小
     * @return 入库记录列表
     */
    @Override
    @Cacheable(value = "storageRecordList", key = "#chemicalId + '_' + #chemicalName + '_' + #supplier + '_' + #startTime + '_' + #endTime + '_' + #offset + '_' + #size", unless = "#result == null || #result.isEmpty()")
    public List<StorageRecord> getStorageRecordList(Integer chemicalId, String chemicalName, String supplier, Date startTime, Date endTime, Integer offset, Integer size) {
        log.info("从数据库查询入库记录列表, chemicalId: {}, chemicalName: {}, supplier: {}, startTime: {}, endTime: {}, offset: {}, size: {}", 
                chemicalId, chemicalName, supplier, startTime, endTime, offset, size);
        
        // 构建缓存key
        String cacheKey = CACHE_KEY_STORAGE_RECORD_LIST + 
                (chemicalId != null ? chemicalId : "null") + "_" + 
                (chemicalName != null ? chemicalName : "null") + "_" + 
                (supplier != null ? supplier : "null") + "_" + 
                (startTime != null ? startTime.getTime() : "null") + "_" + 
                (endTime != null ? endTime.getTime() : "null") + "_" + 
                offset + "_" + size;
        
        // 尝试从缓存获取
        Object cachedList = redisUtil.get(cacheKey);
        if (cachedList != null) {
            log.info("从缓存获取入库记录列表");
            return (List<StorageRecord>) cachedList;
        }
        
        // 从数据库获取
        List<StorageRecord> records = storageRecordMapper.selectList(chemicalId, chemicalName, startTime, endTime, supplier, offset, size);
        List<StorageRecord> result = records != null ? records : new ArrayList<>();
        
        // 放入缓存
        if (!result.isEmpty()) {
            redisUtil.set(cacheKey, result, TimeUnit.MINUTES.toSeconds(CACHE_EXPIRE_TIME));
        }
        
        return result;
    }
    
    /**
     * 统计入库记录数量
     * @param chemicalId 化学品id
     * @param chemicalName 化学品名称
     * @param supplier 供应商
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 记录数量
     */
    @Override
    @Cacheable(value = "storageRecordCount", key = "#chemicalId + '_' + #chemicalName + '_' + #supplier + '_' + #startTime + '_' + #endTime")
    public int countStorageRecords(Integer chemicalId, String chemicalName, String supplier, Date startTime, Date endTime) {
        log.info("从数据库统计入库记录数量, chemicalId: {}, chemicalName: {}, supplier: {}, startTime: {}, endTime: {}", 
                chemicalId, chemicalName, supplier, startTime, endTime);
        
        // 构建缓存key
        String cacheKey = CACHE_KEY_STORAGE_RECORD_COUNT + 
                (chemicalId != null ? chemicalId : "null") + "_" + 
                (chemicalName != null ? chemicalName : "null") + "_" + 
                (supplier != null ? supplier : "null") + "_" + 
                (startTime != null ? startTime.getTime() : "null") + "_" + 
                (endTime != null ? endTime.getTime() : "null");
        
        // 尝试从缓存获取
        Object cachedCount = redisUtil.get(cacheKey);
        if (cachedCount != null) {
            log.info("从缓存获取入库记录数量");
            return (int) cachedCount;
        }
        
        // 从数据库获取
        Integer count = storageRecordMapper.countStorageRecords(chemicalId, chemicalName, supplier, startTime, endTime);
        int result = count != null ? count : 0;
        
        // 放入缓存
        redisUtil.set(cacheKey, result, TimeUnit.MINUTES.toSeconds(CACHE_EXPIRE_TIME));
        
        return result;
    }
    
    /**
     * 计算入库总量
     * @param chemicalId 化学品id
     * @param chemicalName 化学品名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 入库总量
     */
    @Override
    @Cacheable(value = "storageRecordSum", key = "#chemicalId + '_' + #chemicalName + '_' + #startTime + '_' + #endTime")
    public Double sumStorageAmount(Integer chemicalId, String chemicalName, Date startTime, Date endTime) {
        log.info("从数据库计算入库总量, chemicalId: {}, chemicalName: {}, startTime: {}, endTime: {}", 
                chemicalId, chemicalName, startTime, endTime);
        
        // 构建缓存key
        String cacheKey = CACHE_KEY_STORAGE_RECORD_SUM + 
                (chemicalId != null ? chemicalId : "null") + "_" + 
                (chemicalName != null ? chemicalName : "null") + "_" + 
                (startTime != null ? startTime.getTime() : "null") + "_" + 
                (endTime != null ? endTime.getTime() : "null");
        
        // 尝试从缓存获取
        Object cachedSum = redisUtil.get(cacheKey);
        if (cachedSum != null) {
            log.info("从缓存获取入库总量");
            return (Double) cachedSum;
        }
        
        // 从数据库获取
        Double sum = storageRecordMapper.sumAmountByChemicalId(chemicalId, chemicalName, startTime, endTime);
        
        // 放入缓存
        if (sum != null) {
            redisUtil.set(cacheKey, sum, TimeUnit.MINUTES.toSeconds(CACHE_EXPIRE_TIME));
        }
        
        return sum;
    }
    
    /**
     * 批量添加入库记录
     * @param records 入库记录列表
     * @return 是否成功
     */
    @Override
    @Transactional
    @CacheEvict(value = {"storageRecordList", "storageRecordCount", "storageRecordSum"}, allEntries = true)
    public int batchAddStorageRecords(List<StorageRecord> records) {
        if (records == null || records.isEmpty()) {
            return 0;
        }
        // 设置创建时间
        Date now = new Date();
        for (StorageRecord record : records) {
            if (record.getCreateTime() == null) {
                record.setCreateTime(now);
            }
        }
        
        // 清除相关缓存
        log.info("批量添加入库记录，清除相关缓存");
        
        return storageRecordMapper.batchInsert(records);
    }
    
    /**
     * 获取入库总量
     * @param chemicalId 化学品id
     * @param chemicalName 化学品名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 入库总量
     */
    @Override
    public Double getTotalAmountByChemicalId(Integer chemicalId, String chemicalName, Date startTime, Date endTime) {
        return storageRecordMapper.sumAmountByChemicalId(chemicalId, chemicalName, startTime, endTime);
    }
    
    /**
     * 获取月度入库数量
     * @param chemicalId 化学品id
     * @param chemicalName 化学品名称
     * @return 月度入库数量
     */
    @Override
    public int getMonthlyStorageCount(Integer chemicalId, String chemicalName) {
        // 直接调用mapper的getMonthlyStorageCount方法
        return storageRecordMapper.getMonthlyStorageCount(chemicalId, chemicalName);
    }
    /**
     * 获取月度入库次数
     * @param chemicalId 化学品ID
     * @param chemicalName 化学品名称
     * @return 月度入库次数
     */
    @Override
    public int getMonthlyStorageTimes(Integer chemicalId, String chemicalName) {
        Integer result = storageRecordMapper.getMonthlyStorageTimes(chemicalId, chemicalName);
        return result != null ? result : 0;
    }
    

    /**
     * 获取当日入库数量
     * @return 当日入库数量
     */
    public int getDailyStorageCount(Integer chemicalId, String chemicalName) {
        // 直接调用mapper的getDailyStorageCount方法
        return storageRecordMapper.getDailyStorageCount(chemicalId, chemicalName);
    }

    /**
     * 获取当日入库次数
     * @param chemicalId 化学品ID
     * @param chemicalName 化学品名称
     * @return 当日入库次数
     */
    @Override
    public int getDailyStorageTimes(Integer chemicalId, String chemicalName) {
        Integer result = storageRecordMapper.getDailyStorageTimes(chemicalId, chemicalName);
        return result != null ? result : 0;
    }

}