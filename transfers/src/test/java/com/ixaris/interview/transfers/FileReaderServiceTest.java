package com.ixaris.interview.transfers;

import com.ixaris.interview.transfers.model.Transaction;
import com.ixaris.interview.transfers.service.FileReaderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@RunWith(SpringRunner.class)
public class FileReaderServiceTest {

    @InjectMocks
    FileReaderService fileReaderService;

    private String TEST_FILE_PATH;
    private final String TEST_FILE_NAME = "transfers.txt";

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    @Before
    public void prepareEnvironment() throws URISyntaxException {
        TEST_FILE_PATH = getClass().getClassLoader().getResource(TEST_FILE_NAME).getFile().substring(1);
    }

    @Test
    public void shouldReadAllLinesInAFile() throws IOException, URISyntaxException {
        List<String> lines = fileReaderService.readAllLines(TEST_FILE_PATH);

        Assert.assertEquals(9, lines.size());
        Assert.assertEquals("223344, 334455, 006.018, 13/08/2055, 1450", lines.get(8));
    }

    @Test
    public void shouldParseAllTransactions() throws IOException, URISyntaxException, ParseException {
        List<Transaction> transactionList = fileReaderService.getTransactionsFromFile(TEST_FILE_PATH);

        Assert.assertTrue(
                transactionList.contains(
                        Transaction.builder()
                                .sourceAccount("223344")
                                .destinationAccount("334455")
                                .amount(6.018)
                                .date(formatter.parse("13/08/2055"))
                                .transactionId("1450")
                                .build()));
    }

    @Test
    public void shouldParseAllTransactions_FileInput() throws IOException, URISyntaxException, ParseException {
        List<Transaction> transactionList = fileReaderService.getTransactionsFromFile(fileUpload(TEST_FILE_PATH));

        Assert.assertTrue(
                transactionList.contains(
                        Transaction.builder()
                                .sourceAccount("223344")
                                .destinationAccount("334455")
                                .amount(6.018)
                                .date(formatter.parse("13/08/2055"))
                                .transactionId("1450")
                                .build()));
    }

    private MultipartFile fileUpload(String locationFile) {

        Path path = Paths.get(TEST_FILE_PATH);
        String name = "TEST.txt";
        String originalFileName = "TEST.txt";
        String contentType = "text/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        MultipartFile file = new MockMultipartFile(name, originalFileName, contentType, content);

        return file;

    }

}
