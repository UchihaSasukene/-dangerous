package com.vueones.service.impl;

import com.vueones.entity.Chemical;
import com.vueones.mapper.ChemicalMapper;
import com.vueones.service.IChemicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ChemicalServiceImpl implements IChemicalService {

    @Autowired
    private ChemicalMapper chemicalMapper;

    /**
     * 查询全部危化品数据
     * @return List<Chemical>
     */
    @Override
    public List<Chemical> listChemical() {
        return chemicalMapper.listChemical(null, null, null, null);
    }
    
    /**
     * 分页查询危化品数据
     * @param name 名称（可选）
     * @param category 类别（可选）
     * @param page 页码
     * @param size 每页大小
     * @return List<Chemical>
     */
    @Override
    public List<Chemical> listChemical(String name, String category, Integer page, Integer size) {
        // 添加日志输出
        System.out.println("Service层接收到的参数: name=" + name + ", category=" + category + ", page=" + page + ", size=" + size);
        
        // 确保分页参数有效
        if (page == null || page < 0) {
            page = 0;
        }
        if (size == null || size <= 0) {
            size = 10;
        }
        
        // 添加日志输出
        System.out.println("Service层处理后的参数: name=" + name + ", category=" + category + ", page=" + page + ", size=" + size);
        
        try {
            List<Chemical> result = chemicalMapper.listChemical(name, category, page, size);
            
            // 添加日志输出
            System.out.println("Service层查询结果: result.size=" + (result != null ? result.size() : "null"));
            
            return result;
        } catch (Exception e) {
            System.err.println("查询出错: " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    /**
     * 根据 id 查询一条 危化品 数据
     * @param id
     * @return Chemical
     */
    @Override
    public Chemical selectChemicalById(Integer id) {
        return chemicalMapper.selectChemicalById(id);
    }

    /**
     * 新增一条 危化品 数据
     * @param chemical
     */
    @Override
    @Transactional
    public int addChemical(Chemical chemical) {
        return chemicalMapper.addChemical(chemical);
    }

    /**
     * 根据 id 删除一条 危化品 
     * @param id
     */
    @Override
    @Transactional
    public int removeChemical(Integer id) {
        return chemicalMapper.removeChemical(id);
    }

    /**
     * 更新一条 危化品 数据
     * @param chemical
     */
    @Override
    @Transactional
    public int editChemical(Chemical chemical) {
        return chemicalMapper.editChemical(chemical);
    }
    
    /**
     * 统计 Chemical 数据总数
     * @param params 查询参数
     * @return int
     */
    @Override
    public int countChemical(Map<String, Object> params) {
        return chemicalMapper.countChemical(params);
    }
    
    /**
     * 根据危险等级统计数量
     * @return List<Map<String, Object>>
     */
    @Override
    public List<Map<String, Object>> countByDangerLevel() {
        return chemicalMapper.countByDangerLevel();
    }
    
    /**
     * 根据存储条件统计数量
     * @return List<Map<String, Object>>
     */
    @Override
    public List<Map<String, Object>> countByStorageCondition() {
        return chemicalMapper.countByStorageCondition();
    }
    
    /**
     * 查询低于预警阈值的化学品
     * @return List<Chemical>
     */
    @Override
    public List<Chemical> listBelowWarningThreshold() {
        return chemicalMapper.listBelowWarningThreshold();
    }
}