package com.vueones.service;

import com.vueones.entity.StorageRecord;
import java.util.List;
import java.util.Date;

/**
 * 入库记录服务接口
 */
public interface IStorageRecordService {
    /**
     * 添加入库记录
     * @param record 入库记录
     * @return 是否成功
     */
    int addStorageRecord(StorageRecord record);
    /**
     * 更新入库记录
     * @param record 入库记录
     * @return 是否成功
     */
    int updateStorageRecord(StorageRecord record);
    /**
     * 删除入库记录
     * @param id 入库记录id
     * @return 是否成功
     */
    int deleteStorageRecord(Integer id);
    /**
     * 根据id查询入库记录
     * @param id 入库记录id
     * @return 入库记录
     */
    StorageRecord getStorageRecordById(Integer id);
    /**
     * 查询入库记录列表
     * @param chemicalId 化学品id
     * @param chemicalName 化学品名称
     * @param supplier 供应商
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param offset 偏移量
     * @param size 分页大小
     * @return 入库记录列表
     */
    List<StorageRecord> getStorageRecordList(Integer chemicalId, String chemicalName, String supplier, Date startTime, Date endTime, Integer offset, Integer size);
    /**
     * 统计入库记录数量
     * @param chemicalId 化学品id
     * @param chemicalName 化学品名称
     * @param supplier 供应商
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 记录数量
     */
    int countStorageRecords(Integer chemicalId, String chemicalName, String supplier, Date startTime, Date endTime);
    /**
     * 计算入库总量
     * @param chemicalId 化学品id
     * @param chemicalName 化学品名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 入库总量
     */
    Double sumStorageAmount(Integer chemicalId, String chemicalName, Date startTime, Date endTime);
    /**
     * 批量添加入库记录
     * @param records 入库记录列表
     * @return 是否成功
     */
    int batchAddStorageRecords(List<StorageRecord> records);
    /**
     * 获取入库总量
     * @param chemicalId 化学品id
     * @param chemicalName 化学品名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 入库总量
     */
    Double getTotalAmountByChemicalId(Integer chemicalId, String chemicalName, Date startTime, Date endTime);
    /**
     * 获取月度入库数量
     * @return 月度入库数量
     */
    int getMonthlyStorageCount(Integer chemicalId, String chemicalName);
    /**
     * 获取当日入库数量
     * @return 当日入库数量
     */
    int getDailyStorageCount(Integer chemicalId, String chemicalName);
    /**
     * 获取月度入库次数
     * @return 月度入库次数
     */
    int getMonthlyStorageTimes(Integer chemicalId, String chemicalName);
    /**
     * 获取当日入库次数
     * @param chemicalId
     * @param chemicalName
     * @return 当日入库次数
     */
    int getDailyStorageTimes(Integer chemicalId, String chemicalName);
} 