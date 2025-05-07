package com.vueones.service;

import com.vueones.entity.Chemical;
import java.util.List;
import java.util.Map;

public interface IChemicalService {
    /**
     * 查询全部危化品数据（无分页）
     * @return List<Chemical>
     */
    List<Chemical> listChemical();

    /**
     * 分页查询危化品数据
     * @param name 名称（可选）
     * @param category 类别（可选）
     * @param page 页码
     * @param size 每页大小
     * @return List<Chemical>
     */
    List<Chemical> listChemical(String name, String category, Integer page, Integer size);

    /**
     * 根据 id 查询一条 Chemical 数据
     * @param id
     * @return Chemical
     */
    Chemical selectChemicalById(Integer id);

    /**
     * 新增一条 Chemical 数据
     * @param chemical
     */
    int addChemical(Chemical chemical);

    /**
     * 根据 id 删除一条 Chemical 数据
     * @param id
     */
    int removeChemical(Integer id);

    /**
     * 更新一条 Chemical 数据
     * @param chemical
     */
    int editChemical(Chemical chemical);

    /**
     * 统计 Chemical 数据总数
     * @param name 名称（可选）
     * @param category 类别（可选）
     * @return int
     */
    int countChemical(Map<String, Object> params);

    /**
     * 根据危险等级统计数量
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> countByDangerLevel();

    /**
     * 根据存储条件统计数量
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> countByStorageCondition();

    /**
     * 查询低于预警阈值的化学品
     * @return List<Chemical>
     */
    List<Chemical> listBelowWarningThreshold();
    
}
