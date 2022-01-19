package com.movieapichallenge.app.payloads.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SingUpRequest {

    @Column(nullable = false)
    @NotBlank(message = "The username cannot be blank")
    @Size(max = 100, message = "The username must contain a maximum of 100 characters")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "The email cannot be blank")
    @Size(max = 100, message = "The email must contain a maximum of 100 characters")
    @Email
    private String email;

    private Set<String> role;

    @Column(nullable = false)
    @NotBlank(message = "The password cannot be blank")
    @Size(max = 100, message = "The password must contain a maximum of 100 characters")
    private String password;
}
