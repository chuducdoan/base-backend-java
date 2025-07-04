package com.example.myshop.entity;

import com.example.myshop.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "type")
    private String type;

    @Column(name = "role")
    private String role;

    @Column(name = "is_verify")
    private Integer is_verify;

    public UserEntity( String username, String password, String fullName,
                       String email, Integer isActive, String type, String role, Integer is_verify) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.isActive = isActive;
        this.type = type;
        this.role = role;
        this.is_verify = is_verify;
    }
}
