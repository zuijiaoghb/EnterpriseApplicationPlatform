package com.enterprise.platform.inventorymanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enterprise.platform.inventorymanagement.model.sqlserver.HYBarCodeMain;

public interface HYBarCodeMainService {
    List<HYBarCodeMain> getAllBarCodeMains();
    Page<HYBarCodeMain> getAllBarCodeMains(Pageable pageable);
    Optional<HYBarCodeMain> getBarCodeMainById(String barCode);
    HYBarCodeMain saveBarCodeMain(HYBarCodeMain barCodeMain);
    void deleteBarCodeMain(String barCode);
}