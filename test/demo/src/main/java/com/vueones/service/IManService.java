package com.vueones.service;
import com.vueones.entity.Man;

import java.util.List;


public interface IManService {

    /**
     * 查询全部 Man 数据
     * @return List<Man>
     */
    List<Man> listMans();

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
    Man selectByEmail(String email);

    /**
     * 新增一条 Man 数据
     * @param man
     * @return boolean
     */
    boolean insertMan(Man man);

    /**
     * 根据 id 删除一条 Man 数据
     * @param id
     * @return int
     */
    int deleteManById(Integer id);

    /**
     * 更新一条 Man 数据
     * @param man
     * @return int  
     */
    int updateMan(Man man);
    
    /**
     * 更新用户最后登录时间
     * @param id 用户ID
     * @return 影响行数
     */
    int updateLastLoginTime(Integer id);
    
    /**
     * 用户登录认证
     * @param email 邮箱
     * @param password 密码
     * @param userType 用户类型
     * @return 认证成功返回用户信息，失败返回null
     */
    Man login(String email, String password, Integer userType);
    
    /**
     * 用户注册
     * @param man 用户信息
     * @return 注册成功返回true，失败返回false
     */
    boolean register(Man man);

    /**
     * 获取符合条件的记录总数
     * @param params 查询参数
     * @return 记录总数
     */
    int getTotal(java.util.Map<String, Object> params);

    /**
     * 查询Man数据，支持分页和条件查询
     * @param params 查询参数
     * @return List<Man>
     */
    List<Man> listMans(java.util.Map<String, Object> params);
}