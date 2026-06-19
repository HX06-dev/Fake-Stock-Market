package com.hx06.stockmarket.service;

import com.hx06.stockmarket.dto.AuthResponse;
import com.hx06.stockmarket.dto.RegisterRequest;
import com.hx06.stockmarket.model.Account;
import com.hx06.stockmarket.model.User;
import com.hx06.stockmarket.repository.AccountRepository;
import com.hx06.stockmarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AccountRepository accountRepository;

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        userRepository.save(user);

        Account account = new Account();
        account.setUser(user);
        accountRepository.save(account);

        return new AuthResponse(jwtService.generateToken(req.getEmail()));
    }

    public AuthResponse login(RegisterRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Incorrect password");
        }
        return new AuthResponse(jwtService.generateToken(req.getEmail()));
    }
}
