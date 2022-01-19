package com.movieapichallenge.app.payloads.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @Column(nullable = false)
    @NotBlank(message = "The username cannot be blank")
    @Size(max = 100, message = "The username must contain a maximum of 100 characters")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "The password name cannot be blank")
    @Size(max = 100, message = "The password must contain a maximum of 100 characters")
    private String password;

}
