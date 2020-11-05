package com.ixaris.interview.transfers.service;

import com.ixaris.interview.transfers.model.Account;
import com.ixaris.interview.transfers.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    public List<Account> initializeAccounts(List<Transaction> transactionList) {
        List<Account> accounts = new ArrayList<>();
        for(Transaction transaction : transactionList) {
            if(!containsAccount(accounts,transaction.getSourceAccount())) {
                accounts.add(Account.builder().accountId(transaction.getSourceAccount()).balance(0.00).usagesAsSource(0).build());
            }

            if(!containsAccount(accounts,transaction.getDestinationAccount())) {
                accounts.add(Account.builder().accountId(transaction.getDestinationAccount()).balance(0.00).usagesAsSource(0).build());
            }
        }
        return accounts.stream()
                .filter(account -> !account.getAccountId().equalsIgnoreCase("0"))
                .collect(Collectors.toList());
    }

    public void prepareAccounts(List<Transaction> transactionList, List<Account> accountList) {
        for(Account account : accountList) {

            transactionList.stream()
                    .filter(transaction -> transaction.getDestinationAccount().equalsIgnoreCase(account.getAccountId()))
                    .forEach(transaction -> account.setBalance(account.getBalance() + transaction.getAmount()));


            transactionList.stream()
                    .filter(transaction -> transaction.getSourceAccount().equalsIgnoreCase(account.getAccountId()))
                    .forEach(transaction -> {
                        account.setBalance(account.getBalance() - transaction.getAmount());
                        account.setUsagesAsSource(account.getUsagesAsSource() + 1);
                    });
        }
    }

    public Account getMostUsedSource(List<Account> accountList) {
        Account max = Account.builder().usagesAsSource(-1).build();
        for(Account account : accountList) {
            if(account.getUsagesAsSource() > max.getUsagesAsSource()) {
                max = account;
            }
        }
        return max;
    }

    public Account getHighestBalanceAccount(List<Account> accountList) {
        Account max = Account.builder().balance(-1.00).build();
        for(Account account : accountList) {
            if(account.getBalance() > max.getBalance()) {
                max = account;
            }
        }
        return max;
    }

    private boolean containsAccount(List<Account> accounts, String accountId) {
        return accounts.stream().anyMatch(account -> account.getAccountId().equals(accountId));
    }

}
