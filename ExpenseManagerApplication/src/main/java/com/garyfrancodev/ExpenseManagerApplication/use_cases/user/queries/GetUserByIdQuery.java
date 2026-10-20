package com.garyfrancodev.ExpenseManagerApplication.use_cases.user.queries;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import com.garyfrancodev.ExpenseManagerApplication.dtos.user.UserDto;

import java.util.UUID;

public class GetUserByIdQuery implements Command<UserDto> {
    private final UUID userId;

    public GetUserByIdQuery(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UserDto execute(Pipeline pipeline) {
        return pipeline.send(this);
    }
}
