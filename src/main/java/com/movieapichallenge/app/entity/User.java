package com.movieapichallenge.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "The username cannot be blank")
    @Size(max = 100, message = "The username must contain a maximum of 100 characters")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "The email name cannot be blank")
    @Size(max = 100, message = "The email must contain a maximum of 100 characters")
    @Email(message = "Must be a valid email")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "The password name cannot be blank")
    @Size(max = 100, message = "The password must contain a maximum of 100 characters")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
