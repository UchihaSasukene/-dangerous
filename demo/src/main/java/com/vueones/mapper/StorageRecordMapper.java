package com.vueones.mapper;

import com.vueones.entity.StorageRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Date;

@Mapper
public interface StorageRecordMapper {
    // 基本CRUD操作
    int insert(StorageRecord record);
    int update(StorageRecord record);
    int deleteById(Integer id);
    StorageRecord selectById(Integer id);
    
    // 自定义查询方法
    List<StorageRecord> selectList(@Param("chemicalId") Integer chemicalId,
                                 @Param("chemicalName") String chemicalName,
                                 @Param("startTime") Date startTime,
                                 @Param("endTime") Date endTime,
                                 @Param("supplier") String supplier,
                                 @Param("offset") Integer offset,
                                 @Param("size") Integer size);
    
    // 批量操作
    int batchInsert(List<StorageRecord> records);
    
    // 统计查询
    Double sumAmountByChemicalId(@Param("chemicalId") Integer chemicalId,
                                @Param("chemicalName") String chemicalName,
                                @Param("startTime") Date startTime,
                                @Param("endTime") Date endTime);    

    // 获取月度入库记录数量
    Integer getMonthlyStorageCount(@Param("chemicalId") Integer chemicalId,
                                  @Param("chemicalName") String chemicalName);

    // 获取当日入库记录数量
    Integer getDailyStorageCount(@Param("chemicalId") Integer chemicalId,
                                @Param("chemicalName") String chemicalName);

    // 统计符合条件的入库记录数量
    Integer countStorageRecords(@Param("chemicalId") Integer chemicalId,
                               @Param("chemicalName") String chemicalName,
                               @Param("supplier") String supplier,
                               @Param("startTime") Date startTime,
                               @Param("endTime") Date endTime);

    //统计当日入库次数
    Integer countDailyStorageRecords(@Param("chemicalId") Integer chemicalId,
                                    @Param("chemicalName") String chemicalName);

    //统计月度入库次数
    Integer countMonthlyStorageRecords(@Param("chemicalId") Integer chemicalId,
                                     @Param("chemicalName") String chemicalName,
                                     @Param("startTime") Date startTime,
                                     @Param("endTime") Date endTime);

    // 获取月度入库次数
    Integer getMonthlyStorageTimes(@Param("chemicalId") Integer chemicalId,
                                   @Param("chemicalName") String chemicalName);

    // 获取当日入库次数
    Integer getDailyStorageTimes(@Param("chemicalId") Integer chemicalId,
                                @Param("chemicalName") String chemicalName);
} 