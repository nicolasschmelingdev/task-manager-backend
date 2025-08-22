package com.portfolio.taskmanager.web;

import com.portfolio.taskmanager.support.OracleTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRegistrationIT extends OracleTestContainer {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    @Test
    void should_register_user_with_encrypted_password() {
        String username = "user_" + UUID.randomUUID();
        Map<String, Object> payload = Map.of(
                "username", username,
                "password", "123456",
                "role", "USER"
        );

        ResponseEntity<Map> resp = rest.postForEntity(url("/api/users"), payload, Map.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resp.getHeaders().getLocation()).isNotNull();

        Map body = resp.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("id")).isNotNull();
        assertThat(body.get("username")).isEqualTo(username);
        assertThat(body.get("role")).isEqualTo("USER");

        // Try to login with the created user
        Map<String, String> login = Map.of("username", username, "password", "123456");
        ResponseEntity<Map> loginResp = rest.postForEntity(url("/api/auth/login"), login, Map.class);
        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResp.getBody()).isNotNull();
        assertThat(loginResp.getBody().get("accessToken")).isNotNull();
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }
}
