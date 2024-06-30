package org.example.ejournal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.RoleType;

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

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @OneToOne(mappedBy = "userAuthentication", targetEntity = User.class)
    private User user;
}
