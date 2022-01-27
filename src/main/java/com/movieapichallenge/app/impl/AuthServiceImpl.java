package com.movieapichallenge.app.impl;

import com.movieapichallenge.app.entity.ERole;
import com.movieapichallenge.app.entity.Role;
import com.movieapichallenge.app.entity.User;
import com.movieapichallenge.app.dto.request.LoginRequest;
import com.movieapichallenge.app.dto.request.SingUpRequest;
import com.movieapichallenge.app.dto.response.JwtResponse;
import com.movieapichallenge.app.dto.response.MessageResponse;
import com.movieapichallenge.app.repository.RoleRepository;
import com.movieapichallenge.app.repository.UserRepository;
import com.movieapichallenge.app.security.jwt.JwtUtils;
import com.movieapichallenge.app.service.AuthService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${app.sendgrid.api-key}")
    private String appKey;

    @Override
    public ResponseEntity<?> registerUser(SingUpRequest singUpRequest) {
        if(userRepository.existsByUsername(singUpRequest.getUsername())){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if(userRepository.existsByEmail(singUpRequest.getEmail())){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error: email is already in use!"));
        }

        User user = User.builder()
                .username(singUpRequest.getUsername())
                .email(singUpRequest.getEmail())
                .password(encoder.encode(singUpRequest.getPassword()))
                .build();

        Set<String> strRoles = singUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null){
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: role is not found"));
            roles.add(userRole);
        }else{
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: role is not found"));
                        roles.add(adminRole);
                        break;

                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        Email from = new Email("hello.movieapi@hotmail.com");
        String subject = "Thanks for joining Disney´s movie and series API!";
        Email to = new Email(user.getEmail());
        Content content = new Content("text/html", "<h1>Hi There!</h1></br></br>Thanks so much for joining this movie API to explore Disney´s world movies and series—we're thrilled to have you! ");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(appKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("User registered successfully"));

    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
}
