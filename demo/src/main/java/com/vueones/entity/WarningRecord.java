package com.vueones.entity;

import java.util.Date;

public class WarningRecord {
    private Integer id;
    private Integer chemicalId;
    private String chemicalName;
    private String warningType;    // 预警类型：库存不足、即将过期等
    private String warningLevel;   // 预警等级：一般、严重、紧急
    private String warningContent; // 预警内容
    private String status;         // 处理状态：未处理、处理中、已处理
    private Date warningTime;      // 预警时间
    private Date handleTime;       // 处理时间
    private String handler;        // 处理人
    private String handleResult;   // 处理结果

    // 关联的危化品信息
    private Chemical chemical;
    // 关联的库存信息
    private Inventory inventory;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChemicalId() {
        return chemicalId;
    }

    public void setChemicalId(Integer chemicalId) {
        this.chemicalId = chemicalId;
    }

    public String getWarningType() {
        return warningType;
    }

    public void setWarningType(String warningType) {
        this.warningType = warningType;
    }

    public String getWarningLevel() {
        return warningLevel;
    }

    public void setWarningLevel(String warningLevel) {
        this.warningLevel = warningLevel;
    }

    public String getWarningContent() {
        return warningContent;
    }

    public void setWarningContent(String warningContent) {
        this.warningContent = warningContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    // 为了兼容后端代码，添加handleStatus的getter和setter
    public String getHandleStatus() {
        return status;
    }

    public void setHandleStatus(String handleStatus) {
        this.status = handleStatus;
    }

    public Date getWarningTime() {
        return warningTime;
    }

    public void setWarningTime(Date warningTime) {
        this.warningTime = warningTime;
    }
    
    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }
    
    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public Chemical getChemical() {
        return chemical;
    }

    public void setChemical(Chemical chemical) {
        this.chemical = chemical;
    }
    
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public String toString() {
        return "WarningRecord{" +
                "id=" + id +
                ", chemicalId=" + chemicalId +
                ", chemicalName=" + chemicalName +
                ", warningType='" + warningType + '\'' +
                ", warningLevel='" + warningLevel + '\'' +
                ", warningContent='" + warningContent + '\'' +
                ", handleStatus='" + status + '\'' +
                ", warningTime=" + warningTime +
                ", handleTime=" + handleTime +
                ", handler='" + handler + '\'' +
                ", handleResult='" + handleResult + '\'' +
                ", chemical=" + chemical +
                ", inventory=" + inventory +
                '}';
    }
}