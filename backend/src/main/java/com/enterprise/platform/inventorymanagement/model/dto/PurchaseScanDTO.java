package com.enterprise.platform.inventorymanagement.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PurchaseScanDTO {
    private String cPOID;
    private LocalDateTime dPODate;
    
    private String cInvCode;
    
    private BigDecimal iQuantity;
    private BigDecimal remainingQuantity;
    private String barcode;

    // Getters and Setters
    public String getcPOID() { return cPOID; }
    public void setcPOID(String cPOID) { this.cPOID = cPOID; }
    public LocalDateTime getdPODate() { return dPODate; }
    public void setdPODate(LocalDateTime dPODate) { this.dPODate = dPODate; }

    public String getcInvCode() { return cInvCode; }
    public void setcInvCode(String cInvCode) { this.cInvCode = cInvCode; }

    public BigDecimal getiQuantity() { return iQuantity; }
    public void setiQuantity(BigDecimal iQuantity) { this.iQuantity = iQuantity; }
    public BigDecimal getRemainingQuantity() { return remainingQuantity; }
    public void setRemainingQuantity(BigDecimal remainingQuantity) { this.remainingQuantity = remainingQuantity; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
}