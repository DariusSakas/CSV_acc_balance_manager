package com.example.csv_acc_balance_manager.service;

import com.example.csv_acc_balance_manager.model.Transaction;
import com.example.csv_acc_balance_manager.repository.TransactionRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {

    private final TransactionRepository transactionRepository;

    public CSVService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void saveCSVTransactionsIntoDB(MultipartFile csvFile) throws Exception {
        List<Transaction> transactions = formatCSVFileIntoCSVModelList(csvFile);
        for (Transaction transaction : transactions) {

        }
    }

    private List<Transaction> formatCSVFileIntoCSVModelList(MultipartFile csvFile) throws Exception {
        List<Transaction> transactionList = new ArrayList<>();
        Transaction transaction = new Transaction();

        BufferedReader inputFile = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8));
        CSVParser csvParser = new CSVParser(inputFile, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
        Iterable<CSVRecord> csvRecords = csvParser.getRecords();

        csvRecords.forEach(csvRecord -> {
                    try {
                        transaction.setAccNumber(Integer.parseInt(csvRecord.get(0)));
                        transaction.setOperationDate(csvRecord.get(1));
                        transaction.setBeneficiary(csvRecord.get(2));
                        transaction.setComment(csvRecord.get(3));
                        transaction.setAmount(Double.parseDouble(csvRecord.get(4)));
                        transaction.setCurrency(csvRecord.get(5));
                        transactionList.add(transaction);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

        return transactionList;
    }

}
