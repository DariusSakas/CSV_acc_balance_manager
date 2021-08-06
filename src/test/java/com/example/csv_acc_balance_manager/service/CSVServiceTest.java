package com.example.csv_acc_balance_manager.service;

import com.example.csv_acc_balance_manager.exception.InvalidValueProvided;
import com.example.csv_acc_balance_manager.model.Transaction;
import com.example.csv_acc_balance_manager.repository.TransactionRepository;
import com.example.csv_acc_balance_manager.util.TransactionsListParser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CSVServiceTest {

    private static MultipartFile multipartFile;

    @Captor
    ArgumentCaptor<List<Transaction>> arrayListArgumentCaptor;

    @BeforeAll
    static void beforeAll() {
        Path path = Paths.get("src/main/resources/static/csvfile.csv");
        String name = "csvfile.csv";
        String originalFileName = "csvfile.csv";
        String contentType = "text/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        multipartFile = new MockMultipartFile(name,
                originalFileName, contentType, content);
    }

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionsListParser transactionsListParser;

    private CSVService csvService;

    @BeforeEach
    void setup() {
        csvService = new CSVService(transactionRepository, transactionsListParser);
    }

    @Test
    void canSaveCSVTransactionsIntoDB() throws Exception {

        csvService.saveCSVTransactionsIntoDB(multipartFile);

        verify(transactionRepository).saveAll(arrayListArgumentCaptor.capture());
    }

    @Test
    @Disabled
    void canGetCSVFile() throws ParseException, InvalidValueProvided {

    }

    @Test
    @Disabled
    void canGetValidDateFormatElseEmpty() {

    }
}
