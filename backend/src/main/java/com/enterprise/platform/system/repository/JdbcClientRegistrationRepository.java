package com.enterprise.platform.system.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcClientRegistrationRepository implements ClientRegistrationRepository {
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    public JdbcClientRegistrationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ClientRegistration findByRegistrationId(String clientId) {
        return jdbcTemplate.queryForObject(
            "SELECT client_id, client_secret, scopes FROM oauth2_client WHERE client_id = ? AND active = TRUE",
            (rs, rowNum) -> ClientRegistration.withRegistrationId(clientId)
                .clientId(rs.getString("client_id"))
                .clientSecret(rs.getString("client_secret"))
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenUri("http://192.168.21.175:8081/auth/oauth2/token")
                .scope(rs.getString("scopes").split(","))
                .build(),
            clientId);
    }
}
