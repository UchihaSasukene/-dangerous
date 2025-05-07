package com.vueones.mapper;

import com.vueones.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface InventoryMapper {
    /**
     * 获取库存列表
     * @param chemicalId 化学品ID（可选）
     * @param chemicalName 化学品名称（可选）
     * @param location 存储位置（可选）
     * @param status 库存状态（可选）
     * @return 库存列表
     */
    List<Inventory> getInventoryList(@Param("chemicalId") Integer chemicalId,
                                    @Param("chemicalName") String chemicalName,
                                    @Param("location") String location,
                                    @Param("status") String status);
    
    /**
     * 根据ID获取库存
     * @param id 库存ID
     * @return 库存信息
     */
    Inventory getInventoryById(@Param("id") Integer id);
    
    /**
     * 添加库存
     * @param inventory 库存信息
     * @return 影响行数
     */
    int addInventory(Inventory inventory);
    
    /**
     * 添加库存（别名方法，与addInventory功能相同）
     * @param inventory 库存信息
     * @return 影响行数
     */
    int insert(Inventory inventory);
    
    /**
     * 更新库存
     * @param inventory 库存信息
     * @return 影响行数
     */
    int updateInventory(Inventory inventory);
    
    /**
     * 删除库存
     * @param id 库存ID
     * @return 影响行数
     */
    int deleteInventory(@Param("id") Integer id);
    
    /**
     * 更新库存数量
     * @param id 库存ID
     * @param amount 数量
     * @return 影响行数
     */
    int updateInventoryAmount(@Param("id") Integer id, @Param("amount") Double amount);
    
    /**
     * 获取低于预警阈值的库存
     * @return 低于预警阈值的库存列表
     */
    List<Inventory> getBelowThresholdInventory();
    
    /**
     * 批量更新库存
     * @param inventories 库存列表
     * @return 影响行数
     */
    int batchUpdateInventory(List<Inventory> inventories);
    
    /**
     * 获取化学品库存列表
     * @param chemicalId 化学品id
     * @return 化学品库存列表
     */
    List<Inventory> getInventoryList(Integer chemicalId);

    /**
     * 获取化学品总库存量
     * @param chemicalId 化学品ID
     * @return 总库存量
     */
    Double getTotalAmount(@Param("chemicalId") Integer chemicalId);
    
    /**
     * 入库操作
     * @param chemicalId 化学品ID
     * @param amount 数量
     * @return 影响行数
     */
    int processStorageIn(@Param("chemicalId") Integer chemicalId, @Param("amount") Double amount);
    
    /**
     * 出库操作
     * @param chemicalId 化学品ID
     * @param amount 数量
     * @return 影响行数
     */
    int processStorageOut(@Param("chemicalId") Integer chemicalId, @Param("amount") Double amount);

    /**
     * 获取库存盘点功能
     * */
    List<Inventory> getInventoryCheck();
} 