package com.vueones.dto;

import com.vueones.entity.Man;

/**
 * 登录响应DTO
 */
public class LoginResponse {
    
    private Man user;
    private String token;
    
    public LoginResponse() {
    }
    
    public LoginResponse(Man man, String token) {
        this.user = man;
        this.token = token;
    }
    
    public Man getUser() {
        return user;
    }
    
    public void setUser(Man man) {
        this.user = man;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
} 