package com.vueones.service;

import com.vueones.entity.UsageRecord;
import java.util.List;
import java.util.Date;

public interface IUsageRecordService {
    // 基本操作
    int addUsageRecord(UsageRecord record);
    int updateUsageRecord(UsageRecord record);
    int deleteUsageRecord(Integer id);
    UsageRecord getUsageRecordById(Integer id);
    
    /**
     * 获取使用记录列表
     * @param chemicalId 化学品ID
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 使用记录列表
     */
    List<UsageRecord> getUsageRecordList(Integer chemicalId, Integer userId, 
                                        Date startTime, Date endTime);
    
    /**
     * 获取用户使用记录列表
     * @param userId 用户ID
     * @return 用户使用记录列表
     */
    List<UsageRecord> getUserUsageRecords(Integer userId);
    
    /**
     * 获取化学品ID的使用记录数量
     * @param chemicalId 化学品ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 使用记录数量
     */
    Double getTotalUsageAmount(Integer chemicalId, Date startTime, Date endTime);
    
    /**
     * 批量添加使用记录
     * @param records 使用记录列表
     * @return 是否成功
     */
    int batchAddUsageRecords(List<UsageRecord> records);
    
    /**
     * 使用记录处理
     * @param chemicalName 化学品名称
     * @param userName 用户名称
     * @param amount 使用数量
     * @param purpose 用途
     * @param notes 备注
     * @return 是否成功
     */
    boolean processUsage(String chemicalName, String userName, Double amount, 
                        String purpose, String notes);
    
    /**
     * 获取使用中数量
     * @return 使用中数量
     */
    Integer getActiveUsageCount();
    
    /**
     * 获取使用人数
     * @return 使用人数
     */
    Integer getDistinctUserCount();
    List<UsageRecord> getUsageRecordList(String chemicalName, String userName, Date startTime, Date endTime);

    /**
     * 获取用户使用记录列表
     * @param userName 用户名称
     * @return 用户使用记录列表
     */
    List<UsageRecord> getUserUsageRecords(String userName);

    /**
     * 获取化学品总使用量
     * @param chemicalName 化学品名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 化学品总使用量
     */
    String getTotalUsageAmount(String chemicalName, Date startTime, Date endTime);

    /**
     * 获取今日使用次数
     * @return 今日使用次数
     */
    Integer getTodayUsageCount();

    /**
     * 获取本月使用次数
     * @return 本月使用次数
     */
    Integer getMonthUsageCount();
} 