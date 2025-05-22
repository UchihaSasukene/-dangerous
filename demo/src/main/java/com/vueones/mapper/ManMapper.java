package com.vueones.mapper;
import com.vueones.entity.Man;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ManMapper {

    /**
     * 查询全部 Man 数据
     * @return List<Man>
     */
    List<Man> selectList();

    /**
     * 根据 id 查询一条 Man 数据
     * @param id
     * @return Man
     */
    Man selectById(Integer id);

    /**
     * 根据邮箱查询一条 Man 数据
     * @param email 邮箱
     * @return Man
     */
    Man selectByEmail(@Param("email") String email);

    /**
     * 新增一条 Man 数据
     * @return
     */
    int insert(Man man);

    /**
     * 根据 id 删除一条 Man 数据
     * @return
     */
    int deleteById(Integer id);

    /**
     * 更新一条 Man 数据
     * @return
     */
    int updateMan(Man man);

    /**
     * 更新最后登录时间
     * @param id 用户ID
     * @return 影响行数
     */
    int updateLastLoginTime(@Param("id") Integer id);

    // 获取人员列表
    List<Man> listMans(Map<String, Object> params);
    
    // 获取总记录数
    int getTotal(Map<String, Object> params);
    
    
    // 添加人员
    int insertMan(Man man);
    
    
    // 删除人员
    int deleteManById(@Param("id") Integer id);
}