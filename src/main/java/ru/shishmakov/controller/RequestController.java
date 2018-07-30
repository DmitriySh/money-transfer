package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.shishmakov.dao.AccountRepository;
import ru.shishmakov.model.Account;
import ru.shishmakov.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

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
    }

    @GetMapping("/account")
    public List<Account> getAccount() {
        return repository.findAll();
    }

    @GetMapping("/account/{id}")
    public Account getAccount(@PathVariable("id") long id) {
        return repository.findById(id);
    }

    @PutMapping("/account/{id}/put")
    public void putUp(@PathVariable("id") long id) {

    }

    @PutMapping("/account/{id}/withdraw")
    public void withdraw(@RequestBody Transfer transfer) {

    }
}
