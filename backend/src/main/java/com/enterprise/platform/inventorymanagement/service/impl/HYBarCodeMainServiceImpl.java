package com.enterprise.platform.inventorymanagement.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.platform.inventorymanagement.model.sqlserver.HYBarCodeMain;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.HYBarCodeMainRepository;
import com.enterprise.platform.inventorymanagement.service.HYBarCodeMainService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(transactionManager = "sqlServerTransactionManager")
public class HYBarCodeMainServiceImpl implements HYBarCodeMainService {
    private static final Logger logger = LoggerFactory.getLogger(HYBarCodeMainServiceImpl.class);

    @Autowired
    private HYBarCodeMainRepository repository;

    @PersistenceContext(unitName = "sqlServerEntityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<HYBarCodeMain> getAllBarCodeMains() {
        return repository.findAll();
    }
    
    @Override
    public Page<HYBarCodeMain> getAllBarCodeMains(Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        // 添加日志记录分页参数
        logger.info("Executing query with pageNumber: {}, pageSize: {}", pageNumber, pageSize);

        int offset = pageNumber * pageSize;
        int start = offset + 1;
        int end = offset + pageSize;

        // 记录实际SQL参数
        logger.info("SQL pagination range - start: {}, end: {}", start, end);

        // 使用 ROW_NUMBER() 进行分页查询
        String sql = "SELECT * FROM ("
                + "SELECT hcm.*, ROW_NUMBER() OVER (ORDER BY (SELECT 0)) as row_num "
                + "FROM HY_BarCodeMain hcm"
                + ") as subquery "
                + "WHERE row_num BETWEEN :start AND :end";

        Query query = entityManager.createNativeQuery(sql, HYBarCodeMain.class);
        query.setParameter("start", start);
        query.setParameter("end", end);

        // 由于 createNativeQuery 返回的是未经类型检查的 List，这里进行强制类型转换以解决类型安全问题
        @SuppressWarnings("unchecked")
        List<HYBarCodeMain> barCodeMains = (List<HYBarCodeMain>) query.getResultList();

        // 获取总记录数
        String countSql = "SELECT COUNT(*) FROM HY_BarCodeMain";
        Query countQuery = entityManager.createNativeQuery(countSql);
        Object result = countQuery.getSingleResult();
        Long total;
        if (result instanceof Integer) {
            total = ((Integer) result).longValue();
        } else if (result instanceof Long) {
            total = (Long) result;
        } else {
            throw new IllegalArgumentException("Unexpected type of count result: " + result.getClass().getName());
        }

        return new PageImpl<>(barCodeMains, pageable, total);
    }

    @Override
    public Optional<HYBarCodeMain> getBarCodeMainById(String barCode) {
        return repository.findByBarcode(barCode);
    }

    @Override
    public HYBarCodeMain saveBarCodeMain(HYBarCodeMain barCodeMain) {
        return repository.save(barCodeMain);
    }

    @Override
    public void deleteBarCodeMain(String barCode) {
        repository.deleteByBarcode(barCode);
    }
}