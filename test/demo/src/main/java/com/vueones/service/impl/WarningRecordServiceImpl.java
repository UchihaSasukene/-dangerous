package com.vueones.service.impl;

import com.vueones.entity.WarningRecord;
import com.vueones.entity.Inventory;
import com.vueones.entity.Chemical;
import com.vueones.mapper.WarningRecordMapper;
import com.vueones.service.IWarningRecordService;
import com.vueones.service.IInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Service
public class WarningRecordServiceImpl implements IWarningRecordService {
    
    @Autowired
    private WarningRecordMapper warningRecordMapper;
    
    @Autowired
    private IInventoryService inventoryService;
    /**
     * 添加预警记录
     * @param record 预警记录
     * @return 是否成功
     */
    @Override
    @Transactional
    public int addWarningRecord(WarningRecord record) {
        if (record.getWarningTime() == null) {
            record.setWarningTime(new Date());
        }
        if (record.getStatus() == null) {
            record.setStatus("未处理");
        }
        return warningRecordMapper.insert(record);
    }
    /**
     * 更新预警记录
     * @param record 预警记录
     * @return 是否成功
     */
    @Override
    @Transactional
    public int updateWarningRecord(WarningRecord record) {
        return warningRecordMapper.update(record);
    }
    /**
     * 删除预警记录
     * @param id 预警记录id
     * @return 是否成功
     */
    @Override
    @Transactional
    public int deleteWarningRecord(Integer id) {
        return warningRecordMapper.deleteById(id);
    }
    /**
     * 根据id查询预警记录
     * @param id 预警记录id
     * @return 预警记录
     */
    @Override
    public WarningRecord getWarningRecordById(Integer id) {
        return warningRecordMapper.selectById(id);
    }
    /**
     * 根据化学品id、预警类型、预警等级、预警状态、开始时间、结束时间查询预警记录（带分页）
     * @param chemicalId 化学品id
     * @param chemicalName 化学品名称
     * @param warningType 预警类型
     * @param warningLevel 预警等级
     * @param status 预警状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 预警记录列表
     */
    @Override
    public List<WarningRecord> getWarningRecordList(Integer chemicalId, String chemicalName, String warningType, String warningLevel, String status, Date startTime, Date endTime, Integer offset, Integer limit) {
        try {
            return warningRecordMapper.selectList(chemicalId, chemicalName, warningType, warningLevel, status, startTime, endTime, offset, limit);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // /**
    //  * 兼容旧方法（无分页参数），用于现有代码调用
    //  */
    // @Override
    // public List<WarningRecord> getWarningRecordList(Integer chemicalId, String chemicalName, String warningType, String warningLevel, String status, Date startTime, Date endTime) {
    //     return getWarningRecordList(chemicalId, chemicalName, warningType, warningLevel, status, startTime, endTime, null, null);
    // }
    
    /**
     * 统计预警记录总数
     * @param chemicalId 化学品id
     * @param chemicalName 化学品名称
     * @param warningType 预警类型
     * @param warningLevel 预警等级
     * @param status 预警状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 符合条件的记录总数
     */
    @Override
    public int countWarningRecords(Integer chemicalId, String chemicalName, String warningType, String warningLevel, String status, Date startTime, Date endTime) {
        try {
            return warningRecordMapper.countWarningRecords(chemicalId, chemicalName, warningType, warningLevel, status, startTime, endTime);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * 更新预警状态
     * @param id 预警记录id
     * @param status 预警状态
     * @param handler 处理人
     * @param handleResult 处理结果
     * @return 是否成功
     */
    @Override
    @Transactional
    public int updateWarningStatus(Integer id, String status, String handler, String handleResult) {
        return warningRecordMapper.updateStatus(id, status, handler, handleResult);
    }
    /**
     * 获取未处理预警数量
     * @return 未处理预警数量
     */
    @Override
    public int getUnhandledWarningCount() {
        return warningRecordMapper.countUnhandledWarnings();
    }
    /**
     * 获取未处理预警
     * @return 未处理预警列表
     */
    @Override
    public List<WarningRecord> getUnhandledWarnings() {
        return warningRecordMapper.selectUnhandledWarnings();
    }
    /**
     * 检查并生成预警
     */
    @Override
    @Transactional
    public void checkAndGenerateWarnings() {
        // 获取所有低于阈值的库存
        List<Inventory> belowThresholdInventories = inventoryService.getBelowThresholdInventory();
        
        for (Inventory inventory : belowThresholdInventories) {
            // 检查是否已经存在未处理的预警
            List<WarningRecord> existingWarnings = getWarningRecordList(
                inventory.getChemicalId(), inventory.getChemical().getName(), "库存不足", null, "未处理", null, null, null, null);
            
            if (existingWarnings.isEmpty()) {
                // 创建新的预警记录
                WarningRecord warning = new WarningRecord();
                warning.setChemicalId(inventory.getChemicalId());
                // 注意：WarningRecord可能没有直接设置chemicalName的字段
                // 如果需要设置chemicalName，需要通过chemical对象设置
                Chemical chemical = new Chemical();
                chemical.setId(inventory.getChemicalId());
                chemical.setName(inventory.getChemical().getName());
                warning.setChemical(chemical);
                
                warning.setWarningType("库存不足");
                warning.setWarningLevel("严重");
                warning.setWarningContent(inventory.getChemical().getName() + 
                                      "的库存量低于预警阈值，当前库存量：" + 
                                      inventory.getCurrentAmount() + inventory.getUnit() + 
                                      "，预警阈值：" + inventory.getChemical().getWarningThreshold() + inventory.getUnit());
                warning.setStatus("未处理");
                warning.setWarningTime(new Date());
                addWarningRecord(warning);
            }
        }
    }
    /**
     * 处理预警
     * @param id 预警记录id
     * @param handler 处理人
     * @param handleResult 处理结果
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean handleWarning(Integer id, String handler, String handleResult) {
        WarningRecord warning = getWarningRecordById(id);
        if (warning == null || !"未处理".equals(warning.getStatus())) {
            return false;
        }
        
        warning.setStatus("已处理");
        warning.setHandler(handler);
        warning.setHandleResult(handleResult);
        warning.setHandleTime(new Date());
        
        int result = updateWarningRecord(warning);
        return result > 0;
    }
    
    /**
     * 批量处理预警
     * @param ids 预警记录ID列表
     * @param status 预警状态
     * @param handler 处理人
     * @param handleResult 处理结果
     * @return 处理成功的记录数
     */
    @Override
    @Transactional
    public int batchHandleWarnings(List<Integer> ids, String status, String handler, String handleResult) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        
        int successCount = 0;
        for (Integer id : ids) {
            int result = updateWarningStatus(id, status, handler, handleResult);
            if (result > 0) {
                successCount++;
            }
        }
        return successCount;
    }
    
    /**
     * 获取预警统计信息
     * @return 返回包含不同类型预警数量的Map
     */
    @Override
    public Map<String, Integer> getWarningStatistics() {
        Map<String, Integer> statistics = new HashMap<>();
        
        // 获取未处理预警数量
        int unhandledCount = getUnhandledWarningCount();
        statistics.put("unhandled", unhandledCount);
        
        // 获取各预警级别的数量
        List<Map<String, Object>> levelStats = warningRecordMapper.countByWarningLevel();
        if (levelStats != null) {
            for (Map<String, Object> stat : levelStats) {
                String level = (String) stat.get("warning_level");
                Integer count = ((Number) stat.get("count")).intValue();
                statistics.put("level_" + level, count);
            }
        }
        
        // 获取各类型预警数量
        int stockWarnings = countWarningRecords(null, null, "库存不足", null, null, null, null);
        statistics.put("stockWarning", stockWarnings);
        
        int qualityWarnings = countWarningRecords(null, null, "质量异常", null, null, null, null);
        statistics.put("qualityWarning", qualityWarnings);
        
        int storageWarnings = countWarningRecords(null, null, "存储条件异常", null, null, null, null);
        statistics.put("storageWarning", storageWarnings);
        
        return statistics;
    }
} 