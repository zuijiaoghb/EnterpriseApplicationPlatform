package com.enterprise.platform.inventorymanagement.model.sqlserver;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "mom_order")
@DynamicInsert
@DynamicUpdate
public class MomOrder {

    @Id
    @Column(name = "MoId")
    private Integer moId;

    @Column(name = "MoCode", nullable = false)
    private String moCode;

    @Column(name = "CreateDate")
    private LocalDateTime createDate;

    @Column(name = "CreateUser")
    private String createUser;

    @Column(name = "ModifyDate")
    private LocalDateTime modifyDate;

    @Column(name = "ModifyUser")
    private String modifyUser;

    @Column(name = "UpdCount")
    private Integer updCount;

    @Version
    @Column(name = "Ufts",insertable = false,updatable = false)
    private byte[] ufts;

    @Column(name = "Define1")
    private String define1;

    @Column(name = "Define2")
    private String define2;

    @Column(name = "Define3")
    private String define3;

    @Column(name = "Define4")
    private LocalDateTime define4;

    @Column(name = "Define5")
    private Integer define5;

    @Column(name = "Define6")
    private LocalDateTime define6;

    @Column(name = "Define7")
    private Double define7;

    @Column(name = "Define8")
    private String define8;

    @Column(name = "Define9")
    private String define9;

    @Column(name = "Define10")
    private String define10;

    @Column(name = "Define11")
    private String define11;

    @Column(name = "Define12")
    private String define12;

    @Column(name = "Define13")
    private String define13;

    @Column(name = "Define14")
    private String define14;

    @Column(name = "Define15")
    private Integer define15;

    @Column(name = "Define16")
    private Double define16;

    @Column(name = "VTid")
    private Integer vtid;

    @Column(name = "CreateTime")
    private LocalDateTime createTime;

    @Column(name = "ModifyTime")
    private LocalDateTime modifyTime;

    @Column(name = "iPrintCount")
    private Integer iPrintCount;

    @Column(name = "RelsVTid")
    private Integer relsVTid;

    @Column(name = "cSysBarCode")
    private String cSysBarCode;

    // 关联订单子表
    @OneToMany(mappedBy = "momOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MomOrderdetail> orderDetails;

    // 关联生产订单表
    @OneToMany(mappedBy = "momOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MomMorder> morders;
}