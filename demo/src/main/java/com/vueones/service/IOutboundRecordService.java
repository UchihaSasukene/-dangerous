package com.vueones.service;

import com.vueones.entity.OutboundRecord;
import java.util.List;
import java.util.Date;

/**
 * 出库记录服务接口
 */
public interface IOutboundRecordService {
    /**
     * 添加出库记录
     * @param record 出库记录
     * @return 是否成功
     */
    int addOutboundRecord(OutboundRecord record);
    /**
     * 更新出库记录
     * @param record 出库记录
     * @return 是否成功
     */
    int updateOutboundRecord(OutboundRecord record);
    /**
     * 删除出库记录
     * @param id 出库记录id
     * @return 是否成功
     */
    int deleteOutboundRecord(Integer id);
    /**
     * 根据id查询出库记录
     * @param id 出库记录id
     * @return 出库记录
     */
    OutboundRecord getOutboundRecordById(Integer id);
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
    List<OutboundRecord> getOutboundRecordList(Integer chemicalId, String chemicalName, String recipient, Date startTime, Date endTime, Integer offset, Integer size);
    /**
     * 统计出库记录数量
     * @param chemicalId 化学品id
     * @param chemicalName 化学品名称
     * @param recipient 领用人
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 记录数量
     */
    int countOutboundRecords(Integer chemicalId, String chemicalName, String recipient, Date startTime, Date endTime);
    /**
     * 统计出库总量
     * @param chemicalId 化学品id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 出库总量
     */
    Double sumAmountByChemicalId(Integer chemicalId, Date startTime, Date endTime);
    /**
     * 批量添加出库记录
     * @param records 出库记录列表
     * @return 是否成功
     */
    int batchAddOutboundRecords(List<OutboundRecord> records);
    /**
     * 获取月度出库记录总数量
     * @return 月度出库记录数量
     */
    int getMonthlyOutboundCount(Integer chemicalId, String chemicalName);
    /**
     * 获取月度出库次数
     * @return
     */
    int getMonthlyOutboundTimes(Integer chemicalId, String chemicalName);
    /**
     * 获取当日出库记录总数量
     * @return 当日出库记录数量
     */
    int getDailyOutboundCount(Integer chemicalId, String chemicalName);
    /**
     * 获取当日出库总次数
     * @return 出库总次数
     */
    int getDailyOutboundTimes(Integer chemicalId, String chemicalName);
} 