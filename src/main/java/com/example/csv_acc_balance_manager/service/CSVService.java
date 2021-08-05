package com.example.csv_acc_balance_manager.service;

import com.example.csv_acc_balance_manager.exception.InvalidValueProvided;
import com.example.csv_acc_balance_manager.model.Transaction;
import com.example.csv_acc_balance_manager.repository.TransactionRepository;
import com.example.csv_acc_balance_manager.util.TransactionsListParser;
import org.apache.commons.csv.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CSVService {

    private static final CSVFormat format = CSVFormat.DEFAULT.withHeader(
            "Account", "Date", "Beneficiary", "Comment", "Amount", "Currency");

    private final TransactionRepository transactionRepository;
    private final TransactionsListParser transactionsListParser;

    public CSVService(TransactionRepository transactionRepository, TransactionsListParser transactionsListParser) {
        this.transactionRepository = transactionRepository;
        this.transactionsListParser = transactionsListParser;
    }

    public void saveCSVTransactionsIntoDB(MultipartFile csvFile) throws Exception {
        List<Transaction> transactions = transactionsListParser.formatCSVFileIntoCSVModelList(csvFile);
        transactionRepository.saveAll(transactions);
    }

    public ByteArrayInputStream getCSVFile(String dateFrom, String dateTo) throws InvalidValueProvided, ParseException {

        String dateFromValid = getValidDateFormatElseEmpty(dateFrom);
        String dateToValid = getValidDateFormatElseEmpty(dateTo);

        List<Transaction> transactionList = transactionsListParser.getTransactionsList(dateFromValid, dateToValid);

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), format);
            printTransactionsToCSV(transactionList, csvPrinter);

            csvPrinter.flush();
            return new ByteArrayInputStream(outputStream.toByteArray());

        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to import data to CSV from DB");
        }
    }

    private void printTransactionsToCSV(List<Transaction> transactionList, CSVPrinter csvPrinter) {
        transactionList.forEach(t ->
        {
            try {
                csvPrinter.printRecord(Arrays.asList(
                        String.valueOf(t.getAccNumber()),
                        t.getOperationDate(),
                        t.getBeneficiary(),
                        t.getComment(),
                        t.getAmount(),
                        t.getCurrency()
                        ));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public String getValidDateFormatElseEmpty(String dateToCheck) throws InvalidValueProvided {
        if (dateToCheck != null && !dateToCheck.isEmpty()) {
            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateToCheck);
                return dateToCheck;
            } catch (ParseException e) {
                e.printStackTrace();
                throw new InvalidValueProvided("Invalid date format or value");
            }
        } else if (dateToCheck == null || dateToCheck.isEmpty()) {
            return "";
        }
        return "";
    }
}
