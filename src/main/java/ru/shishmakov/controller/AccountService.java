package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.dao.AccountRepository;
import ru.shishmakov.model.Account;

import java.math.BigDecimal;
import java.time.Instant;
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

    @Transactional(rollbackFor = Exception.class)
    public void withdraw(long number, BigDecimal amount) {
        log.debug("withdraw account: {}, amount: {}", number, amount);
        // TODO: 30.07.2018 impl
    }

    @Transactional(rollbackFor = Exception.class)
    public void deposit(long number, BigDecimal amount) {
        log.debug("deposit account: {}, amount: {}", number, amount);
        // TODO: 30.07.2018 impl
    }

    @Transactional(rollbackFor = Exception.class)
    public void transfer(long number1, long number2, BigDecimal amount) {
        long from = number1 < number2 ? number1 : number2;
        long to = number1 < number2 ? number2 : number1;

        Account fromAccount = repository.findByAccNumberAndLock(from).orElseThrow(() -> new IllegalArgumentException("not found id: " + from));
        Account toAccount = repository.findByAccNumberAndLock(to).orElseThrow(() -> new IllegalArgumentException("not found id: " + to));
        decreaseAmount(amount, fromAccount);
        increaseAmount(amount, toAccount);
        checkAmount(fromAccount);

        repository.save(fromAccount);
        repository.save(toAccount);
        log.debug("transfer amount: {}, accounts: {} -> {} ", amount, from, to);
    }

    private static void increaseAmount(BigDecimal amount, Account account) {
        account.setAmount(account.getAmount().add(amount));
        account.setLastUpdate(Instant.now());
    }

    private static void decreaseAmount(BigDecimal amount, Account account) {
        account.setAmount(account.getAmount().subtract(amount));
        account.setLastUpdate(Instant.now());
    }

    private static void checkAmount(Account fromAccount) {
        if (BigDecimal.ZERO.compareTo(fromAccount.getAmount()) > 0) throw new IllegalArgumentException();
    }
}
