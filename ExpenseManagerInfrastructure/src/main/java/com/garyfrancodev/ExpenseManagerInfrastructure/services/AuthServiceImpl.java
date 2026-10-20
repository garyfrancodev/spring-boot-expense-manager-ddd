package com.garyfrancodev.ExpenseManagerInfrastructure.services;

import com.garyfrancodev.ExpenseManagerDomain.model.User;
import com.garyfrancodev.ExpenseManagerDomain.services.AuthResult;
import com.garyfrancodev.ExpenseManagerDomain.services.AuthService;
import com.garyfrancodev.ExpenseManagerInfrastructure.jwt.JwtService;
import com.garyfrancodev.ExpenseManagerInfrastructure.model.UserJpaModel;
import com.garyfrancodev.ExpenseManagerInfrastructure.repositories.user.UserJpaRepository;
import com.garyfrancodev.ExpenseManagerInfrastructure.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserJpaRepository userRepository;

    @Override
    public AuthResult login(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        UserJpaModel userDetails = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        try {
            User user = new User(
                    userDetails.getFirstName(),
                    userDetails.getLastName(),
                    userDetails.getEmail(),
                    userDetails.getPassword(),
                    userDetails.getId()
            );
            String token = jwtService.getToken(user);
            return new AuthResult(token, userDetails.getId(), userDetails.getEmail());
        } catch (Exception e) {
            throw new RuntimeException("Error during user authentication: " + e.getMessage(), e);
        }
    }

    @Override
    public String register(User user) {
        UserJpaModel userJpaModel = UserUtils.mapToJpaEntity(user);
        userJpaModel.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userJpaModel);

        return jwtService.getToken(user);
    }
}
