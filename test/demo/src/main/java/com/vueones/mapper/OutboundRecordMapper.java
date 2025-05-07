package com.vueones.mapper;

import com.vueones.entity.OutboundRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Date;

@Mapper
public interface OutboundRecordMapper {
    // 基本CRUD操作
    int insert(OutboundRecord record);
    int update(OutboundRecord record);
    int deleteById(Integer id);
    OutboundRecord selectById(Integer id);
    
    // 自定义查询方法
    List<OutboundRecord> selectList(@Param("chemicalId") Integer chemicalId,
                                  @Param("chemicalName") String chemicalName,
                                  @Param("startTime") Date startTime,
                                  @Param("endTime") Date endTime,
                                  @Param("recipient") String recipient,
                                  @Param("offset") Integer offset,
                                  @Param("size") Integer size);
    
    // 批量操作
    int batchInsert(List<OutboundRecord> records);
    
    // 统计查询
    Double sumAmountByChemicalId(@Param("chemicalId") Integer chemicalId,
                                @Param("startTime") Date startTime,
                                @Param("endTime") Date endTime);
    
    // 统计时间范围内的出库记录数量
    Integer countByDateRange(@Param("startTime") Date startTime, 
                           @Param("endTime") Date endTime);
    
    // 获取月度出库记录数量
    Integer getMonthlyOutboundCount(@Param("chemicalId") Integer chemicalId,
                                  @Param("chemicalName") String chemicalName);
    
    // 获取当日出库记录数量
    Integer getDailyOutboundCount(@Param("chemicalId") Integer chemicalId,
                                @Param("chemicalName") String chemicalName);
    
    // 统计符合条件的出库记录数量
    Integer countOutboundRecords(@Param("chemicalId") Integer chemicalId,
                              @Param("chemicalName") String chemicalName,
                              @Param("recipient") String recipient,
                              @Param("startTime") Date startTime,
                              @Param("endTime") Date endTime);
    
    // 统计月度出库次数
    Integer countMonthlyOutboundRecords(@Param("chemicalId") Integer chemicalId,
                                      @Param("chemicalName") String chemicalName,
                                      @Param("startTime") Date startTime,
                                      @Param("endTime") Date endTime);
    // 统计当日出库次数
    Integer countDailyOutboundRecords(@Param("chemicalId") Integer chemicalId,
                                    @Param("chemicalName") String chemicalName);
    
    // 获取月度出库次数
    Integer getMonthlyOutboundTimes(@Param("chemicalId") Integer chemicalId,
                                   @Param("chemicalName") String chemicalName);
    
    // 获取当日出库次数
    Integer getDailyOutboundTimes(@Param("chemicalId") Integer chemicalId,
                                 @Param("chemicalName") String chemicalName);
} 