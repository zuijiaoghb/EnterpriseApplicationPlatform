package com.enterprise.platform.inventorymanagement.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CurrentStockDTO {
    private Integer autoId;
    private String cWhCode;
    private String cInvCode;
    private Integer itemId;
    private String cBatch;
    private BigDecimal iQuantity;
    private BigDecimal iNum;
    private String cFree1;
    private String cFree2;
    private BigDecimal fAvaQuantity;
    private BigDecimal fAvaNum;
    private BigDecimal fInQuantity;
    private LocalDateTime dVDate;
    private String cCheckState;
    private LocalDateTime dExpirationdate;
    private BigDecimal ipeqty;
    private BigDecimal ipenum;
}