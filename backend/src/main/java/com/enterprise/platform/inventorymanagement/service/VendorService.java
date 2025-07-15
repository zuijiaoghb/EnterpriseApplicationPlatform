package com.enterprise.platform.inventorymanagement.service;

import com.enterprise.platform.inventorymanagement.model.dto.VendorDTO;
import java.util.List;

public interface VendorService {
    List<VendorDTO> findAll();
    VendorDTO findById(String cVenCode);
    VendorDTO save(VendorDTO vendorDTO);
    VendorDTO update(String cVenCode, VendorDTO vendorDTO);
    void deleteById(String cVenCode);
}