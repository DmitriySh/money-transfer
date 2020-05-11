package ru.shishmakov.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.persistence.entity.Account;
import ru.shishmakov.persistence.entity.AccountAudit;
import ru.shishmakov.persistence.repository.AccountAuditRepository;
import ru.shishmakov.persistence.repository.AccountRepository;

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
        var account = accountRepository
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
        var account = accountRepository
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
        var first = Math.min(from, to);
        var second = Math.max(from, to);

        var firstAccount = accountRepository.findByAccNumberAndLock(first)
                .orElseThrow(() -> new IllegalArgumentException("not found id: " + first));
        var secondAccount = accountRepository.findByAccNumberAndLock(second)
                .orElseThrow(() -> new IllegalArgumentException("not found id: " + second));
        decreaseAmount(firstAccount.getAccNumber().equals(from) ? firstAccount : secondAccount, amount);
        increaseAmount(firstAccount.getAccNumber().equals(to) ? firstAccount : secondAccount, amount);

        log.debug("transfer amount: {}, accounts: {} -> {} ", amount, from, to);
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
