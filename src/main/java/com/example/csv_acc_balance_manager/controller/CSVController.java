package com.example.csv_acc_balance_manager.controller;

import com.example.csv_acc_balance_manager.service.CSVService;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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
            return new ResponseEntity<>("Unable to convert and save CSV file to DB. Invalid file type or values.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("CSV converted and saved successfully", HttpStatus.OK);
    }

    @GetMapping("/getCSV")
    public ResponseEntity<Resource> getTransactionsFromDBasCSVFile(@RequestParam(value = "fromDate", required = false) String fromDate, @RequestParam(value = "toDate", required = false) String toDate){
        String filename = "transactions.csv";
        InputStreamResource file = new InputStreamResource(csvService.getCSVFile(), fromDate, toDate);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
}
