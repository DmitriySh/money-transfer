package ru.shishmakov.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shishmakov.dto.AccountDTO;
import ru.shishmakov.dto.TransferDTO;
import ru.shishmakov.persistence.entity.AccountAudit;
import ru.shishmakov.service.AccountService;


import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

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
    public ResponseEntity<List<AccountDTO>> getAccounts() {
        return new ResponseEntity<>(service.getAccounts().stream()
                .map(t -> AccountDTO.builder()
                        .accNumber(t.getAccNumber()).amount(t.getAmount())
                        .build())
                .collect(Collectors.toList()), OK);
    }

    @PutMapping("/accounts/transfer")
    public ResponseEntity<List<AccountDTO>> transfer(@RequestBody TransferDTO transfer) {
        try {
            requireNonNull(transfer.getFrom());
            requireNonNull(transfer.getTo());
            var accounts = service.transfer(transfer.getFrom(), transfer.getTo(), transfer.getAmount())
                    .stream().map(t -> AccountDTO.builder()
                            .accNumber(t.getAccNumber()).amount(t.getAmount())
                            .build())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(accounts, OK);
        } catch (Exception e) {
            log.error("transfer error", e);
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    @GetMapping("/account/{accNumber}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable("accNumber") long accNumber) {
        return service.getAccount(accNumber)
                .map(a -> AccountDTO.builder()
                        .accNumber(a.getAccNumber()).amount(a.getAmount())
                        .build())
                .map(a -> new ResponseEntity<>(a, OK))
                .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }

    @PutMapping("/account/deposit")
    public ResponseEntity<AccountDTO> deposit(@RequestBody TransferDTO transfer) {
        try {
            requireNonNull(transfer.getTo());
            var account = service.deposit(transfer.getTo(), transfer.getAmount());
            return new ResponseEntity<>(AccountDTO.builder()
                    .accNumber(account.getAccNumber()).amount(account.getAmount())
                    .build(), OK);
        } catch (Exception e) {
            log.error("transfer error", e);
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    @PutMapping("/account/withdraw")
    public ResponseEntity<AccountDTO> withdraw(@RequestBody TransferDTO transfer) {
        try {
            var account = service.withdraw(requireNonNull(transfer.getFrom()), transfer.getAmount());
            return new ResponseEntity<>(AccountDTO.builder()
                    .accNumber(account.getAccNumber()).amount(account.getAmount())
                    .build(), OK);
        } catch (Exception e) {
            log.error("transfer error", e);
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }
}
