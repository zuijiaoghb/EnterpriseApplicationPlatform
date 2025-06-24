package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecords10;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface Rdrecords10Repository extends JpaRepository<Rdrecords10, Long> {
    @Query("SELECT r FROM Rdrecords10 r WHERE r.cBarCode = :barcode")
    List<Rdrecords10> findByCBarCode(@Param("barcode") String barcode);

    List<Rdrecords10> findByRdrecord10_Id(Long id);

}