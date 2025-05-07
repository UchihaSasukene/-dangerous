package com.vueones.controller;

import com.vueones.entity.WarningRecord;
import com.vueones.service.IWarningRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * 预警记录控制器
 * 特别说明：
 * 1. 支持自动检查和生成库存预警
 * 2. 预警状态包括：未处理、处理中、已处理
 * 3. 预警等级包括：一般、严重、紧急
 * 4. 提供未处理预警的快速查询
 * 5. 预警处理时会记录处理人和处理结果
 */
@RestController
@RequestMapping("/warning")
@CrossOrigin(origins = "*")
public class WarningRecordController {
    
    @Autowired
    private IWarningRecordService warningRecordService;
    
    /**
     * 添加预警记录
     * @param record
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<?> addWarningRecord(@RequestBody WarningRecord record) {
        int result = warningRecordService.addWarningRecord(record);
        if (result > 0) {
            return ResponseEntity.ok(record);
        }
        return ResponseEntity.badRequest().body("添加预警记录失败");
    }
    
    /**
     * 更新预警记录
     * @param id
     * @param record
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateWarningRecord(@PathVariable Integer id, @RequestBody WarningRecord record) {
        record.setId(id);
        int result = warningRecordService.updateWarningRecord(record);
        if (result > 0) {
            return ResponseEntity.ok(record);
        }
        return ResponseEntity.badRequest().body("更新预警记录失败");
    }
    /**
     * 删除预警记录
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWarningRecord(@PathVariable Integer id) {
        int result = warningRecordService.deleteWarningRecord(id);
        if (result > 0) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("删除预警记录失败");
    }
    /**
     * 根据id查询预警记录
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<WarningRecord> getWarningRecord(@PathVariable Integer id) {
        WarningRecord record = warningRecordService.getWarningRecordById(id);
        if (record != null) {
            return ResponseEntity.ok(record);
        }
        return ResponseEntity.notFound().build();
    }
    /**
     * 根据化学品id、预警类型、预警等级、预警状态、开始时间、结束时间查询预警记录
     * @param chemicalId 化学品id
     * @param chemicalName 化学品名称
     * @param warningType 预警类型
     * @param warningLevel 预警等级
     * @param status 预警状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 页码
     * @param size 每页数量
     * @return 预警记录列表和总数
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getWarningRecordList(
            @RequestParam(required = false) Integer chemicalId,
            @RequestParam(required = false) String chemicalName,
            @RequestParam(required = false) String warningType,
            @RequestParam(required = false) String warningLevel,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        try {
            // 计算偏移量
            Integer offset = (page - 1) * size;
            
            // 获取记录列表（带分页）
            List<WarningRecord> records = warningRecordService.getWarningRecordList(
                chemicalId, chemicalName, warningType, warningLevel, status, startTime, endTime, offset, size);
            
            // 如果没有预警记录，先检查并生成预警
            if ((records == null || records.isEmpty()) && page == 1) {
                warningRecordService.checkAndGenerateWarnings();
                // 再次查询
                records = warningRecordService.getWarningRecordList(
                    chemicalId, chemicalName, warningType, warningLevel, status, startTime, endTime, offset, size);
            }
            
            // 获取符合条件的总记录数
            int total = warningRecordService.countWarningRecords(
                chemicalId, chemicalName, warningType, warningLevel, status, startTime, endTime);
            
            // 构建分页数据
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("records", records);
            pageData.put("total", total);
            pageData.put("page", page);
            pageData.put("size", size);
            pageData.put("pages", (total + size - 1) / size); // 计算总页数
            
            // 构建响应数据
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "获取预警记录成功");
            response.put("data", pageData);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "获取预警记录失败: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(200).body(response);
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
    @PutMapping("/update/{id}/status")
    public ResponseEntity<?> updateWarningStatus(
            @PathVariable Integer id,
            @RequestParam String status,
            @RequestParam String handler,
            @RequestParam String handleResult) {
        int result = warningRecordService.updateWarningStatus(id, status, handler, handleResult);
        if (result > 0) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("更新预警状态失败");
    }
    /**
     * 获取未处理预警数量
     * @return 未处理预警数量
     */
    @GetMapping("/unhandled/count")
    public ResponseEntity<Map<String, Object>> getUnhandledWarningCount() {
        try {
            int count = warningRecordService.getUnhandledWarningCount();
            
            Map<String, Object> data = new HashMap<>();
            data.put("count", count);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "获取未处理预警数量成功");
            response.put("data", data);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "获取未处理预警数量失败: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(200).body(response);
        }
    }
    /**
     * 获取未处理预警
     * @return 未处理预警列表
     */
    @GetMapping("/unhandled/list")
    public ResponseEntity<Map<String, Object>> getUnhandledWarnings() {
        try {
            List<WarningRecord> warnings = warningRecordService.getUnhandledWarnings();
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "获取未处理预警列表成功");
            response.put("data", warnings);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "获取未处理预警列表失败: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(200).body(response);
        }
    }
    /**
     * 检查并生成预警
     * @return 是否成功
     */
    @PostMapping("/check")
    public ResponseEntity<?> checkAndGenerateWarnings() {
        warningRecordService.checkAndGenerateWarnings();
        return ResponseEntity.ok().build();
    }
    /**
     * 处理预警
     * @param id 预警记录id
     * @param handler 处理人
     * @param handleResult 处理结果
     * @return 是否成功
     */
    @PostMapping("/handle/{id}")
    public ResponseEntity<?> handleWarning(
            @PathVariable Integer id,
            @RequestParam String handler,
            @RequestParam String handleResult) {
        boolean success = warningRecordService.handleWarning(id, handler, handleResult);
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("处理预警失败");
    }
    /**
     * 获取预警统计信息
     * @return 预警统计信息，包括各类型预警数量
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getWarningStatistics() {
        try {
            Map<String, Integer> statistics = warningRecordService.getWarningStatistics();
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "获取预警统计信息成功");
            response.put("data", statistics);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "获取预警统计信息失败: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(200).body(response);
        }
    }
} 