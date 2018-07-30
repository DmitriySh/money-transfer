package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.shishmakov.dao.AccountRepository;
import ru.shishmakov.model.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository repository;

    public List<Account> getAccounts() {
        log.debug("get all accounts");
        return repository.findAll();
    }

    public Optional<Account> getAccount(long id) {
        log.debug("get: {}", id);
        return repository.findById(id);
    }

    public void withdraw(long from, BigDecimal amount) {
        log.debug("withdraw account: {}, amount: {}", from, amount);
        // TODO: 30.07.2018 impl
    }

    public void deposit(long to, BigDecimal amount) {
        log.debug("deposit account: {}, amount: {}", to, amount);
        // TODO: 30.07.2018 impl
    }

    public void transfer(long from, long to, BigDecimal amount) {
        log.debug("transfer amount: {}, accounts: {} -> {} ", amount, from, to);
        // TODO: 30.07.2018 impl
    }
}
