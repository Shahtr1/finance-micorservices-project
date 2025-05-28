package com.finance.user.controller;

import com.finance.user.dto.LoginRequest;
import com.finance.user.dto.LoginResponse;
import com.finance.user.dto.RegisterRequest;
import com.finance.user.entity.Role;
import com.finance.user.entity.User;
import com.finance.user.repository.RoleRepository;
import com.finance.user.repository.UserRepository;
import com.finance.user.security.CustomUserDetails;
import com.finance.user.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authManager,
                          UserRepository userRepo,
                          RoleRepository roleRepo,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider) {
        this.authManager = authManager;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepo.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (userRepo.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> userRoles = new HashSet<>();
        for (String roleName : request.getRoles()) {
            Role role = roleRepo.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            userRoles.add(role);
        }
        user.setRoles(userRoles);
        userRepo.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String token = jwtTokenProvider.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
