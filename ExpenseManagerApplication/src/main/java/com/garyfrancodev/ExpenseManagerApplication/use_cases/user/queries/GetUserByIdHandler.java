package com.garyfrancodev.ExpenseManagerApplication.use_cases.user.queries;

import an.awesome.pipelinr.Command;
import com.garyfrancodev.ExpenseManagerApplication.dtos.user.UserDto;
import com.garyfrancodev.ExpenseManagerDomain.model.User;
import com.garyfrancodev.ExpenseManagerDomain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetUserByIdHandler implements Command.Handler<GetUserByIdQuery, UserDto> {

    private final UserRepository userRepository;

    @Autowired
    public GetUserByIdHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto handle(GetUserByIdQuery query) {
        User user = userRepository.findId(query.getUserId());
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
