package com.enterprise.platform.inventorymanagement.model.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MomOrderdetailDTO {
    private Integer moDId;
    private BigDecimal iQuantity;
    private String invCode;
    private String moLotCode;
    private BigDecimal qualifiedInQty;
}