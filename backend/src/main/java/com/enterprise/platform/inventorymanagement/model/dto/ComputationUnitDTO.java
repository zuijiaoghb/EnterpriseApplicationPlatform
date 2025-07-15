package com.enterprise.platform.inventorymanagement.model.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ComputationUnitDTO {
    private String cComunitCode;
    private String cComUnitName;
    private String cGroupCode;
    private String cBarCode;
    private Boolean bMainUnit;
    private BigDecimal iChangRate;
    private Double iProportion;
    private Short iNumber;
    private String cEnSingular;
    private String cEnPlural;
    private String cUnitRefInvCode;
}