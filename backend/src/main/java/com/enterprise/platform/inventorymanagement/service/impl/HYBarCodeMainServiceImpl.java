package com.enterprise.platform.inventorymanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enterprise.platform.inventorymanagement.model.sqlserver.HYBarCodeMain;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.HYBarCodeMainRepository;
import com.enterprise.platform.inventorymanagement.service.HYBarCodeMainService;

import java.util.List;
import java.util.Optional;

@Service
public class HYBarCodeMainServiceImpl implements HYBarCodeMainService {
    @Autowired
    private HYBarCodeMainRepository repository;

    @Override
    public List<HYBarCodeMain> getAllBarCodeMains() {
        return repository.findAll();
    }

    @Override
    public Optional<HYBarCodeMain> getBarCodeMainById(String barCode) {
        return repository.findById(barCode);
    }

    @Override
    public HYBarCodeMain saveBarCodeMain(HYBarCodeMain barCodeMain) {
        return repository.save(barCodeMain);
    }

    @Override
    public void deleteBarCodeMain(String barCode) {
        repository.deleteById(barCode);
    }
}