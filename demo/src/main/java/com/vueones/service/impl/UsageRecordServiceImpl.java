package com.vueones.service.impl;

import com.vueones.entity.UsageRecord;
import com.vueones.mapper.UsageRecordMapper;
import com.vueones.service.IUsageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

@Service
public class UsageRecordServiceImpl implements IUsageRecordService {
    
    private static final Logger log = LoggerFactory.getLogger(UsageRecordServiceImpl.class);
    
    @Autowired
    private UsageRecordMapper usageRecordMapper;
    
    /**
     * 添加使用记录
     * @param record 使用记录
     * @return 是否成功
     */
    @Override
    @Transactional
    public int addUsageRecord(UsageRecord record) {
        if (record == null) {
            return 0;
        }
        
        // 设置创建时间
        if (record.getCreateTime() == null) {
            record.setCreateTime(new Date());
        }
        
        return usageRecordMapper.insert(record);
    }
    /**
     * 更新使用记录
     * @param record 使用记录
     * @return 是否成功
     */
    @Override
    @Transactional
    public int updateUsageRecord(UsageRecord record) {
        if (record == null || record.getId() == null) {
            return 0;
        }
        return usageRecordMapper.update(record);
    }
    /**
     * 删除使用记录
     * @param id 使用记录id
     * @return 是否成功
     */
    @Override
    @Transactional
    public int deleteUsageRecord(Integer id) {
        if (id == null) {
            return 0;
        }
        return usageRecordMapper.deleteById(id);
    }
    /**
     * 根据id查询使用记录
     * @param id 使用记录id
     * @return 使用记录
     */
    @Override
    public UsageRecord getUsageRecordById(Integer id) {
        if (id == null) {
            return null;
        }
        return usageRecordMapper.selectById(id);
    }
    /**
     * 根据化学品名称、用户名称、开始时间、结束时间查询使用记录
     * @param chemicalName 化学品名称
     * @param userName 用户名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 使用记录列表
     */
    @Override
    public List<UsageRecord> getUsageRecordList(String chemicalName, String userName,
                                               Date startTime, Date endTime) {
        // 过滤掉空字符串，转换为 null
        chemicalName = (chemicalName != null && !chemicalName.trim().isEmpty()) ? chemicalName.trim() : null;
        userName = (userName != null && !userName.trim().isEmpty()) ? userName.trim() : null;
        
        log.info("Service层处理后的参数: chemicalName={}, userName={}, startTime={}, endTime={}", 
                chemicalName, userName, startTime, endTime);
        
        // 允许部分参数为空，执行查询
        List<UsageRecord> result = usageRecordMapper.selectList(chemicalName, userName, startTime, endTime);
        log.info("查询结果条数: {}", result != null ? result.size() : 0);
        
        if (result != null && !result.isEmpty() && result.size() < 5) {
            // 打印一些结果示例，便于调试
            log.info("查询结果示例: 第一条记录ID={}, chemicalName={}, userName={}", 
                    result.get(0).getId(), 
                    result.get(0).getChemical() != null ? result.get(0).getChemical().getName() : null,
                    result.get(0).getUser() != null ? result.get(0).getUser().getName() : null);
        }
        
        return result;
    }
    /**
     * 根据用户名称查询使用记录
     * @param userName 用户名称
     * @return 使用记录列表
     */
    @Override
    public List<UsageRecord> getUserUsageRecords(String userName) {
        if (userName == null) {
            return new ArrayList<>();
        }
        return usageRecordMapper.selectByUserName(userName);
    }
    /**
     * 获取化学品总使用量
     * @param chemicalName 化学品名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 化学品总使用量
     */
    @Override
    public String getTotalUsageAmount(String chemicalName, Date startTime, Date endTime) {
        Double total = usageRecordMapper.getTotalAmount(chemicalName, startTime, endTime);
        if (total == null) {
            return "0";
        }
        return total.toString();
    }
    /**
     * 批量添加使用记录
     * @param records 使用记录列表
     * @return 是否成功
     */
    @Override
    @Transactional
    public int batchAddUsageRecords(List<UsageRecord> records) {
        if (records == null || records.isEmpty()) {
            return 0;
        }
        
        // 设置创建时间
        Date now = new Date();
        for (UsageRecord record : records) {
            if (record.getCreateTime() == null) {
                record.setCreateTime(now);
            }
        }
        
        return usageRecordMapper.batchInsert(records);
    }
    /**
     * 处理使用记录
     * @param chemicalName 化学品名称
     * @param userName 用户名称
     * @param amount 使用量
     * @param purpose 用途
     * @param notes 备注
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean processUsage(String chemicalName, String userName, Double amount, String purpose, String notes) {
        if (chemicalName == null || userName == null || amount == null) {
            return false;
        }
        
        // 创建新记录
        UsageRecord record = new UsageRecord();
        
        // 这里需要查询化学品和用户的ID，这里示例性实现
        // 实际项目中应该通过 ChemicalService 和 UserService 查询对应的 ID
        // 下面的代码仅供参考，需要根据实际情况修改
        // ChemicalMapper chemicalMapper = ...
        // ManMapper manMapper = ...
        // Integer chemicalId = chemicalMapper.selectIdByName(chemicalName);
        // Integer userId = manMapper.selectIdByName(userName);
        
        // 临时解决方案
        record.setChemicalName(chemicalName);
        record.setUserName(userName);
        // record.setChemicalId(chemicalId);
        // record.setUserId(userId);
        
        record.setAmount(amount);
        record.setUsagePurpose(purpose);
        record.setNotes(notes);
        record.setCreateTime(new Date());
        record.setUsageTime(new Date());
        
        return usageRecordMapper.insert(record) > 0;
    }
    
    /**
     * 获取今日使用记录数量
     * @return 今日使用记录数量
     */
    @Override
    public Integer getTodayUsageCount() {
        return usageRecordMapper.countTodayUsage();
    }
    /**
     * 获取本月使用记录数量
     * @return 本月使用记录数量
     */
    @Override
    public Integer getMonthUsageCount() {
        return usageRecordMapper.countMonthUsage();
    }
    /**
     * 获取活跃用户使用记录数量
     * @return 活跃用户使用记录数量
     */
    @Override
    public Integer getActiveUsageCount() {
        return usageRecordMapper.countActiveUsage();
    }
    /**
     * 获取不同用户使用记录数量
     * @return 不同用户使用记录数量
     */
    @Override
    public Integer getDistinctUserCount() {
        return usageRecordMapper.countDistinctUsers();
    }
    
    /**
     * 以下方法为接口兼容，将ID类型转换为名称类型的查询
     */
    @Override
    public List<UsageRecord> getUsageRecordList(Integer chemicalId, Integer userId, Date startTime, Date endTime) {
        // 这里需要查询化学品和用户的名称，然后调用名称查询方法
        // 因为实体中没有相应的方法，这里简单实现
        return new ArrayList<>(); 
    }

    @Override
    public List<UsageRecord> getUserUsageRecords(Integer userId) {
        // 同上，需要查询用户名称，这里简单实现
        return new ArrayList<>();
    }

    @Override
    public Double getTotalUsageAmount(Integer chemicalId, Date startTime, Date endTime) {
        // 同上，需要查询化学品名称，这里简单实现
        return 0.0;
    }
} 