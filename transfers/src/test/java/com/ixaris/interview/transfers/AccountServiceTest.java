package com.ixaris.interview.transfers;

import com.ixaris.interview.transfers.model.Account;
import com.ixaris.interview.transfers.model.Transaction;
import com.ixaris.interview.transfers.service.AccountService;
import com.ixaris.interview.transfers.service.FileReaderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AccountServiceTest {

    @Mock
    FileReaderService fileReaderService;

    @InjectMocks
    AccountService accountService;

    private final String TEST_FILE_NAME = "transfers.txt";

    @Before
    public void setUp() throws ParseException, IOException, URISyntaxException {
        Transaction transaction1 = Transaction.builder().sourceAccount("0").destinationAccount("112233").amount(60.00).build();
        Transaction transaction2 = Transaction.builder().sourceAccount("0").destinationAccount("223344").amount(25.03).build();
        Transaction transaction3 = Transaction.builder().sourceAccount("0").destinationAccount("334455").amount(67.67).build();
        Transaction transaction4 = Transaction.builder().sourceAccount("112233").destinationAccount("223344").amount(11.11).build();
        Transaction transaction5 = Transaction.builder().sourceAccount("112233").destinationAccount("334455").amount(12.12).build();
        Transaction transaction6 = Transaction.builder().sourceAccount("223344").destinationAccount("334455").amount(06.018).build();

        List<Transaction> transactionList = Arrays.asList(transaction1,transaction2,transaction3,transaction4,transaction5,transaction6);
        when(fileReaderService.getTransactionsFromFile(TEST_FILE_NAME)).thenReturn(transactionList);
    }

    @Test
    public void shouldInitializeAccount() throws ParseException, IOException, URISyntaxException {
        List<Account> accountList = accountService.initializeAccounts(fileReaderService.getTransactionsFromFile(TEST_FILE_NAME));

        Assert.assertTrue(accountList.stream().anyMatch(account -> account.getAccountId().equals("112233")));
        Assert.assertTrue(accountList.stream().anyMatch(account -> account.getAccountId().equals("223344")));
        Assert.assertTrue(accountList.stream().anyMatch(account -> account.getAccountId().equals("334455")));
    }

    @Test
    public void shouldPrepareAccounts() throws ParseException, IOException, URISyntaxException {
        List<Transaction> transactionList = fileReaderService.getTransactionsFromFile(TEST_FILE_NAME);

        List<Account> accountList = accountService.initializeAccounts(transactionList);
        accountService.prepareAccounts(transactionList, accountList);

        Assert.assertTrue(accountList.stream()
                .filter(account -> account.getAccountId().equalsIgnoreCase("112233"))
                .findFirst()
                .get()
                .getBalance()
                .equals(60.00 - 11.11 - 12.12));

        Assert.assertTrue(accountList.stream()
                .filter(account -> account.getAccountId().equalsIgnoreCase("112233"))
                .findFirst()
                .get()
                .getUsagesAsSource()
                .equals(2));

        Assert.assertTrue(accountList.stream()
                .filter(account -> account.getAccountId().equalsIgnoreCase("223344"))
                .findFirst()
                .get()
                .getBalance()
                .equals(25.03 + 11.11 - 6.018));

        Assert.assertTrue(accountList.stream()
                .filter(account -> account.getAccountId().equalsIgnoreCase("223344"))
                .findFirst()
                .get()
                .getUsagesAsSource()
                .equals(1));

        Assert.assertTrue(accountList.stream()
                .filter(account -> account.getAccountId().equalsIgnoreCase("334455"))
                .findFirst()
                .get()
                .getBalance()
                .equals(67.67 + 12.12 + 6.018));

        Assert.assertTrue(accountList.stream()
                .filter(account -> account.getAccountId().equalsIgnoreCase("334455"))
                .findFirst()
                .get()
                .getUsagesAsSource()
                .equals(0));

        Assert.assertTrue(accountService.getHighestBalanceAccount(accountList).getAccountId().equalsIgnoreCase("334455"));
        Assert.assertTrue(accountService.getMostUsedSource(accountList).getAccountId().equalsIgnoreCase("112233"));
    }


}
