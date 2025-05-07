package com.vueones.controller;

import com.vueones.dto.LoginRequest;
import com.vueones.dto.LoginResponse;
import com.vueones.dto.RegisterRequest;
import com.vueones.entity.Man;
import com.vueones.service.IManService;
import com.vueones.utils.JwtUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@Log4j2
public class UserController {

    @Autowired
    private IManService manService;
    
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            log.info("接收到登录请求：{}", loginRequest);
            
            // 参数验证
            if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("code", 400);
                error.put("message", "邮箱和密码不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            // 认证用户
            Man man = manService.login(loginRequest.getEmail(), loginRequest.getPassword(), loginRequest.getUserType());
            
            if (man == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("code", 401);
                error.put("message", "邮箱或密码错误");
                return ResponseEntity.status(401).body(error);
            }
            
            // 生成JWT
            String token = jwtUtils.generateToken(man.getId(), man.getUserType());
            
            // 构建响应
            LoginResponse loginResponse = new LoginResponse(man, token);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "登录成功");
            response.put("data", loginResponse);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("登录失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "登录失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 用户注册
     * @param registerRequest 注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            log.info("接收到注册请求：{}", registerRequest);
            
            // 参数验证
            if (registerRequest.getEmail() == null || registerRequest.getPassword() == null || registerRequest.getName() == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("code", 400);
                error.put("message", "邮箱、密码和姓名不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            // 检查邮箱是否已存在
            Man existingMan = manService.selectByEmail(registerRequest.getEmail());
            if (existingMan != null) {
                Map<String, Object> error = new HashMap<>();
                error.put("code", 400);
                error.put("message", "该邮箱已注册");
                return ResponseEntity.badRequest().body(error);
            }
            
            // 创建用户
            Man man = new Man();
            man.setEmail(registerRequest.getEmail());
            man.setName(registerRequest.getName());
            man.setPassword(registerRequest.getPassword());
            man.setUserType(registerRequest.getUserType());
            man.setStatus(1); // 默认启用
            
            boolean success = manService.register(man);
            
            if (success) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 200);
                response.put("message", "注册成功");
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> error = new HashMap<>();
                error.put("code", 500);
                error.put("message", "注册失败");
                return ResponseEntity.status(500).body(error);
            }
        } catch (Exception e) {
            log.error("注册失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "注册失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 检查token有效性
     * @param token JWT令牌
     * @return 检查结果
     */
    @GetMapping("/check-token")
    public ResponseEntity<?> checkToken(@RequestParam String token) {
        try {
            // 从令牌中获取用户ID
            Integer userId = jwtUtils.getUserIdFromToken(token);
            
            // 查询用户信息
            Man man = manService.selectById(userId);
            
            if (man == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("code", 401);
                error.put("message", "无效的令牌");
                return ResponseEntity.status(401).body(error);
            }
            
            // 验证令牌
            boolean valid = jwtUtils.validateToken(token, man.getId());
            
            if (!valid) {
                Map<String, Object> error = new HashMap<>();
                error.put("code", 401);
                error.put("message", "令牌已过期或无效");
                return ResponseEntity.status(401).body(error);
            }
            
            // 构建响应
            Map<String, Object> data = new HashMap<>();
            data.put("user", man);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "有效的令牌");
            response.put("data", data);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("检查令牌失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("code", 401);
            error.put("message", "无效的令牌");
            return ResponseEntity.status(401).body(error);
        }
    }
} 