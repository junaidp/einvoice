package com.einvoive.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.einvoive.model.Customer;
import com.einvoive.repository.CustomerRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadCustomersHelper {
    @Autowired
    CustomerRepository repository;
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
//    static String[] HEADERs = { "Id", "Title", "Description", "Published" };
    static String SHEET = "Sheet1";

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<Customer> excelToProductList(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Customer> productMainList = new ArrayList<Customer>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Customer customer = new Customer();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            customer.setCustomer(currentCell.getStringCellValue());
                            break;

                        case 1:
                            customer.setFirstName(currentCell.getStringCellValue());
                            break;

                        case 2:
                            customer.setLastName(currentCell.getStringCellValue());
                            break;

                        case 3:
                            customer.setEmail(currentCell.getStringCellValue());
                            break;

                        case 4:
                            int valuePhone = (int)currentCell.getNumericCellValue();
                            customer.setPhone(String.valueOf(valuePhone));
                            break;

                        case 5:
                            int valueAccountNo = (int)currentCell.getNumericCellValue();
                            customer.setAccountNumber(String.valueOf(valueAccountNo));
                            break;

                        case 6:
                            customer.setWebsite(currentCell.getStringCellValue());
                            break;

                        case 7:
                            customer.setNotes(currentCell.getStringCellValue());
                            break;

                        case 8:
                            customer.setCurrency(currentCell.getStringCellValue());
                            break;

                        case 9:
                            customer.setBillingAddress1(currentCell.getStringCellValue());
                            break;

                        case 10:
                            customer.setBillingAddress2(currentCell.getStringCellValue());
                            break;

                        case 11:
                            customer.setBillingCountry(currentCell.getStringCellValue());
                            break;

                        case 12:
                            customer.setBillingProvince(currentCell.getStringCellValue());
                            break;

                        case 13:
                            customer.setBillingCity(currentCell.getStringCellValue());
                            break;

                        case 14:
                            customer.setBillingPostal(currentCell.getStringCellValue());
                            break;

                        case 15:
                            customer.setShippingAddress1(currentCell.getStringCellValue());
                            break;

                        case 16:
                            customer.setShippingAddress2(currentCell.getStringCellValue());
                            break;

                        case 17:
                            customer.setShippingCountry(currentCell.getStringCellValue());
                            break;

                        case 18:
                            customer.setShippingProvince(currentCell.getStringCellValue());
                            break;

                        case 19:
                            customer.setShippingCity(currentCell.getStringCellValue());
                            break;

                        case 20:
                            customer.setShippingPostal(currentCell.getStringCellValue());
                            break;

                        case 21:
                            customer.setDeliveryInstructions(currentCell.getStringCellValue());
                            break;

                        case 22:
                            int companyID = (int)currentCell.getNumericCellValue();
                            customer.setCompanyID(String.valueOf(companyID));
                            break;

                        default:
                            break;
                    }

                    cellIdx++;
                }

                productMainList.add(customer);
            }

            workbook.close();

            return productMainList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public void saveAll(MultipartFile file) {
        try {
            List<Customer> customerList = excelToProductList(file.getInputStream());
            repository.saveAll(customerList);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

}

