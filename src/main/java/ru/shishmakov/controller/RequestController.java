package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shishmakov.dao.AccountRepository;
import ru.shishmakov.model.Account;
import ru.shishmakov.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RequestController {
    private final AccountRepository repository;

    @GetMapping()
    public String hello() {
        return "RESTfull API for money transfer";
    }

    @PutMapping("/accounts/transfer")
    public void transfer(@RequestBody Transfer transfer) {
        long from = transfer.getFrom();
        long to = transfer.getTo();
        BigDecimal amount = transfer.getAmount();
        log.debug("transfer:{}, {} -> {} ", amount, from, to);
        // TODO: 30.07.2018 impl
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        log.debug("get all accounts");
        return new ResponseEntity<>(repository.findAll(), OK);
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable("id") long id) {
        log.debug("get account: {}", id);
        return repository.findById(id)
                .map(a -> new ResponseEntity<>(a, OK))
                .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }

    @PutMapping("/account/deposit")
    public void deposit(@RequestBody Transfer transfer) {
        log.debug("put account: {}, amount: {}", transfer.getTo(), transfer.getAmount());
        // TODO: 30.07.2018 impl
    }

    @PutMapping("/account/withdraw")
    public void withdraw(@RequestBody Transfer transfer) {
        log.debug("withdraw account: {}, amount: {}", transfer.getFrom(), transfer.getAmount());
        // TODO: 30.07.2018 impl
    }
}
