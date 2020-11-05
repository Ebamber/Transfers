package com.ixaris.interview.transfers.controller;


import com.ixaris.interview.transfers.model.Account;
import com.ixaris.interview.transfers.model.FileReaderResponse;
import com.ixaris.interview.transfers.model.Transaction;
import com.ixaris.interview.transfers.service.AccountService;
import com.ixaris.interview.transfers.service.FileReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileReaderController {

    private final FileReaderService fileReaderService;
    private final AccountService accountService;

    //A more elegant alternative to simply reading a file path and working with that in my opinion
    @GetMapping("/accounts")
    public FileReaderResponse getAccounts(@RequestParam(value = "file") MultipartFile file) throws ParseException, IOException, URISyntaxException {
        List<Transaction> transactionList = fileReaderService.getTransactionsFromFile(file);

        List<Account> accountList = accountService.initializeAccounts(transactionList);
        accountService.prepareAccounts(transactionList, accountList);

        List<String> accountBalances = new ArrayList<>();
        accountList.forEach( account -> accountBalances.add("Account balance for account: " + account.getAccountId() + " is: " + account.getBalance()));

        return FileReaderResponse
                    .builder()
                    .accountBalances(accountBalances)
                    .highestBalanceAccount(accountService.getHighestBalanceAccount(accountList))
                    .mostUsedSource(accountService.getMostUsedSource(accountList))
                    .build();
    }

}
