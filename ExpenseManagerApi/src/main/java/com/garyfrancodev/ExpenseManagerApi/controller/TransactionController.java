package com.garyfrancodev.ExpenseManagerApi.controller;

import an.awesome.pipelinr.Pipeline;
import com.garyfrancodev.ExpenseManagerApplication.dtos.transaction.CreateTransactionDto;
import com.garyfrancodev.ExpenseManagerApplication.dtos.transaction.TransactionDto;
import com.garyfrancodev.ExpenseManagerApplication.dtos.transaction.UpdateTransactionDto;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.transaction.command.CreateTransactionCommand;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.transaction.command.UpdateTransactionCommand;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.transaction.queries.GetTransactionsByUserIdByPeriodQuery;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.transaction.queries.GetTransactionsByUserIdQuery;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Tag(name = "Transaction Controller")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private Pipeline pipeline;

    @PostMapping()
    public ResponseEntity<UUID> create(@RequestBody CreateTransactionDto createTransactionDto) {
        CreateTransactionCommand createTransactionCommand = new CreateTransactionCommand(createTransactionDto);
        UUID transactionId = createTransactionCommand.execute(pipeline);
        return ResponseEntity.ok(transactionId);
    }

    @PutMapping()
    public ResponseEntity<UUID> update(@RequestBody UpdateTransactionDto updateTransactionDto) {
        UpdateTransactionCommand updateTransactionCommand = new UpdateTransactionCommand(updateTransactionDto);
        UUID transactionId = updateTransactionCommand.execute(pipeline);
        return ResponseEntity.ok(transactionId);
    }

    @GetMapping()
    public ResponseEntity<List<TransactionDto>> getTransactionsByUserId(@Parameter(description = "User UUID") @RequestParam String userId) {
        GetTransactionsByUserIdQuery getTransactionsByUserIdQuery = new GetTransactionsByUserIdQuery(UUID.fromString(userId));
        List<TransactionDto> list = getTransactionsByUserIdQuery.execute(pipeline);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/period")
    public ResponseEntity<List<TransactionDto>> getTransactionsByUserIdByPeriod(
            @Parameter(description = "User UUID") @RequestParam String userId,
            @Parameter(description = "Start Date ISO") @RequestParam String startDate,
            @Parameter(description = "End Date ISO") @RequestParam String endDate) {
        Date start = convertToDate(LocalDateTime.ofInstant(Instant.parse(startDate), ZoneId.systemDefault()));
        Date end = convertToDate(LocalDateTime.ofInstant(Instant.parse(endDate), ZoneId.systemDefault()));

        GetTransactionsByUserIdByPeriodQuery query = new GetTransactionsByUserIdByPeriodQuery(UUID.fromString(userId), start, end);
        List<TransactionDto> list = query.execute(pipeline);
        return ResponseEntity.ok(list);
    }

    private static Date convertToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }
}
