package com.vueones.service.impl;

import com.vueones.entity.OutboundRecord;
import com.vueones.mapper.OutboundRecordMapper;
import com.vueones.service.IOutboundRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Date;
@Service
public class OutboundRecordServiceImpl implements IOutboundRecordService {
    
    @Autowired
    private OutboundRecordMapper outboundRecordMapper;
    /**
     * 添加出库记录
     * @param record 出库记录
     * @return 是否成功
     */
    @Override
    @Transactional
    public int addOutboundRecord(OutboundRecord record) {
        if (record.getCreateTime() == null) {
            record.setCreateTime(new Date());
        }
        return outboundRecordMapper.insert(record);
    }
    /**
     * 更新出库记录
     * @param record 出库记录
     * @return 是否成功
     */
    @Override
    @Transactional
    public int updateOutboundRecord(OutboundRecord record) {
        return outboundRecordMapper.update(record);
    }
    /**
     * 删除出库记录
     * @param id 出库记录id
     * @return 是否成功
     */
    @Override
    @Transactional
    public int deleteOutboundRecord(Integer id) {
        return outboundRecordMapper.deleteById(id);
    }
    /**
     * 根据id查询出库记录
     * @param id 出库记录id
     * @return 出库记录
     */
    @Override
    public OutboundRecord getOutboundRecordById(Integer id) {
        return outboundRecordMapper.selectById(id);
    }
    /**
     * 查询出库记录列表
     * @param chemicalId 化学品id
     * @param chemicalName 化学品名称
     * @param recipient 领用人
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param offset 偏移量
     * @param size 分页大小
     * @return 出库记录列表
     */
    @Override
    public List<OutboundRecord> getOutboundRecordList(Integer chemicalId, String chemicalName, String recipient, Date startTime, Date endTime, Integer offset, Integer size) {
        // 直接使用mapper提供的分页查询
        return outboundRecordMapper.selectList(chemicalId, chemicalName, startTime, endTime, recipient, offset, size);
    }
    
    /**
     * 统计出库记录数量
     * @param chemicalId 化学品id
     * @param chemicalName 化学品名称
     * @param recipient 领用人
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 记录数量
     */
    @Override
    public int countOutboundRecords(Integer chemicalId, String chemicalName, String recipient, Date startTime, Date endTime) {
        // 使用Mapper提供的计数方法
        Integer count = outboundRecordMapper.countOutboundRecords(chemicalId, chemicalName, recipient, startTime, endTime);
        return count != null ? count : 0;
    }
    
    /**
     * 统计出库总量
     * @param chemicalId 化学品id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 出库总量
     */
    @Override
    public Double sumAmountByChemicalId(Integer chemicalId, Date startTime, Date endTime) {
        return outboundRecordMapper.sumAmountByChemicalId(chemicalId, startTime, endTime);
    }
    
    /**
     * 批量添加出库记录
     * @param records 出库记录列表
     * @return 是否成功
     */
    @Override
    @Transactional
    public int batchAddOutboundRecords(List<OutboundRecord> records) {
        if (records == null || records.isEmpty()) {
            return 0;
        }
        // 设置创建时间
        Date now = new Date();
        for (OutboundRecord record : records) {
            if (record.getCreateTime() == null) {
                record.setCreateTime(now);
            }
        }
        return outboundRecordMapper.batchInsert(records);
    }
    
    /**
     * 获取月度出库记录数量
     * @return 月度出库记录数量
     */
    @Override
    public int getMonthlyOutboundCount(Integer chemicalId, String chemicalName) {
        // 直接调用mapper的getMonthlyOutboundCount方法
        return outboundRecordMapper.getMonthlyOutboundCount(chemicalId, chemicalName);
    }
    
    /**
     * 获取当日出库记录数量
     * @return 当日出库记录数量
     */
    public int getDailyOutboundCount(Integer chemicalId, String chemicalName) {
        // 直接调用mapper的getDailyOutboundCount方法
        return outboundRecordMapper.getDailyOutboundCount(chemicalId, chemicalName);
    }
    /**
     * 获取当日出库记录次数
     * @return 当日出库记录次数
     */
    @Override
    public int getDailyOutboundTimes(Integer chemicalId, String chemicalName) {
        Integer result = outboundRecordMapper.getDailyOutboundTimes(chemicalId, chemicalName);
        return result != null ? result : 0;
    }

    /**
     * 获取月度出库记录次数
     * @return 月度出库记录次数
     */
    @Override
    public int getMonthlyOutboundTimes(Integer chemicalId, String chemicalName) {
        Integer result = outboundRecordMapper.getMonthlyOutboundTimes(chemicalId, chemicalName);
        return result != null ? result : 0;
    }
} 