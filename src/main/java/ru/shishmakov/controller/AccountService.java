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

    public Optional<Account> getAccount(long accNumber) {
        log.debug("get account: {}", accNumber);
        return repository.findByAccNumber(accNumber);
    }

    @Transactional(rollbackFor = Exception.class)
    public Account withdraw(long number, BigDecimal amount) {
        checkAmount(amount);
        Account account = repository.findByAccNumberAndLock(number).orElseThrow(() -> new IllegalArgumentException("not found id: " + number));
        decreaseAmount(account, amount);

        log.debug("withdraw account: {}, amount: {}", number, amount);
        return repository.save(account);
    }

    @Transactional(rollbackFor = Exception.class)
    public Account deposit(long number, BigDecimal amount) {
        checkAmount(amount);
        Account account = repository.findByAccNumberAndLock(number).orElseThrow(() -> new IllegalArgumentException("not found id: " + number));
        increaseAmount(account, amount);

        log.debug("deposit account: {}, amount: {}", number, amount);
        return repository.save(account);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Account> transfer(long from, long to, BigDecimal amount) {
        checkAmount(amount);
        long first = from < to ? from : to;
        long second = from < to ? to : from;

        Account firstAccount = repository.findByAccNumberAndLock(first)
                .orElseThrow(() -> new IllegalArgumentException("not found id: " + first));
        Account secondAccount = repository.findByAccNumberAndLock(second)
                .orElseThrow(() -> new IllegalArgumentException("not found id: " + second));
        decreaseAmount(firstAccount.getAccNumber().equals(from) ? firstAccount : secondAccount, amount);
        increaseAmount(firstAccount.getAccNumber().equals(to) ? firstAccount : secondAccount, amount);

        log.debug("transfer amount: {}, accounts: {} -> {} ", amount, from, to);
        return repository.saveAll(List.of(firstAccount, secondAccount));
    }

    private static void increaseAmount(Account account, BigDecimal amount) {
        account.setAmount(account.getAmount().add(amount));
        account.setLastUpdate(Instant.now());
    }

    private static void decreaseAmount(Account account, BigDecimal amount) {
        account.setAmount(account.getAmount().subtract(amount));
        account.setLastUpdate(Instant.now());
    }

    private static void checkAmount(BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) throw new IllegalArgumentException("not positive amount: " + amount);
    }
}
