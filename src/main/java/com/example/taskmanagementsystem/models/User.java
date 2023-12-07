package com.example.taskmanagementsystem.models;

import com.example.taskmanagementsystem.models.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private boolean enabled;
    @Column(name = "expiration_date")
    private Date expirationDate;
    @Column(name = "account_locked")
    private boolean accountLocked;
    @Column(name = "credentials_expired")
    private boolean credentialsExpired;
    @NotNull
    @Column(name = "is_moderator", nullable = false)
    private byte isModerator;

    public Role getRole(){
        return isModerator == 1? Role.ADMIN : Role.USER;
    }
}

