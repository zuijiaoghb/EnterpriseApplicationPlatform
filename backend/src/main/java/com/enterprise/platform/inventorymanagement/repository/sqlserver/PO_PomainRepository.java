package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.platform.inventorymanagement.model.sqlserver.PO_Pomain;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PO_PomainRepository extends JpaRepository<PO_Pomain, Integer> {
    PO_Pomain findBycPOID(String cPOID);
    @Query(value = "WITH DistinctPOs AS (" +
        "SELECT DISTINCT p.* " +
        "FROM PO_Pomain p " +
        "INNER JOIN PO_Podetails pd ON p.POID = pd.POID " +
        "INNER JOIN inventory i ON pd.cInvCode = i.cInvCode " +
        "WHERE p.cVenCode = :cVenCode " +
        "AND p.cAuditDate IS NOT NULL " +
        "AND p.cState != 2 " +
        "AND (COALESCE(:cPOID, '') = '' OR p.cPOID LIKE '%' + COALESCE(:cPOID, '') + '%') " +
        "AND (COALESCE(:dPODate, '') = '' OR CONVERT(VARCHAR, p.dPODate, 23) LIKE '%' + COALESCE(:dPODate, '') + '%') " +
        "AND (COALESCE(:cInvCode, '') = '' OR pd.cInvCode = COALESCE(:cInvCode, '')) " +
        "AND (COALESCE(:cItemName, '') = '' OR i.cInvName LIKE '%' + COALESCE(:cItemName, '') + '%') " +
        "AND isnull(pd.iQuantity, 0) != isnull(pd.iReceivedQTY, 0) " +
        ") " +
        "SELECT * FROM DistinctPOs ORDER BY dPODate DESC", nativeQuery = true)
    List<PO_Pomain> findAllByCVenCodeAndCAuditDateIsNotNullAndCPOIDLikeAndDPODateLikeAndCInvCodeLikeAndCItemNameLike(
        @Param("cVenCode") String cVenCode,
        @Param("cPOID") String cPOID,
        @Param("dPODate") String dPODate,
        @Param("cInvCode") String cInvCode,
        @Param("cItemName") String cItemName);

    @Query(value = "WITH DistinctPOs AS (" +
        "SELECT DISTINCT p.* " +
        "FROM PO_Pomain p " +
        "INNER JOIN PO_Podetails pd ON p.POID = pd.POID " +
        "INNER JOIN inventory i ON pd.cInvCode = i.cInvCode " +
        "WHERE p.cVenCode = :cVenCode " +
        "AND p.cAuditDate IS NOT NULL " +
        "AND p.cState != 2 " +
        "AND (COALESCE(:cPOID, '') = '' OR p.cPOID LIKE '%' + COALESCE(:cPOID, '') + '%') " +
        "AND (COALESCE(:dPODate, '') = '' OR CONVERT(VARCHAR, p.dPODate, 23) LIKE '%' + COALESCE(:dPODate, '') + '%') " +
        "AND (COALESCE(:cInvCode, '') = '' OR pd.cInvCode = COALESCE(:cInvCode, '')) " +
        "AND (COALESCE(:cItemName, '') = '' OR i.cInvName LIKE '%' + COALESCE(:cItemName, '') + '%') " +
        "AND isnull(pd.iQuantity, 0) != isnull(pd.iReceivedQTY, 0) " +
        "), " +
        "FilteredData AS (" +
        "SELECT *, ROW_NUMBER() OVER (ORDER BY dPODate DESC) AS RowNum " +
        "FROM DistinctPOs" +
        ") " +
        "SELECT * FROM FilteredData " +
        "WHERE RowNum > :offset AND RowNum <= :offset + :pageSize", nativeQuery = true)
    List<PO_Pomain> findByCVenCodeAndCAuditDateIsNotNullAndCPOIDLikeAndDPODateLikeAndCInvCodeLikeAndCItemNameLike(
        @Param("cVenCode") String cVenCode,
        @Param("cPOID") String cPOID,
        @Param("dPODate") String dPODate,
        @Param("cInvCode") String cInvCode,
        @Param("cItemName") String cItemName,
        @Param("offset") int offset,
        @Param("pageSize") int pageSize);

    @Query(value = "SELECT COUNT(DISTINCT pd.ID) " +
            "FROM PO_Pomain p " +
            "INNER JOIN PO_Podetails pd ON p.POID = pd.POID " +
            "INNER JOIN inventory i ON pd.cInvCode = i.cInvCode " +
            "WHERE p.cVenCode = :cVenCode " +
            "AND p.cAuditDate IS NOT NULL " +
            "AND p.cState != 2 " +
            "AND (:cPOID IS NULL OR p.cPOID LIKE '%' + :cPOID + '%') " +
            "AND (:dPODate IS NULL OR CONVERT(VARCHAR, p.dPODate, 23) LIKE '%' + :dPODate + '%') " +
            "AND (:cInvCode IS NULL OR pd.cInvCode = :cInvCode) " +
            "AND (:cItemName IS NULL OR i.cInvName LIKE '%' + :cItemName + '%') " +
            "AND isnull(pd.iQuantity, 0) != isnull(pd.iReceivedQTY, 0)", nativeQuery = true)
    long countByCVenCodeAndCAuditDateIsNotNullAndCPOIDLikeAndDPODateLikeAndCInvCodeLikeAndCItemNameLike(
        @Param("cVenCode") String cVenCode,
        @Param("cPOID") String cPOID,
        @Param("dPODate") String dPODate,
        @Param("cInvCode") String cInvCode,
        @Param("cItemName") String cItemName);
}
