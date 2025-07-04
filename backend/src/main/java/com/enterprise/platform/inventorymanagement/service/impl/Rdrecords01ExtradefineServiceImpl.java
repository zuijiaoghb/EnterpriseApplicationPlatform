package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecords01Extradefine;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.Rdrecords01ExtradefineRepository;
import com.enterprise.platform.inventorymanagement.service.Rdrecords01ExtradefineService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "sqlServerTransactionManager")
public class Rdrecords01ExtradefineServiceImpl implements Rdrecords01ExtradefineService {

    private final Rdrecords01ExtradefineRepository repository;

    @Override
    public List<Rdrecords01Extradefine> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Rdrecords01Extradefine> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Rdrecords01Extradefine save(Rdrecords01Extradefine record) {
        return repository.save(record);
    }

    @Override
    public Rdrecords01Extradefine update(Long id, Rdrecords01Extradefine record) {
        return repository.findById(id)
                .map(existingRecord -> {
                    existingRecord.setCbdefine1(record.getCbdefine1());
                    existingRecord.setCbdefine3(record.getCbdefine3());
                    existingRecord.setCbdefine4(record.getCbdefine4());
                    return repository.save(existingRecord);
                })
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}