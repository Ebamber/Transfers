package com.ixaris.interview.transfers;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p>
 * 	A Command-line application to parse and process a transfers file and provide the business requirements, namely:
 * 	<ul>
 * 	    <li>1. Print the final balances on all bank accounts</li>
 * 	    <li>2. Print the bank account with the highest balance</li>
 * 	    <li>3. Print the most frequently used source bank account</li>
 * 	</ul>
 * </p>
 */
@SpringBootApplication
@Log4j2
public class TransfersApplication /*implements CommandLineRunner*/ {
/*
    @Autowired
    private FileReaderService fileReaderService;

	@Autowired
	private AccountService accountService;
*/
	public static void main(String[] args) {
		SpringApplication.run(TransfersApplication.class, args);
	}

	/* Replaced with REST Controller, just uncomment this and comment the controller if you want to use this
	@Override
	public void run(final String... args) throws URISyntaxException, IOException, ParseException {
		// Below is some sample code to get you started. Good luck :)

		BufferedReader reader =
				new BufferedReader(new InputStreamReader(System.in));

		log.info("Enter the full path to a file to parse: ");

		String filePath = reader.readLine();

        List<Transaction> transactionList = fileReaderService.getTransactionsFromFile(filePath);

        List<Account> accountList = accountService.initializeAccounts(transactionList);
        accountService.prepareAccounts(transactionList, accountList);

        accountList.forEach( account -> log.info("Account balance for account: " + account.getAccountId() + " is: " + account.getBalance()));
		log.info("Account most used as source is: " + accountService.getMostUsedSource(accountList));
		log.info("Account with highest balance is: " + accountService.getHighestBalanceAccount(accountList));
	}
	*/


}
