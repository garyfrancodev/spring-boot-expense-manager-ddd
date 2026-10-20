package com.garyfrancodev.ExpenseManagerInfrastructure.repositories.account;

import com.garyfrancodev.ExpenseManagerDomain.model.Account;
import com.garyfrancodev.ExpenseManagerDomain.repositories.AccountRepository;
import com.garyfrancodev.ExpenseManagerInfrastructure.model.AccountJpaModel;
import com.garyfrancodev.ExpenseManagerInfrastructure.model.UserJpaModel;
import com.garyfrancodev.ExpenseManagerInfrastructure.repositories.user.UserJpaRepository;
import com.garyfrancodev.ExpenseManagerInfrastructure.utils.AccountUtils;
import com.garyfrancodev.ExpenseManagerSharedKernel.core.BusinessRuleValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    @Autowired
    private AccountJpaRepository _accountJpaRepository;
    @Autowired
    private UserJpaRepository _userJpaRepository;

    @Override
    public UUID create(Account account) {
        AccountJpaModel accountJpaModel = new AccountJpaModel();
        accountJpaModel.setId(account.getId());
        accountJpaModel.setAccountName(account.getAccountName());
        accountJpaModel.setDescription(account.getDescription());
        accountJpaModel.setAmount(account.getAmount());
        accountJpaModel.setUser(_userJpaRepository.findById(account.getUserId()).orElseThrow());

        return _accountJpaRepository.save(accountJpaModel).getId();
    }

    @Override
    public Account getAccountById(UUID id) {
        AccountJpaModel accountJpaModel = _accountJpaRepository.findById(id).orElseThrow();
        try {
            return new Account(
                    accountJpaModel.getAccountName(),
                    accountJpaModel.getDescription(),
                    accountJpaModel.getAmount(),
                    accountJpaModel.getUser().getId(),
                    accountJpaModel.getId()
            );
        } catch (BusinessRuleValidationException e) {
            throw new RuntimeException("Error mapping account from database: " + e.getMessage(), e);
        }
    }

    @Override
    public UUID update(Account account) {
        AccountJpaModel accountJpaModel = _accountJpaRepository.findById(account.getId()).orElseThrow();
        accountJpaModel.setAmount(account.getAmount());

        return _accountJpaRepository.save(accountJpaModel).getId();
    }

    @Override
    public Double GetAmountTotalByUserId(UUID userId) {
        return getAmountTotalByUserId(userId);
    }

    @Override
    public Double getAmountTotalByUserId(UUID userId) {
        UserJpaModel userJpaModel = _userJpaRepository.findById(userId).orElseThrow();
        Double total = _accountJpaRepository.total(userJpaModel);
        return total != null ? total : 0.0;
    }

    @Override
    public List<Account> findAccountsByUserId(UUID userId) {
        UserJpaModel userJpaModel = _userJpaRepository.findById(userId).orElseThrow();
        List<AccountJpaModel> accountJpaModels = _accountJpaRepository.get(userJpaModel);
        return accountJpaModels.stream().map(AccountUtils::jpaModelToDomainModel).toList();
    }
}
