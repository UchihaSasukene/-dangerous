package com.vueones.service;

import com.vueones.entity.WarningRecord;
import java.util.List;
import java.util.Date;
import java.util.Map;

public interface IWarningRecordService {
    /**
     * 添加预警记录
     * @param record 预警记录
     * @return 是否成功
     */
    int addWarningRecord(WarningRecord record);
    int updateWarningRecord(WarningRecord record);
    int deleteWarningRecord(Integer id);
    WarningRecord getWarningRecordById(Integer id);
    
    /**
     * 获取预警记录列表（带分页）
     * @param chemicalId 化学品ID
     * @param chemicalName 化学品名称
     * @param warningType 预警类型
     * @param warningLevel 预警级别
     * @param status 预警状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 预警记录列表
     */
    List<WarningRecord> getWarningRecordList(Integer chemicalId, String chemicalName, String warningType, String warningLevel, String status, Date startTime, Date endTime, Integer offset, Integer limit);
    
    /**
     * 统计预警记录总数
     * @param chemicalId 化学品ID
     * @param chemicalName 化学品名称
     * @param warningType 预警类型
     * @param warningLevel 预警级别
     * @param status 预警状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 符合条件的记录总数
     */
    int countWarningRecords(Integer chemicalId, String chemicalName, String warningType, String warningLevel, String status, Date startTime, Date endTime);
    
    /**
     * 更新预警状态
     * @param id 预警记录ID
     * @param status 预警状态
     * @param handler 处理人
     * @param handleResult 处理结果
     */
    int updateWarningStatus(Integer id, String status, String handler, String handleResult);
    
    /**
     * 获取未处理预警数量
     * @return 未处理预警数量
     */
    int getUnhandledWarningCount();
    
    /**
     * 获取未处理预警列表
     * @return 未处理预警列表
     */
    List<WarningRecord> getUnhandledWarnings();
    
    /**
     * 获取预警统计信息
     * @return 返回包含不同类型预警数量的Map
     */
    Map<String, Integer> getWarningStatistics();
    
    /**
     * 预警生成
     */
    void checkAndGenerateWarnings();
    
    /**
     * 预警处理
     * @param id 预警记录ID
     * @param handler 处理人
     * @param handleResult 处理结果
     */
    boolean handleWarning(Integer id, String handler, String handleResult);
    
    /**
     * 批量处理预警
     * @param ids 预警记录ID列表
     * @param status 预警状态
     * @param handler 处理人
     * @param handleResult 处理结果
     * @return 处理成功的记录数
     */
    int batchHandleWarnings(List<Integer> ids, String status, String handler, String handleResult);
} 