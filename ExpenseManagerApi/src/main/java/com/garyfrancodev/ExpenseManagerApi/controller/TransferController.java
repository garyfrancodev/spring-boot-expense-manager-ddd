package com.garyfrancodev.ExpenseManagerApi.controller;

import an.awesome.pipelinr.Pipeline;
import com.garyfrancodev.ExpenseManagerApplication.dtos.transfer.CreateTransferDto;
import com.garyfrancodev.ExpenseManagerApplication.dtos.transfer.TransferDto;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.transfer.command.CreateTransferCommand;
import com.garyfrancodev.ExpenseManagerApplication.use_cases.transfer.queries.GetTransfersByUserIdQuery;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Transfer Controller")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    @Autowired
    private Pipeline pipeline;

    @GetMapping()
    public ResponseEntity<List<TransferDto>> getTransferByUserId(@Parameter(description = "User UUID") @RequestParam String userId) {
        GetTransfersByUserIdQuery getTransfersByUserIdQuery = new GetTransfersByUserIdQuery(UUID.fromString(userId));
        List<TransferDto> list = getTransfersByUserIdQuery.execute(pipeline);
        return ResponseEntity.ok(list);
    }

    @PostMapping()
    public ResponseEntity<UUID> createTransfer(@RequestBody CreateTransferDto createTransferDto) {
        CreateTransferCommand transferCommand = new CreateTransferCommand(createTransferDto);
        UUID transferId = transferCommand.execute(pipeline);
        return ResponseEntity.ok(transferId);
    }
}
