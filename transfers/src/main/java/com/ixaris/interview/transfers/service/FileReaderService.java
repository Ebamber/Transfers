package com.ixaris.interview.transfers.service;

import com.ixaris.interview.transfers.model.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class FileReaderService {

    //I'm using the header in transfers.txt as a marker for where to start my parsing of transactions
    // - please stick to the file structure in transfers.txt as a standard going forward or the system will break
    //I have the unit tests necessary to make sure this is covered in case any changes are needed
    private final String HEADER_LINE = "SOURCE_ACCT, DESTINATION_ACCT, AMOUNT, DATE, TRANSFERID";
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    public List<Transaction> getTransactionsFromFile(String fileName) throws IOException, URISyntaxException, ParseException {

        List<Transaction> transactionList = new ArrayList<>();
        List<String> bufferedOutput = readAllLines(fileName);
        for (int line = bufferedOutput.indexOf(HEADER_LINE) + 1; line < bufferedOutput.size(); line++) {
            String transactionString = bufferedOutput.get(line);
            String[] transactionElements = transactionString.replaceAll("\\s","").split(",");
            transactionList.add(
                    Transaction.builder()
                            .sourceAccount(transactionElements[0])
                            .destinationAccount(transactionElements[1])
                            .amount(Double.valueOf(transactionElements[2]))
                            .date(formatter.parse(transactionElements[3]))
                            .transactionId(transactionElements[4])
                            .build());
        }
        return transactionList;
    }

    //This method is so that I can read directly from a given file in the REST endpoint,
    // just a slight change on the other
    public List<Transaction> getTransactionsFromFile(MultipartFile multipartFile) throws IOException, URISyntaxException, ParseException {

        List<Transaction> transactionList = new ArrayList<>();
        List<String> bufferedOutput = Arrays.asList(new String(multipartFile.getBytes()).split("\\r\\n"));
        for (int line = bufferedOutput.indexOf(HEADER_LINE) + 1; line < bufferedOutput.size(); line++) {
            String transactionString = bufferedOutput.get(line);
            String[] transactionElements = transactionString.replaceAll("\\s","").split(",");
            transactionList.add(
                    Transaction.builder()
                            .sourceAccount(transactionElements[0])
                            .destinationAccount(transactionElements[1])
                            .amount(Double.valueOf(transactionElements[2]))
                            .date(formatter.parse(transactionElements[3]))
                            .transactionId(transactionElements[4])
                            .build());
        }
        return transactionList;
    }

    //URISyntaxException is used if we read from file name rather than file path
    public List<String> readAllLines(String file) throws IOException, URISyntaxException {
        //Uses a file name and reads from resources folder
        /*
        final URL file = getClass().getClassLoader().getResource(file);
        return Files.readAllLines(Paths.get(file.toURI()));
         */

        //Uses the full path
        return Files.readAllLines(Paths.get(file));
    }

}
