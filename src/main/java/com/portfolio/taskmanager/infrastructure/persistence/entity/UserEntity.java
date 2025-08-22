package com.portfolio.taskmanager.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_username", columnNames = {"username"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "username", length = 100, nullable = false)
    private String username;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Column(name = "role", length = 50, nullable = false)
    private String role = "USER";

}
