package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecords01Extradefine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Rdrecords01ExtradefineRepository extends JpaRepository<Rdrecords01Extradefine, Long> {
}