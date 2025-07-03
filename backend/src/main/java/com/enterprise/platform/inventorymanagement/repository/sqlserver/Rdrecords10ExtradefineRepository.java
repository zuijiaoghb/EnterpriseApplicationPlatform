package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecords10Extradefine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Rdrecords10ExtradefineRepository extends JpaRepository<Rdrecords10Extradefine, Long> {
}