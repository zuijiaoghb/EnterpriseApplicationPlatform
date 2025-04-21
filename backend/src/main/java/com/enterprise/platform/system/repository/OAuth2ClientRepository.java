package com.enterprise.platform.system.repository;

import com.enterprise.platform.system.model.OAuth2Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OAuth2ClientRepository extends JpaRepository<OAuth2Client, String> {
    
    List<OAuth2Client> findByActiveTrue();
    
    @Modifying
    @Query("UPDATE OAuth2Client c SET c.active = :active WHERE c.clientId = :clientId")
    int updateActiveStatus(@Param("clientId") String clientId, @Param("active") boolean active);
}
