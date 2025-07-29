package com.enterprise.platform.inventorymanagement.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PurchaseScanDTO {
    private String cPOID;
    private LocalDateTime dPODate;
    private String cVenCode;
    private String cDefine1; // 执行公司
    private String cPersonCode; // 采购员
    private String cDepCode; // 采购部门
    private String cInvCode;
    private String cItemName; // 存货名称
    private BigDecimal iQuantity;
    private String cUnitID; // 单位ID
    private String unitName; // 单位名称
    private LocalDateTime dArriveDate; // 计划到货日期
    private BigDecimal remainingQuantity;
    private BigDecimal receivedQuantity; // 已入库数量
    private String barcode;
    private String batchNumber; // 批号
    private Integer irowno; // 行号
    private BigDecimal boxQuantity; // 一箱的数量
    private String supplierName; // 供应商名称

    // Getters and Setters
    public String getcPOID() { return cPOID; }
    public void setcPOID(String cPOID) { this.cPOID = cPOID; }
    public LocalDateTime getdPODate() { return dPODate; }
    public void setdPODate(LocalDateTime dPODate) { this.dPODate = dPODate; }
    public String getcInvCode() { return cInvCode; }
    public void setcInvCode(String cInvCode) { this.cInvCode = cInvCode; }
    public String getcVenCode() { return cVenCode; }
    public void setcVenCode(String cVenCode) { this.cVenCode = cVenCode; }
    public String getcDefine1() { return cDefine1; }
    public void setcDefine1(String cDefine1) { this.cDefine1 = cDefine1; }
    public String getcPersonCode() { return cPersonCode; }
    public void setcPersonCode(String cPersonCode) { this.cPersonCode = cPersonCode; }
    public String getcDepCode() { return cDepCode; }
    public void setcDepCode(String cDepCode) { this.cDepCode = cDepCode; }
    public String getcItemName() { return cItemName; }
    public void setcItemName(String cItemName) { this.cItemName = cItemName; }
    public String getcUnitID() { return cUnitID; }
    public void setcUnitID(String cUnitID) { this.cUnitID = cUnitID; }
    public LocalDateTime getdArriveDate() { return dArriveDate; }
    public void setdArriveDate(LocalDateTime dArriveDate) { this.dArriveDate = dArriveDate; }
    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }
    public Integer getIrowno() { return irowno; }
    public void setIrowno(Integer irowno) { this.irowno = irowno; }
    public BigDecimal getBoxQuantity() { return boxQuantity; }
    public void setBoxQuantity(BigDecimal boxQuantity) { this.boxQuantity = boxQuantity; }

    public BigDecimal getiQuantity() { return iQuantity; }
    public void setiQuantity(BigDecimal iQuantity) { this.iQuantity = iQuantity; }
    public BigDecimal getRemainingQuantity() { return remainingQuantity; }
    public void setRemainingQuantity(BigDecimal remainingQuantity) { this.remainingQuantity = remainingQuantity; }
    public BigDecimal getReceivedQuantity() { return receivedQuantity; }
    public void setReceivedQuantity(BigDecimal receivedQuantity) { this.receivedQuantity = receivedQuantity; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public String getUnitName() { return unitName; }
    public void setUnitName(String unitName) { this.unitName = unitName; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
}