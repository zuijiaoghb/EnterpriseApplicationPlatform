package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecord10Extradefine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Rdrecord10ExtradefineRepository extends JpaRepository<Rdrecord10Extradefine, Long> {
}