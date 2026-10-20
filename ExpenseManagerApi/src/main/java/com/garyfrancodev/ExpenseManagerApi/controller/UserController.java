package com.garyfrancodev.ExpenseManagerApi.controller;

import an.awesome.pipelinr.Pipeline;
import com.garyfrancodev.ExpenseManagerApplication.dtos.user.UserDto;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.user.queries.GetUserByIdQuery;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "User Controller")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private Pipeline pipeline;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@Parameter(description = "User UUID") @PathVariable String id) {
        GetUserByIdQuery query = new GetUserByIdQuery(UUID.fromString(id));
        UserDto user = query.execute(pipeline);
        return ResponseEntity.ok(user);
    }
}
