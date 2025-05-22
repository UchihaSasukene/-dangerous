package com.vueones.service.impl;

import com.vueones.entity.Inventory;
import com.vueones.mapper.InventoryMapper;
import com.vueones.service.IInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Service
public class InventoryServiceImpl implements IInventoryService {
    
    @Autowired
    private InventoryMapper inventoryMapper;
    

    /**
     * 添加库存
     * @param inventory 库存信息
     * @return 是否成功
     */
    @Override
    @Transactional
    public int addInventory(Inventory inventory) {
        if (inventory.getCreateTime() == null) {
            inventory.setCreateTime(new Date());
        }
        inventory.setUpdateTime(new Date());
        return inventoryMapper.addInventory(inventory);
    }
    /**
     * 更新库存
     * @param inventory 库存信息
     * @return 是否成功
     */
    @Override
    @Transactional
    public int updateInventory(Inventory inventory) {
        inventory.setUpdateTime(new Date());
        return inventoryMapper.updateInventory(inventory);
    }
    /**
     * 删除库存
     * @param id 库存id
     * @return 是否成功
     */
    @Override
    @Transactional
    public int deleteInventory(Integer id) {
        return inventoryMapper.deleteInventory(id);
    }
    /**
     * 根据id查询库存
     * @param id 库存id
     * @return 库存信息
     */
    @Override
    public Inventory getInventoryById(Integer id) {
        return inventoryMapper.getInventoryById(id);
    }
    /**
     * 根据化学品id和存储位置查询库存
     * @param chemicalId 化学品id
     * @param chemicalName 化学品名称
     * @param location 存储位置
     * @param status 库存状态（normal:正常,low:不足,high:超储）
     * @return 库存信息列表
     */
    @Override
    public List<Inventory> getInventoryList(Integer chemicalId, String chemicalName, String location, String status) {
        // 先获取基础库存列表
        List<Inventory> inventories = inventoryMapper.getInventoryList(chemicalId, chemicalName, location,status);
        
        // 如果状态为空，返回所有库存
        if (status == null || status.isEmpty()) {
            return inventories;
        }
        
        // 根据状态筛选
        List<Inventory> filteredInventories = new ArrayList<>();
        for (Inventory inventory : inventories) {
            if (inventory.getChemical() == null) {
                continue;
            }
            
            Double warningThreshold = inventory.getChemical().getWarningThreshold();
            Double currentAmount = inventory.getCurrentAmount();
            
            // 根据状态筛选
            switch (status) {
                case "normal": // 正常
                    if (currentAmount >= warningThreshold * 1.2) {
                        filteredInventories.add(inventory);
                    }
                    break;
                case "low": // 不足
                    if (currentAmount < warningThreshold) {
                        filteredInventories.add(inventory);
                    }
                    break;
                case "high": // 超储（暂定为超过预警阈值的5倍为超储）
                    if (currentAmount > warningThreshold * 5) {
                        filteredInventories.add(inventory);
                    }
                    break;
                default:
                    filteredInventories.add(inventory);
                    break;
            }
        }
        
        return filteredInventories;
    }
    /**
     * 更新库存数量
     * @param id 库存id
     * @param amount 库存数量
     * @return 是否成功
     */
    @Override
    @Transactional
    public int updateInventoryAmount(Integer id, Double amount) {
        return inventoryMapper.updateInventoryAmount(id, amount);
    }
    /**
     * 获取低于预警阈值的库存
     * @return 低于预警阈值的库存列表
     */
    @Override
    public List<Inventory> getBelowThresholdInventory() {
        try {
            return inventoryMapper.getBelowThresholdInventory();
        } catch (Exception e) {
            System.err.println("获取低于预警阈值的库存记录失败: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    /**
     * 批量更新库存
     * @param inventories 库存信息列表
     * @return 是否成功
     */
    @Override
    @Transactional
    public int batchUpdateInventory(List<Inventory> inventories) {
        Date now = new Date();
        inventories.forEach(inventory -> {
            inventory.setUpdateTime(now);
        });
        return inventoryMapper.batchUpdateInventory(inventories);
    }
    /**
     * 获取化学品总库存量
     * @param chemicalId 化学品id
     * @return 化学品总库存量
     */
    @Override
    public Double getTotalAmount(Integer chemicalId) {
        return inventoryMapper.getTotalAmount(chemicalId);
    }
    /**
     * 获取化学品库存列表
     * @param chemicalId 化学品id
     * @return 化学品库存列表
     */
    @Override
    public List<Inventory> getInventoryList(Integer chemicalId) {
        return getInventoryList(chemicalId, null, null, null);
    }

    /**
     * 入库操作
     * @param chemicalId 化学品id
     * @param amount 入库数量
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean processStorageIn(Integer chemicalId, Double amount) {
        Inventory inventory = inventoryMapper.getInventoryList(chemicalId, null, null,null).stream()
                .findFirst()
                .orElse(null);
                
        if (inventory == null) {
            // 如果不存在库存记录，创建新记录
            inventory = new Inventory();
            inventory.setChemicalId(chemicalId);
            inventory.setCurrentAmount(amount);
            inventory.setCreateTime(new Date());
            inventory.setUpdateTime(new Date());
            return inventoryMapper.addInventory(inventory) > 0;
        } else {
            // 更新现有库存
            Double newAmount = inventory.getCurrentAmount() + amount;
            inventory.setCurrentAmount(newAmount);
            inventory.setUpdateTime(new Date());
            return inventoryMapper.updateInventory(inventory) > 0;
        }
    }
    /**
     * 出库操作
     * @param chemicalId 化学品id
     * @param amount 出库数量
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean processStorageOut(Integer chemicalId, Double amount) {
        Inventory inventory = inventoryMapper.getInventoryList(chemicalId, null, null,null).stream()
                .findFirst()
                .orElse(null);
                
        if (inventory == null || inventory.getCurrentAmount() < amount) {
            // 库存不存在或库存不足
            return false;
        }
        
        // 更新库存
        Double newAmount = inventory.getCurrentAmount() - amount;
        inventory.setCurrentAmount(newAmount);
        inventory.setUpdateTime(new Date());
        return inventoryMapper.updateInventory(inventory) > 0;
    }

    /**
     * 获取库存盘点功能
     * */
    @Override
    public List<Inventory> getInventoryCheck() {
        return inventoryMapper.getInventoryCheck();
    }
}