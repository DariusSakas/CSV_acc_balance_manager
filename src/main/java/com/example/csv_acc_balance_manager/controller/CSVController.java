package com.example.csv_acc_balance_manager.controller;

import com.example.csv_acc_balance_manager.exception.InvalidValueProvided;
import com.example.csv_acc_balance_manager.model.CSVModel;
import com.example.csv_acc_balance_manager.service.CSVService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/csv")
public class CSVController {

    private final CSVService csvService;

    public CSVController(CSVService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/postCSV")
    public ResponseEntity<String> saveTransactionsToDB (@RequestParam("csvFile") MultipartFile csvFile){

        try {
            csvService.saveCSVTransactionsIntoDB(csvFile);

        } catch (Exception e) {
            return new ResponseEntity<>("Unable to convert and save CSV file to DB", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("CSV converted and saved successfully", HttpStatus.OK);
    }
}
