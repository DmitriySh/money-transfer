package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shishmakov.model.Account;
import ru.shishmakov.model.AccountAudit;
import ru.shishmakov.model.Transfer;

import java.util.List;
import ru.shishmakov.service.AccountService;


import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountService service;

    @GetMapping
    public String hello() {
        return "RESTfull API for money transfer";
    }

    @GetMapping("/logs")
    public ResponseEntity<List<AccountAudit>> getLogRecords() {
        return new ResponseEntity<>(service.getAccountAudits(), OK);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        return new ResponseEntity<>(service.getAccounts(), OK);
    }

    @PutMapping("/accounts/transfer")
    public ResponseEntity<List<Account>> transfer(@RequestBody Transfer transfer) {
        try {
            List<Account> accounts = service.transfer(requireNonNull(transfer.getFrom()), requireNonNull(transfer.getTo()), transfer.getAmount());
            return new ResponseEntity<>(accounts, OK);
        } catch (Exception e) {
            log.error("transfer error", e);
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    @GetMapping("/account/{accNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable("accNumber") long accNumber) {
        return service.getAccount(accNumber)
                .map(a -> new ResponseEntity<>(a, OK))
                .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }

    @PutMapping("/account/deposit")
    public ResponseEntity<Account> deposit(@RequestBody Transfer transfer) {
        try {
            Account account = service.deposit(requireNonNull(transfer.getTo()), transfer.getAmount());
            return new ResponseEntity<>(account, OK);
        } catch (Exception e) {
            log.error("transfer error", e);
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    @PutMapping("/account/withdraw")
    public ResponseEntity<Account> withdraw(@RequestBody Transfer transfer) {
        try {
            Account account = service.withdraw(requireNonNull(transfer.getFrom()), transfer.getAmount());
            return new ResponseEntity<>(account, OK);
        } catch (Exception e) {
            log.error("transfer error", e);
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }
}
