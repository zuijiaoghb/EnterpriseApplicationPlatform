package com.enterprise.platform.inventorymanagement.model.sqlserver;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rdrecords01_extradefine")
public class Rdrecords01Extradefine {
    @Id
    private Long AutoID;

    @OneToOne
    @JoinColumn(name = "AutoID", referencedColumnName = "AutoID", insertable = false, updatable = false)
    private RdRecords01 rdRecords01;

    @Column(name = "cbdefine1")
    private Float cbdefine1;

    @Column(name = "cbdefine3")
    private String cbdefine3;

    @Column(name = "cbdefine4")
    private Float cbdefine4;
}