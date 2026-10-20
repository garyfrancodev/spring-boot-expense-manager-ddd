package com.garyfrancodev.ExpenseManagerApi.controller;

import an.awesome.pipelinr.Pipeline;
import com.garyfrancodev.ExpenseManagerApplication.dtos.account.AccountDto;
import com.garyfrancodev.ExpenseManagerApplication.dtos.account.CreateAccountDto;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.account.command.CreateAccountCommand;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.account.queries.GetAccountsQuery;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.account.queries.GetAmountTotalQuery;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Account Controller")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private Pipeline pipeline;

    @PostMapping()
    public ResponseEntity<UUID> create(@RequestBody CreateAccountDto createAccountDto) {
        CreateAccountCommand createAccountCommand = new CreateAccountCommand(createAccountDto);
        UUID accountId = createAccountCommand.execute(pipeline);
        return ResponseEntity.ok(accountId);
    }

    @GetMapping("/amount-total")
    public ResponseEntity<Double> getAmountTotal(@Parameter(description = "User UUID") @RequestParam String userId) {
        GetAmountTotalQuery getAmountTotalQuery = new GetAmountTotalQuery(UUID.fromString(userId));
        Double total = getAmountTotalQuery.execute(pipeline);
        return ResponseEntity.ok(total);
    }

    @GetMapping()
    public ResponseEntity<List<AccountDto>> getAccounts(@Parameter(description = "User UUID") @RequestParam String userId) {
        GetAccountsQuery getAccountsQuery = new GetAccountsQuery(UUID.fromString(userId));
        List<AccountDto> accounts = getAccountsQuery.execute(pipeline);
        return ResponseEntity.ok(accounts);
    }
}
