package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shishmakov.model.Account;
import ru.shishmakov.model.Log;
import ru.shishmakov.model.Transfer;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {
    private final AccountService service;

    @GetMapping
    public String hello() {
        return "RESTfull API for money transfer";
    }

    @GetMapping("/logs")
    public ResponseEntity<List<Log>> getLogRecords() {
        return ResponseEntity.ok(service.getLogRecords());
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.ok(service.getAccounts());
    }

    @PutMapping("/accounts/transfer")
    public ResponseEntity<List<Account>> transfer(@RequestBody Transfer transfer) {
        var accounts = service.transfer(transfer.getFrom(), transfer.getTo(), transfer.getAmount());
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/account/{accNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable("accNumber") Long accNumber) {
        return service.getAccount(accNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }

    @PutMapping("/account/deposit")
    public ResponseEntity<Account> deposit(@RequestBody Transfer transfer) {
        var account = service.deposit(transfer.getTo(), transfer.getAmount());
        return new ResponseEntity<>(account, OK);

    }

    @PutMapping("/account/withdraw")
    public ResponseEntity<Account> withdraw(@RequestBody Transfer transfer) {
        var account = service.withdraw(transfer.getFrom(), transfer.getAmount());
        return ResponseEntity.ok(account);
    }
}
