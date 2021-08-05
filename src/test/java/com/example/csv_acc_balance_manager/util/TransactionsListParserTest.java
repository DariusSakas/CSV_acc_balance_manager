package com.example.csv_acc_balance_manager.util;

import com.example.csv_acc_balance_manager.repository.TransactionRepository;
import com.example.csv_acc_balance_manager.service.CSVService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionsListParserTest {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Mock
    private TransactionRepository transactionRepository;

    private TransactionsListParser transactionsListParser;

    @BeforeEach
    void setup() {
        transactionsListParser = new TransactionsListParser(transactionRepository);
    }

    @Test
    void formatCSVFileIntoCSVModelList() {

    }

    @Test
    void parseCSVIntoTransactionList() {
    }

    @Test
    void getTransactionsList() {
    }

    @Test
    void getTransactionsListValidTo() {
    }

    @Test
    void getTransactionsListValidFromTo() {
    }

    @Test
    void getTransactionsListValidFrom() {
    }
}
