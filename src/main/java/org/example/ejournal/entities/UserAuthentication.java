package org.example.ejournal.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
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
public class UserAuthentication extends BaseEntity {

    @Column(length = 30, nullable = false, unique = true)
    private String username;

    @Column(length = 30, nullable = false)
    private String password;

    @OneToOne(mappedBy = "userAuthentication", targetEntity = User.class)
    private User user;
}
