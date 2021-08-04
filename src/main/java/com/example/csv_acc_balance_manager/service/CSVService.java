package com.example.csv_acc_balance_manager.service;

import com.example.csv_acc_balance_manager.model.CSVModel;
import com.example.csv_acc_balance_manager.model.Transaction;
import com.example.csv_acc_balance_manager.repository.TransactionRepository;
import com.fasterxml.jackson.databind.MappingIterator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CSVService {

    private final TransactionRepository transactionRepository;
    private final CsvMapper

    public CSVService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void saveCSVTransactionsIntoDB(MultipartFile csvFile) {
        MappingIterator<Transaction> personIter = new CsvMapper().readerWithTypedSchemaFor(Person.class).readValues(csvFile);
        List<Person> people = personIter.readAll();
    }
}
