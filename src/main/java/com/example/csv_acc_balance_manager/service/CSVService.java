package com.example.csv_acc_balance_manager.service;

import com.example.csv_acc_balance_manager.exception.InvalidValueProvided;
import com.example.csv_acc_balance_manager.model.CSVModel;
import com.example.csv_acc_balance_manager.model.Transaction;
import com.example.csv_acc_balance_manager.repository.TransactionRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
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
        List<CSVModel> transactionsCSVModelType = formatCSVFileIntoCSVModelList(csvFile);

        List<Transaction> transactions = new ArrayList<>();

        transactionsCSVModelType.forEach(transactionCSVtype -> {
            try {
                transactions.add(generateNewTransactionObject(transactionCSVtype, transactionsCSVModelType));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private Transaction generateNewTransactionObject(CSVModel csvModel, List<CSVModel> transactionsCSVModelType ) throws Exception {
        Transaction transaction = new Transaction();

        transaction.setAccNumber(csvModel.getAccNumber());
        transaction.setOperationDate(csvModel.getOperationDate());
        transaction.setBeneficiary(csvModel.getBeneficiary());
        transaction.setComment(csvModel.getComment());
        transaction.setAmount(csvModel.getAmount());
        transaction.setCurrency(csvModel.getCurrency());

        transaction.setFinalBalance(calculateFinalBalanceByDate(csvModel.getOperationDate(), transactionsCSVModelType));

        return transaction;
    }

    private double calculateFinalBalanceByDate(String operationDate, List<CSVModel> transactionsCSVModelType) {
        return 0;
    }

    private List<CSVModel> formatCSVFileIntoCSVModelList(MultipartFile csvFile) throws IOException {
        List<CSVModel> csvModels = new ArrayList<>();

        BufferedReader inputFile = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8));
        CSVParser csvParser = new CSVParser(inputFile, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
        Iterable<CSVRecord> csvRecords = csvParser.getRecords();

        csvRecords.forEach(e ->csvModels.add(new CSVModel(
                Integer.parseInt(e.get(0)),
                e.get(1),
                e.get(2),
                Double.parseDouble(e.get(4)),
                e.get(5),
                e.get(3)
                )));

        return csvModels;
    }

}
