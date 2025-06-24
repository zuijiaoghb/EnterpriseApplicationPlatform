package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecord10;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Rdrecord10Repository extends JpaRepository<Rdrecord10, Long> {
    @Query("SELECT MAX(r.cCode) FROM Rdrecord10 r WHERE r.cCode LIKE ?1")
    String findMaxCCodeByPrefix(String prefix);
}