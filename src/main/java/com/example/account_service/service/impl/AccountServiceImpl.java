package com.example.account_service.service.impl;

import com.example.account_service.domain.Account;
import com.example.account_service.repository.AccountRepo;
import com.example.account_service.service.AccountService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo;
    private final AccountServiceImpl accountService;

    public AccountServiceImpl(AccountRepo accountRepo, @Lazy AccountServiceImpl accountService) {
        this.accountRepo = accountRepo;
        this.accountService = accountService;
    }

    @Override
    public Long getAmount(Integer id) {
        Account account = accountService.findById(id);
        if(account == null) {
            throw new EntityNotFoundException("There is no account with such id");
        }
        return account.getAmount();
    }

    @Override
    public void addAmount(Integer id, Long value) {
        Account account = accountService.findById(id);
        if(account == null) {
           throw new EntityNotFoundException("There is no account with such id");
        }
        accountService.update(account, value);
    }

    @CachePut(cacheNames = "accounts", key = "#id", unless="#result==null")
    public Account save(Integer id) {
        return accountRepo.save(new Account(id, 0L));
    }

    @CachePut(cacheNames = "accounts", key = "#account.id", unless="#result==null")
    public Account update(Account account, Long value) {
        account.setAmount(account.getAmount() + value);
        return accountRepo.save(account);
    }

    @Cacheable(cacheNames = "accounts", key = "#id")
    public Account findById(Integer id) {
        return accountRepo.findById(id).orElse(null);
    }
}
