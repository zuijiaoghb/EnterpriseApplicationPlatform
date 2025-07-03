package com.enterprise.platform.inventorymanagement.model.sqlserver;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rdrecords10_extradefine")
public class Rdrecords10Extradefine {
    @Id
    private Long AutoID;

    @Column(name = "cbdefine1")
    private Float cbdefine1;

    @Column(name = "cbdefine3")
    private String cbdefine3;

    @Column(name = "cbdefine4")
    private Float cbdefine4;

    @OneToOne
    @JoinColumn(name = "AutoID", referencedColumnName = "AutoID", insertable = false, updatable = false)
    private Rdrecords10 rdrecords10;
}