package ru.shishmakov.controller;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.dao.AccountRepository;
import ru.shishmakov.dao.LogRepository;
import ru.shishmakov.domain.Account;
import ru.shishmakov.domain.Log;
import ru.shishmakov.web.ArgumentException;
import ru.shishmakov.web.NotFoundException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accRepository;
    private final LogRepository logRepository;

    public List<Account> getAccounts() {
        log.debug("get all accounts");
        return accRepository.findAll();
    }

    public List<Log> getLogRecords() {
        log.debug("get log records");
        return logRepository.findAll();
    }

    public Optional<Account> getAccount(long accNumber) {
        checkNumber(accNumber, "accNumber");
        log.debug("get account: {}", accNumber);
        return accRepository.findByAccNumber(accNumber);
    }

    @Transactional(rollbackFor = Exception.class)
    public Account withdraw(Long from, BigDecimal amount) {
        checkNumber(from, "from");
        checkAmount(amount);
        Account account = accRepository.findByAccNumberAndLock(from).orElseThrow(() -> new NotFoundException("withdraw; not found id: " + from));
        decreaseAmount(account, amount);

        log.debug("withdraw account: {}, amount: {}", from, amount);
        logRepository.save(Log.builder().fromNumber(from).amount(amount).description("withdraw").createDate(Instant.now()).build());
        return accRepository.save(account);
    }

    @Transactional(rollbackFor = Exception.class)
    public Account deposit(Long to, BigDecimal amount) {
        checkNumber(to, "to");
        checkAmount(amount);
        Account account = accRepository.findByAccNumberAndLock(to).orElseThrow(() -> new NotFoundException("deposit; not found id: " + to));
        increaseAmount(account, amount);

        log.debug("deposit account: {}, amount: {}", to, amount);
        logRepository.save(Log.builder().toNumber(to).amount(amount).description("deposit").createDate(Instant.now()).build());
        return accRepository.save(account);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Account> transfer(Long from, Long to, BigDecimal amount) {
        checkNumber(from, "from");
        checkNumber(to, "to");
        checkAmount(amount);
        long first = from < to ? from : to;
        long second = from < to ? to : from;

        Account firstAccount = accRepository.findByAccNumberAndLock(first)
                .orElseThrow(() -> new NotFoundException("transfer; not found id: " + first));
        Account secondAccount = accRepository.findByAccNumberAndLock(second)
                .orElseThrow(() -> new NotFoundException("transfer; not found id: " + second));
        decreaseAmount(firstAccount.getAccNumber().equals(from) ? firstAccount : secondAccount, amount);
        increaseAmount(firstAccount.getAccNumber().equals(to) ? firstAccount : secondAccount, amount);

        AccountService.log.debug("transfer amount: {}, accounts: {} -> {} ", amount, from, to);
        logRepository.save(Log.builder().fromNumber(from).toNumber(to).amount(amount).description("transfer").createDate(Instant.now()).build());
        return accRepository.saveAll(List.of(firstAccount, secondAccount));
    }

    private void increaseAmount(Account account, BigDecimal amount) {
        account.setAmount(account.getAmount().add(amount));
        updateDate(account);
    }

    private void decreaseAmount(Account account, BigDecimal amount) {
        account.setAmount(account.getAmount().subtract(amount));
        updateDate(account);
    }

    @VisibleForTesting
    void updateDate(Account account) {
        account.setLastUpdate(Instant.now());
    }

    private static void checkAmount(BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) throw new ArgumentException("not positive amount: " + amount);
    }

    private void checkNumber(Long number, String desc) {
        Optional.ofNullable(number)
                .filter(n -> n > 0)
                .orElseThrow(() -> new ArgumentException("not positive " + desc + ": " + number));
    }
}
