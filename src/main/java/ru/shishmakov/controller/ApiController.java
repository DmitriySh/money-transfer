package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shishmakov.model.Account;
import ru.shishmakov.model.Transfer;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {
    private final AccountService service;

    @GetMapping()
    public String hello() {
        return "RESTfull API for money transfer";
    }

    @PutMapping("/accounts/transfer")
    public void transfer(@RequestBody Transfer transfer) {
        service.transfer(requireNonNull(transfer.getFrom()),
                requireNonNull(transfer.getTo()),
                requireNonNull(transfer.getAmount()));
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        return new ResponseEntity<>(service.getAccounts(), OK);
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable("id") long id) {
        return service.getAccount(id)
                .map(a -> new ResponseEntity<>(a, OK))
                .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }

    @PutMapping("/account/deposit")
    public void deposit(@RequestBody Transfer transfer) {
        service.deposit(requireNonNull(transfer.getTo()), requireNonNull(transfer.getAmount()));
    }

    @PutMapping("/account/withdraw")
    public void withdraw(@RequestBody Transfer transfer) {
        service.withdraw(requireNonNull(transfer.getFrom()), requireNonNull(transfer.getAmount()));
    }
}
