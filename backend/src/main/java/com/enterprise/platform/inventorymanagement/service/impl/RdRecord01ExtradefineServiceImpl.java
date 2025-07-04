package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.sqlserver.RdRecord01Extradefine;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.RdRecord01ExtradefineRepository;
import com.enterprise.platform.inventorymanagement.service.RdRecord01ExtradefineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "sqlServerTransactionManager")
public class RdRecord01ExtradefineServiceImpl implements RdRecord01ExtradefineService {

    private final RdRecord01ExtradefineRepository repository;

    @Override
    public List<RdRecord01Extradefine> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<RdRecord01Extradefine> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public RdRecord01Extradefine save(RdRecord01Extradefine record) {
        return repository.save(record);
    }

    @Override
    public RdRecord01Extradefine update(Long id, RdRecord01Extradefine record) {
        return repository.findById(id)
                .map(existingRecord -> {
                    existingRecord.setChdefine1(record.getChdefine1());
                    existingRecord.setChdefine2(record.getChdefine2());
                    return repository.save(existingRecord);
                })
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}