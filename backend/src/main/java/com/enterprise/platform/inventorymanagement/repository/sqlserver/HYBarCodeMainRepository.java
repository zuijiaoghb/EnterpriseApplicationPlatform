package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enterprise.platform.inventorymanagement.model.sqlserver.HYBarCodeMain;

public interface HYBarCodeMainRepository extends JpaRepository<HYBarCodeMain, String> {
}