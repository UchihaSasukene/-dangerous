package com.vueones.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class, 
    property = "id"
)
public class OutboundRecord {
    private Integer id;
    private Integer chemicalId;
    private String chemicalName;
    private Integer inventoryId;
    private Double amount;
    private String unit;
    private String batchNo;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outboundTime;
    private Integer operatorId;
    private String recipient;
    private String purpose;
    private String notes;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    // 关联对象
    private Chemical chemical;
    private Inventory inventory;
    private Man operator;

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
    
    public String getChemicalName() {
        return chemicalName;
    }
    
    public void setChemicalName(String chemicalName) {
        this.chemicalName = chemicalName;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Date getOutboundTime() {
        return outboundTime;
    }

    public void setOutboundTime(Date outboundTime) {
        this.outboundTime = outboundTime;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Man getOperator() {
        return operator;
    }

    public void setOperator(Man operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "OutboundRecord{" +
                "id=" + id +
                ", chemicalId=" + chemicalId +
                ", chemicalName=" + chemicalName +
                ", inventoryId=" + inventoryId +
                ", amount=" + amount +
                ", unit='" + unit + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", outboundTime=" + outboundTime +
                ", operatorId=" + operatorId +
                ", recipient='" + recipient + '\'' +
                ", purpose='" + purpose + '\'' +
                ", notes='" + notes + '\'' +
                ", createTime=" + createTime +
                ", chemical=" + (chemical != null ? chemical.getId() : null) +
                ", inventory=" + (inventory != null ? inventory.getId() : null) +
                ", operator=" + (operator != null ? operator.getId() : null) +
                '}';
    }
}