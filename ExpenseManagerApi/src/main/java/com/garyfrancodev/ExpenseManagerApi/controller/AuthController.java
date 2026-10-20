package com.garyfrancodev.ExpenseManagerApi.controller;

import an.awesome.pipelinr.Pipeline;
import com.garyfrancodev.ExpenseManagerApplication.dtos.user.CreateUserDto;
import com.garyfrancodev.ExpenseManagerApplication.dtos.user.LoginDto;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.user.CreateUserCommand;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.user.LoginUserCommand;
import com.garyfrancodev.ExpenseManagerDomain.services.AuthResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Auth Controller")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private Pipeline pipeline;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDto loginDto) {
        LoginUserCommand loginUserCommand = new LoginUserCommand(loginDto);
        AuthResult authResult = loginUserCommand.execute(pipeline);

        Map<String, String> body = new HashMap<>();
        body.put("jwt", authResult.getToken());
        body.put("userId", authResult.getUserId().toString());
        body.put("email", authResult.getEmail());
        return ResponseEntity.ok(body);
    }

    @PostMapping("/register")
    public ResponseEntity<UUID> createUser(@RequestBody CreateUserDto createUserDto) {
        CreateUserCommand createUserCommand = new CreateUserCommand(createUserDto);
        UUID userId = createUserCommand.execute(pipeline);
        return ResponseEntity.ok(userId);
    }
}
