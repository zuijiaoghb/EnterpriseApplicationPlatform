package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.dto.U8ToWmsDTO;
import com.enterprise.platform.inventorymanagement.model.sqlserver.U8ToWms;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.U8ToWmsRepository;
import com.enterprise.platform.inventorymanagement.service.U8ToWmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "sqlServerTransactionManager")
public class U8ToWmsServiceImpl implements U8ToWmsService {

    private final U8ToWmsRepository u8ToWmsRepository;

    @Override
    public Optional<U8ToWmsDTO> getById(Long id) {
        return u8ToWmsRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public List<U8ToWmsDTO> getAll() {
        return u8ToWmsRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public U8ToWmsDTO save(U8ToWmsDTO u8ToWmsDTO) {
        U8ToWms entity = convertToEntity(u8ToWmsDTO);
        U8ToWms savedEntity = u8ToWmsRepository.save(entity);
        return convertToDTO(savedEntity);
    }

    @Override
    public U8ToWmsDTO update(Long id, U8ToWmsDTO u8ToWmsDTO) {
        return u8ToWmsRepository.findById(id)
                .map(existingEntity -> {
                    updateEntityFromDTO(existingEntity, u8ToWmsDTO);
                    U8ToWms updatedEntity = u8ToWmsRepository.save(existingEntity);
                    return convertToDTO(updatedEntity);
                })
                .orElseThrow(() -> new RuntimeException("U8ToWms record not found with id: " + id));
    }

    @Override
    public void delete(Long id) {
        u8ToWmsRepository.deleteById(id);
    }

    private U8ToWmsDTO convertToDTO(U8ToWms entity) {
        U8ToWmsDTO dto = new U8ToWmsDTO();
        dto.setAID(entity.getAID());
        dto.setID(entity.getID());
        dto.setVoucherType(entity.getVoucherType());
        dto.setCCode(entity.getCCode());
        dto.setDDate(entity.getDDate());
        dto.setCDepCode(entity.getCDepCode());
        dto.setCDepName(entity.getCDepName());
        dto.setCVenCode(entity.getCVenCode());
        dto.setCVenName(entity.getCVenName());
        dto.setCWhCode(entity.getCWhCode());
        dto.setCWhName(entity.getCWhName());
        dto.setCPersonCode(entity.getCPersonCode());
        dto.setCPersonName(entity.getCPersonName());
        dto.setCDeliverCode(entity.getCDeliverCode());
        dto.setCMemo(entity.getCMemo());
        dto.setCMaker(entity.getCMaker());
        dto.setAutoID(entity.getAutoID());
        dto.setIRowNo(entity.getIRowNo());
        dto.setCInvCode(entity.getCInvCode());
        dto.setCInvName(entity.getCInvName());
        dto.setCInvStd(entity.getCInvStd());
        dto.setWb(entity.getWb());
        dto.setCBatch(entity.getCBatch());
        dto.setCSnCode(entity.getCSnCode());
        dto.setCUnitCode(entity.getCUnitCode());
        dto.setCUnitName(entity.getCUnitName());
        dto.setIQuantity(entity.getIQuantity());
        dto.setIOriTaxCost(entity.getIOriTaxCost());
        dto.setIOriTaxMoney(entity.getIOriTaxMoney());
        dto.setCbMemo(entity.getCbMemo());
        dto.setU8CreateDatetime(entity.getU8CreateDatetime());
        dto.setU8Status(entity.getU8Status());
        dto.setWmsReadError(entity.getWmsReadError());
        dto.setCInvCCode(entity.getCInvCCode());
        return dto;
    }

    private U8ToWms convertToEntity(U8ToWmsDTO dto) {
        U8ToWms entity = new U8ToWms();
        entity.setID(dto.getID());
        entity.setVoucherType(dto.getVoucherType());
        entity.setCCode(dto.getCCode());
        entity.setDDate(dto.getDDate());
        entity.setCDepCode(dto.getCDepCode());
        entity.setCDepName(dto.getCDepName());
        entity.setCVenCode(dto.getCVenCode());
        entity.setCVenName(dto.getCVenName());
        entity.setCWhCode(dto.getCWhCode());
        entity.setCWhName(dto.getCWhName());
        entity.setCPersonCode(dto.getCPersonCode());
        entity.setCPersonName(dto.getCPersonName());
        entity.setCDeliverCode(dto.getCDeliverCode());
        entity.setCMemo(dto.getCMemo());
        entity.setCMaker(dto.getCMaker());
        entity.setAutoID(dto.getAutoID());
        entity.setIRowNo(dto.getIRowNo());
        entity.setCInvCode(dto.getCInvCode());
        entity.setCInvName(dto.getCInvName());
        entity.setCInvStd(dto.getCInvStd());
        entity.setWb(dto.getWb());
        entity.setCBatch(dto.getCBatch());
        entity.setCSnCode(dto.getCSnCode());
        entity.setCUnitCode(dto.getCUnitCode());
        entity.setCUnitName(dto.getCUnitName());
        entity.setIQuantity(dto.getIQuantity());
        entity.setIOriTaxCost(dto.getIOriTaxCost());
        entity.setIOriTaxMoney(dto.getIOriTaxMoney());
        entity.setCbMemo(dto.getCbMemo());
        entity.setU8CreateDatetime(dto.getU8CreateDatetime());
        entity.setU8Status(dto.getU8Status());
        entity.setWmsReadError(dto.getWmsReadError());
        entity.setCInvCCode(dto.getCInvCCode());
        return entity;
    }

    private void updateEntityFromDTO(U8ToWms entity, U8ToWmsDTO dto) {
        entity.setID(dto.getID());
        entity.setVoucherType(dto.getVoucherType());
        entity.setCCode(dto.getCCode());
        entity.setDDate(dto.getDDate());
        entity.setCDepCode(dto.getCDepCode());
        entity.setCDepName(dto.getCDepName());
        entity.setCVenCode(dto.getCVenCode());
        entity.setCVenName(dto.getCVenName());
        entity.setCWhCode(dto.getCWhCode());
        entity.setCWhName(dto.getCWhName());
        entity.setCPersonCode(dto.getCPersonCode());
        entity.setCPersonName(dto.getCPersonName());
        entity.setCDeliverCode(dto.getCDeliverCode());
        entity.setCMemo(dto.getCMemo());
        entity.setCMaker(dto.getCMaker());
        entity.setAutoID(dto.getAutoID());
        entity.setIRowNo(dto.getIRowNo());
        entity.setCInvCode(dto.getCInvCode());
        entity.setCInvName(dto.getCInvName());
        entity.setCInvStd(dto.getCInvStd());
        entity.setWb(dto.getWb());
        entity.setCBatch(dto.getCBatch());
        entity.setCSnCode(dto.getCSnCode());
        entity.setCUnitCode(dto.getCUnitCode());
        entity.setCUnitName(dto.getCUnitName());
        entity.setIQuantity(dto.getIQuantity());
        entity.setIOriTaxCost(dto.getIOriTaxCost());
        entity.setIOriTaxMoney(dto.getIOriTaxMoney());
        entity.setCbMemo(dto.getCbMemo());
        entity.setU8CreateDatetime(dto.getU8CreateDatetime());
        entity.setU8Status(dto.getU8Status());
        entity.setWmsReadError(dto.getWmsReadError());
        entity.setCInvCCode(dto.getCInvCCode());
    }
}