package org.example.ejournal.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_auth")
public class UserAuthentication extends BasicEntity {

    @Column(length = 30, nullable = false)
    private String username;

    @Column(length = 30, nullable = false)
    private String password;
}
