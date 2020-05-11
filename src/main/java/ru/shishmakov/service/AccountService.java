package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.dao.AccountRepository;
import ru.shishmakov.dao.AccountAuditRepository;
import ru.shishmakov.model.Account;
import ru.shishmakov.model.AccountAudit;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountAuditRepository accountAuditRepository;

    public List<Account> getAccounts() {
        log.debug("get all accounts");
        return accountRepository.findAll();
    }

    public List<AccountAudit> getAccountAudits() {
        log.debug("get audit records");
        return accountAuditRepository.findAll();
    }

    public Optional<Account> getAccount(long accNumber) {
        log.debug("get account: {}", accNumber);
        return accountRepository.findByAccNumber(accNumber);
    }

    @Transactional(rollbackFor = Exception.class)
    public Account withdraw(long number, BigDecimal amount) {
        checkAmount(amount);
        Account account = accountRepository
                .findByAccNumberAndLock(number)
                .orElseThrow(() -> new IllegalArgumentException("not found id: " + number));
        decreaseAmount(account, amount);

        log.debug("withdraw account: {}, amount: {}", number, amount);
        accountAuditRepository.save(AccountAudit.builder()
                .fromNumber(number).amount(amount)
                .description("withdraw").build());
        return accountRepository.save(account);
    }

    @Transactional(rollbackFor = Exception.class)
    public Account deposit(long number, BigDecimal amount) {
        checkAmount(amount);
        Account account = accountRepository
                .findByAccNumberAndLock(number)
                .orElseThrow(() -> new IllegalArgumentException("not found id: " + number));
        increaseAmount(account, amount);

        log.debug("deposit account: {}, amount: {}", number, amount);
        accountAuditRepository.save(AccountAudit.builder()
                .toNumber(number).amount(amount)
                .description("deposit").build());
        return accountRepository.save(account);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Account> transfer(long from, long to, BigDecimal amount) {
        checkAmount(amount);
        long first = Math.min(from, to);
        long second = Math.max(from, to);

        Account firstAccount = accountRepository.findByAccNumberAndLock(first)
                .orElseThrow(() -> new IllegalArgumentException("not found id: " + first));
        Account secondAccount = accountRepository.findByAccNumberAndLock(second)
                .orElseThrow(() -> new IllegalArgumentException("not found id: " + second));
        decreaseAmount(firstAccount.getAccNumber().equals(from) ? firstAccount : secondAccount, amount);
        increaseAmount(firstAccount.getAccNumber().equals(to) ? firstAccount : secondAccount, amount);

        AccountService.log.debug("transfer amount: {}, accounts: {} -> {} ", amount, from, to);
        accountAuditRepository.save(AccountAudit.builder()
                .fromNumber(from).toNumber(to).amount(amount)
                .description("transfer").build());
        return accountRepository.saveAll(List.of(firstAccount, secondAccount));
    }

    private void increaseAmount(Account account, BigDecimal amount) {
        account.setAmount(account.getAmount().add(amount));
    }

    private void decreaseAmount(Account account, BigDecimal amount) {
        account.setAmount(account.getAmount().subtract(amount));
    }

    private static void checkAmount(BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) throw new IllegalArgumentException("not positive amount: " + amount);
    }
}
