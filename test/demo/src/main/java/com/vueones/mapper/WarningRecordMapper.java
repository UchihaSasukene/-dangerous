package com.vueones.mapper;

import com.vueones.entity.WarningRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Date;
import java.util.Map;

@Mapper
public interface WarningRecordMapper {
    // 基本CRUD操作
    int insert(WarningRecord record);
    int update(WarningRecord record);
    int deleteById(Integer id);
    WarningRecord selectById(Integer id);
    
    // 自定义查询方法（带分页）
    List<WarningRecord> selectList(@Param("chemicalId") Integer chemicalId,
                                 @Param("chemicalName") String chemicalName,
                                 @Param("warningType") String warningType,
                                 @Param("warningLevel") String warningLevel,
                                 @Param("status") String status,
                                 @Param("startTime") Date startTime,
                                 @Param("endTime") Date endTime,
                                 @Param("offset") Integer offset,
                                 @Param("limit") Integer limit);
    
    // 统计总数
    int countWarningRecords(@Param("chemicalId") Integer chemicalId,
                           @Param("chemicalName") String chemicalName,
                           @Param("warningType") String warningType,
                           @Param("warningLevel") String warningLevel,
                           @Param("status") String status,
                           @Param("startTime") Date startTime,
                           @Param("endTime") Date endTime);
    
    // 预警相关特殊方法
    int updateStatus(@Param("id") Integer id,
                    @Param("status") String status,
                    @Param("handler") String handler,
                    @Param("handleResult") String handleResult);
    
    // 统计查询
    int countUnhandledWarnings();
    List<WarningRecord> selectUnhandledWarnings();
    
    // 统计各预警级别的数量
    List<Map<String, Object>> countByWarningLevel();
} 