package com.example.csv_acc_balance_manager.service;

import com.example.csv_acc_balance_manager.model.CSVModel;
import com.example.csv_acc_balance_manager.model.Transaction;
import com.example.csv_acc_balance_manager.repository.TransactionRepository;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CSVService {

    private final TransactionRepository transactionRepository;

    public CSVService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void saveCSVTransactionsIntoDB(MultipartFile csvFile) throws IOException {

        MappingIterator<CSVModel> personIter = new CsvMapper().readerWithTypedSchemaFor(CSVModel.class).readValues(csvFile);
        List<CSVModel> people = personIter.readAll();
    }
}
