# Application
## Install
* gh repo clone DariusSakas/CSV_acc_balance_manager
* mvn install
## Application properties
* Application port set: 9090 (change it at applicatin_properties)
* To acces H2 use: http://localhost:9090/h2-console
## .csv file
 * .csv dummy file is hidden at resources/static/csvfile.csv
## Post and Get API's (prefer Postman):
 * for POST use address: localhost:9090/csv/postCSV
 form-data format, use request param key "csvFile", body file (use provided .csv file or yours)
 * for GET use address: localhost:9090/csv/getCSV
  form-data format, optional request params: "fromDate" and "toDate"
