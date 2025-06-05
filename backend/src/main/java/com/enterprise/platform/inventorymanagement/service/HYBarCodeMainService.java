package com.enterprise.platform.inventorymanagement.service;

import java.util.List;
import java.util.Optional;

import com.enterprise.platform.inventorymanagement.model.sqlserver.HYBarCodeMain;

public interface HYBarCodeMainService {
    List<HYBarCodeMain> getAllBarCodeMains();
    Optional<HYBarCodeMain> getBarCodeMainById(String barCode);
    HYBarCodeMain saveBarCodeMain(HYBarCodeMain barCodeMain);
    void deleteBarCodeMain(String barCode);
}