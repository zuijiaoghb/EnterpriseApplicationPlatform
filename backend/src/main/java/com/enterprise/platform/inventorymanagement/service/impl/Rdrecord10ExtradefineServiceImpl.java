package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecord10Extradefine;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.Rdrecord10ExtradefineRepository;
import com.enterprise.platform.inventorymanagement.service.Rdrecord10ExtradefineService;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "sqlServerTransactionManager")
public class Rdrecord10ExtradefineServiceImpl implements Rdrecord10ExtradefineService {
    private final Rdrecord10ExtradefineRepository repository;

    @Override
    public List<Rdrecord10Extradefine> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Rdrecord10Extradefine> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Rdrecord10Extradefine save(Rdrecord10Extradefine entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}