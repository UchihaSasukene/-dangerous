package com.vueones.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class, 
    property = "id",
    scope = Inventory.class
)
public class Inventory {
    private Integer id;
    private Integer chemicalId;
    private String location;
    private Double currentAmount;
    private String unit;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastCheckTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    // 关联的危化品信息
    private Chemical chemical;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(Date lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Chemical getChemical() {
        return chemical;
    }

    public void setChemical(Chemical chemical) {
        this.chemical = chemical;
    }
    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", chemicalId=" + chemicalId +
                ", location='" + location + '\'' +
                ", currentAmount=" + currentAmount +
                ", unit='" + unit + '\'' +
                ", lastCheckTime=" + lastCheckTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", chemical=" + (chemical != null ? chemical.getId() : null) +
                '}';
    }

} 