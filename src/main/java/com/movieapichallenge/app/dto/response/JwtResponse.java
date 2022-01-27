package com.movieapichallenge.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {

    private String token;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

}
