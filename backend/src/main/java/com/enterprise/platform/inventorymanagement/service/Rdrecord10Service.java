package com.enterprise.platform.inventorymanagement.service;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecord10;
import java.util.Optional;

public interface Rdrecord10Service {
    Optional<Rdrecord10> getInboundByBarcode(String barcode);
    Rdrecord10 createInbound(Rdrecord10 inbound);   
    Rdrecord10 createInboundByBarcode(String barcode, String cwhcode, Integer iQuantity);
}