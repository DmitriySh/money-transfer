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

    @PutMapping("/transfer")
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

    @PutMapping("/account/{id}/put/{amount}")
    public void putUp(@PathVariable("id") long id, @PathVariable BigDecimal amount) {
        log.debug("put account: {}, amount: {}", id, amount);
        // TODO: 30.07.2018 impl
    }

    @PutMapping("/account/{id}/withdraw/{amount}")
    public void withdraw(@PathVariable("id") long id, @PathVariable BigDecimal amount) {
        log.debug("withdraw account: {}, amount: {}", id, amount);
        // TODO: 30.07.2018 impl
    }
}
