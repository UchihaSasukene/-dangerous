package com.vueones.mapper;

import com.vueones.entity.UsageRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Date;

@Mapper
public interface UsageRecordMapper {
    // 基本CRUD操作
    int insert(UsageRecord record);
    int update(UsageRecord record);
    int deleteById(Integer id);
    UsageRecord selectById(Integer id);
    
    // 自定义查询方法
    List<UsageRecord> selectList(@Param("chemicalName") String chemicalName,
                               @Param("userName") String userName,
                               @Param("startTime") Date startTime,
                               @Param("endTime") Date endTime);
    
    // 统计查询
    Double getTotalAmount(@Param("chemicalName") String chemicalName,
                                @Param("startTime") Date startTime,
                                @Param("endTime") Date endTime);
                                
    List<UsageRecord> selectByUserName(@Param("userName") String userName);
    
    // 批量操作
    int batchInsert(List<UsageRecord> records);
    
    // 统计今日使用次数
    int countTodayUsage();
    
    // 统计本月使用次数
    int countMonthUsage();
    
    // 统计使用中数量
    int countActiveUsage();
    
    // 统计不同用户使用人数
    int countDistinctUsers();

    // 获取化学品总使用量
    String getTotalUsageAmount(@Param("chemicalName") String chemicalName,
                              @Param("startTime") Date startTime,
                              @Param("endTime") Date endTime);

    // 获取今日使用次数
    int getTodayUsageCount();

    // 获取本月使用次数
    int getMonthUsageCount();

    // 获取用户使用记录列表
    List<UsageRecord> getUserUsageRecords(@Param("userName") String userName);
} 