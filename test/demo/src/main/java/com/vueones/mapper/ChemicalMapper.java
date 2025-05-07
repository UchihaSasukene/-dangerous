package com.vueones.mapper;
import com.vueones.entity.Chemical;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChemicalMapper {
    /**
     * 查询 危化品 数据列表
     * @param name 名称（可选）
     * @param category 类别（可选）
     * @param page 页码
     * @param size 每页大小
     * @return List<Chemical>
     */
    List<Chemical> listChemical(@Param("name") String name, 
                               @Param("category") String category, 
                               @Param("page") Integer page, 
                               @Param("size") Integer size);

    /**
     * 根据 id 查询一条 危化品 数据
     * @param id
     * @return Chemical
     */
    Chemical selectChemicalById(Integer id);

    /**
     * 新增一条 危化品 数据
     * @param chemical
     */
    int addChemical(Chemical chemical);

    /**
     * 删除一条 危化品 数据
     * @param id
     */
    int removeChemical(Integer id);

    /**
     * 更新一条 危化品 数据
     * @param chemical
     */
    int editChemical(Chemical chemical);
    
    /**
     * 统计 危化品 数据总数
     * @param params 查询参数
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
