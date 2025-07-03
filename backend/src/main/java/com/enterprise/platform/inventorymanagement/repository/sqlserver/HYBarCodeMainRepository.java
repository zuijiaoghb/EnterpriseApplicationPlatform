package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.HYBarCodeMain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HYBarCodeMainRepository extends JpaRepository<HYBarCodeMain, String> {
    Optional<HYBarCodeMain> findByBarcode(String barcode);    
    Optional<HYBarCodeMain> findByCsrccodeAndCsrcsubid(String csrccode, Integer csrcsubid);
    void deleteByBarcode(String barcode);
}