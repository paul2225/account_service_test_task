package com.example.account_service.controller;

import com.example.account_service.service.AccountService;
import com.example.account_service.statistics.LoggingAspect;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final LoggingAspect loggingAspect;

    public AccountController(AccountService accountService, LoggingAspect loggingAspect) {
        this.accountService = accountService;
        this.loggingAspect = loggingAspect;
    }

    @GetMapping
    public ResponseEntity<Long> getAmount(@RequestParam int id) {
        return new ResponseEntity<>(accountService.getAmount(id), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> addAmount(@RequestBody Map<String, String> map) {
        accountService.addAmount(Integer.parseInt(map.get("id")), Long.parseLong(map.get("amount")));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Map<String, String> map) {
        accountService.save(Integer.parseInt(map.get("id")));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clean_stats")
    public ResponseEntity<?> cleanUpMethodsStatistics() {
        loggingAspect.cleanUpLogs();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
