package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.dto.VendorDTO;
import com.enterprise.platform.inventorymanagement.model.sqlserver.Vendor;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.VendorRepository;
import com.enterprise.platform.inventorymanagement.service.VendorService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "sqlServerTransactionManager")
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Override
    public List<VendorDTO> findAll() {
        return vendorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public VendorDTO findById(String cVenCode) {
        Vendor vendor = vendorRepository.findById(cVenCode)
                .orElseThrow(() -> new RuntimeException("Vendor not found with code: " + cVenCode));
        return convertToDto(vendor);
    }

    @Override
    public VendorDTO save(VendorDTO vendorDTO) {
        Vendor vendor = convertToEntity(vendorDTO);
        Vendor savedVendor = vendorRepository.save(vendor);
        return convertToDto(savedVendor);
    }

    @Override
    public VendorDTO update(String cVenCode, VendorDTO vendorDTO) {
        Vendor existingVendor = vendorRepository.findById(cVenCode)
                .orElseThrow(() -> new RuntimeException("Vendor not found with code: " + cVenCode));
        BeanUtils.copyProperties(vendorDTO, existingVendor, "cVenCode");
        Vendor updatedVendor = vendorRepository.save(existingVendor);
        return convertToDto(updatedVendor);
    }

    @Override
    public void deleteById(String cVenCode) {
        if (!vendorRepository.existsById(cVenCode)) {
            throw new RuntimeException("Vendor not found with code: " + cVenCode);
        }
        vendorRepository.deleteById(cVenCode);
    }

    private VendorDTO convertToDto(Vendor vendor) {
        VendorDTO vendorDTO = new VendorDTO();
        BeanUtils.copyProperties(vendor, vendorDTO);
        return vendorDTO;
    }

    private Vendor convertToEntity(VendorDTO vendorDTO) {
        Vendor vendor = new Vendor();
        BeanUtils.copyProperties(vendorDTO, vendor);
        return vendor;
    }
}