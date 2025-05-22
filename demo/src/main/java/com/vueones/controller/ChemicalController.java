package com.vueones.controller;

import com.vueones.common.R;
import com.vueones.entity.Chemical;
import com.vueones.service.IChemicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RestController
@RequestMapping("/chemical")
@CrossOrigin(origins = "*")
public class ChemicalController {

    @Autowired
    private IChemicalService chemicalService;

    /**
     * 获取危化品数据列表
     * @param page 页码
     * @param size 每页大小
     * @param name 名称（可选）
     * @param category 类别（可选）
     * @return Map 包含数据列表和总数
     */
    @GetMapping("/list")
    public R<?> listChemical(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("category", category);
        
        // 确保page至少为1
        page = Math.max(1, page);
        Integer offset = (page - 1) * size;
        
        List<Chemical> list = chemicalService.listChemical(name, category, offset, size);
        int total = chemicalService.countChemical(params);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        
        return R.ok(result);
    }

    /**
     * 查询一条危化品信息
     * @param id 危化品id
     * @return 危化品信息
     */
    @GetMapping("/list/{id}")
    public R<Chemical> getChemical(@PathVariable Integer id) {
        return R.ok(chemicalService.selectChemicalById(id));
    }

    /**
     * 新增危化品
     * @param chemical 危化品信息
     * @return 是否成功
     */
    @PostMapping("/add")
    public R<Integer> addChemical(@RequestBody Chemical chemical) {
        int result = chemicalService.addChemical(chemical);
        return result > 0 ? R.ok(result) : R.error("添加失败");
    }

    /**
     * 删除危化品
     * @param id 危化品id
     * @return 是否成功
     */
    
    @DeleteMapping("/delete/{id}")
    public R<Integer> removeChemical(@PathVariable Integer id) {
        int result = chemicalService.removeChemical(id);
        return result > 0 ? R.ok(result) : R.error("删除失败");
    }

    /**
     * 更新危化品
     * @param chemical 危化品信息
     * @return 是否成功
     */
    @PutMapping("/edit")
    public R<Integer> editChemical(@RequestBody Chemical chemical) {
        try {
            //验证id
            if (chemical.getId() == null) {
                return R.error("id不存在");
            }
            //验证化学品是否存在
            Chemical existingChemical = chemicalService.selectChemicalById(chemical.getId());
            if (existingChemical == null) {
                return R.error("化学品不存在");
            }
            
            int result = chemicalService.editChemical(chemical);
            return result > 0 ? R.ok(result) : R.error("更新失败");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 根据危险等级统计数量
     * @return 危险等级统计数量
     */
    @GetMapping("/stats/danger-level")
    public R<List<Map<String, Object>>> countByDangerLevel() {
        return R.ok(chemicalService.countByDangerLevel());
    }

    /**
     * 根据存储条件统计数量
     * @return 存储条件统计数量
     */
    @GetMapping("/stats/storage-condition")
    public R<List<Map<String, Object>>> countByStorageCondition() {
        return R.ok(chemicalService.countByStorageCondition());
    }

    /**
     * 查询低于预警阈值的化学品
     * @return 低于预警阈值的化学品
     */
    @GetMapping("/below-warning-threshold")
    public R<List<Chemical>> listBelowWarningThreshold() {
        return R.ok(chemicalService.listBelowWarningThreshold());
    }
}
