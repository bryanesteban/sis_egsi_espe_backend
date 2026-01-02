package com.espe.ListoEgsi.domain.model.entity.setting;

import java.util.UUID;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idUser;

    @Column(name = "name")
    @Size(max = 50)
    private String name;

    @Column(name = "lastname")
    @Size(max = 50)
    private String lastname;

    @Column(name = "ci")
    @Size(max = 50)
    private String ci;

    @Column(name = "username" , unique = true)
    @Size(max = 50)
    private String username;

    @Column(name = "password")
    @Size(max = 200)
    private String password;

    @Column(name = "rol")
    @Size(max = 50)
    private String rol;

    @Column(name = "is_deleted")
    private boolean isDeleted;

}
