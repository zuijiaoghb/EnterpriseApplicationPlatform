package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecords10Extradefine;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.Rdrecords10ExtradefineRepository;
import com.enterprise.platform.inventorymanagement.service.Rdrecords10ExtradefineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "sqlServerTransactionManager")
public class Rdrecords10ExtradefineServiceImpl implements Rdrecords10ExtradefineService {
    private final Rdrecords10ExtradefineRepository repository;

    @Override
    public List<Rdrecords10Extradefine> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Rdrecords10Extradefine> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Rdrecords10Extradefine save(Rdrecords10Extradefine entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}