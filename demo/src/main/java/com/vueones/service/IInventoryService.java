package com.vueones.service;

import com.vueones.entity.Inventory;
import java.util.List;

public interface IInventoryService {
    // 基本操作
    int addInventory(Inventory inventory);
    int updateInventory(Inventory inventory);
    int deleteInventory(Integer id);
    Inventory getInventoryById(Integer id);
    
    /**
     * 获取库存列表
     * @param chemicalId 化学品ID
     * @param chemicalName 化学品名称
     * @param location 位置
     * @param status 库存状态（normal:正常,low:不足,high:超储）
     * @return 库存列表
     */
    List<Inventory> getInventoryList(Integer chemicalId, String chemicalName, String location, String status);
    
    /**
     * 更新库存数量
     * @param id 库存ID
     * @param amount 变动数量
     * @return 是否成功
     */
    int updateInventoryAmount(Integer id, Double amount);
    List<Inventory> getBelowThresholdInventory();
    
    /**
     * 批量更新库存数量
     * @param inventories 库存列表
     * @return 是否成功
     */
    int batchUpdateInventory(List<Inventory> inventories);
    
    /**
     * 处理入库操作
     * @param chemicalId 化学品ID
     * @param amount 入库数量
     * @return 是否成功
     */
    boolean processStorageIn(Integer chemicalId, Double amount);
    /**
     * 处理出库操作
     * @param chemicalId 化学品ID
     * @param amount 出库数量
     * @return 是否成功
     */
    boolean processStorageOut(Integer chemicalId, Double amount);
    /**
     * 获取化学品总库存量
     * @param chemicalId 化学品id
     * @return 化学品总库存量
     */
    Double getTotalAmount(Integer chemicalId);
    /**
     * 获取化学品库存列表
     * @param chemicalId 化学品id
     * @return 化学品库存列表
     */
    List<Inventory> getInventoryList(Integer chemicalId);

    /**
     * 获取库存盘点功能
     * @return 库存盘点列表
     */
    List<Inventory> getInventoryCheck();
} 