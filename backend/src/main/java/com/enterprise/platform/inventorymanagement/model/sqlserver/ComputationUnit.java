package com.enterprise.platform.inventorymanagement.model.sqlserver;

import lombok.Data;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Data
@Entity
@Table(name = "ComputationUnit", schema = "dbo")
public class ComputationUnit {
    @Id
    @Column(name = "cComunitCode", length = 35, nullable = false)
    private String cComunitCode;

    @Column(name = "cComUnitName", length = 20, nullable = false)
    private String cComUnitName;

    @Column(name = "cGroupCode", length = 35)
    private String cGroupCode;

    @Column(name = "cBarCode", length = 30)
    private String cBarCode;

    @Column(name = "bMainUnit", nullable = false)
    private Boolean bMainUnit;

    @Column(name = "iChangRate", precision = 28, scale = 10)
    private BigDecimal iChangRate;

    @Column(name = "iProportion")
    private Double iProportion;

    @Column(name = "iNumber")
    private Short iNumber;

    @Version
    @Column(name = "pubufts",insertable = false,updatable = false)
    private byte[] pubufts;

    @Column(name = "cEnSingular", length = 60)
    private String cEnSingular;

    @Column(name = "cEnPlural", length = 60)
    private String cEnPlural;

    @Column(name = "cUnitRefInvCode", length = 20)
    private String cUnitRefInvCode;
}