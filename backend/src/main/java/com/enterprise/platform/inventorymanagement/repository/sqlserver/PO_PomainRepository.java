package com.enterprise.platform.inventorymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.platform.inventorymanagement.model.sqlserver.entity.PO_Pomain;

@Repository
public interface PO_PomainRepository extends JpaRepository<PO_Pomain, Integer> {
    PO_Pomain findBycPOID(String cPOID);
}