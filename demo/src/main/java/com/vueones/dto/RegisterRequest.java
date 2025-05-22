package com.vueones.dto;

/**
 * 注册请求DTO
 */
public class RegisterRequest {
    
    private String email;
    private String name;
    private String password;
    private Integer userType;
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Integer getUserType() {
        return userType;
    }
    
    public void setUserType(Integer userType) {
        this.userType = userType;
    }
    
    @Override
    public String toString() {
        return "RegisterRequest{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", userType=" + userType +
                '}';
    }
} 