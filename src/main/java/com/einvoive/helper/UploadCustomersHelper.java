package com.einvoive.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.einvoive.constants.Constants;
import com.einvoive.model.Customer;
import com.einvoive.repository.CustomerRepository;
import com.einvoive.util.Translator;
import com.google.gson.Gson;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadCustomersHelper {
    @Autowired
    CustomerRepository repository;
    private Logger logger = LoggerFactory.getLogger(UploadCustomersHelper.class);
    @Autowired
    CustomerHelper customerHelper;
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
//    static String[] HEADERs = { "Id", "Title", "Description", "Published" };
    static String SHEET = "Sheet1";
    private Gson gson = new Gson();

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<Customer> excelToProductList(InputStream is, String companyID) {
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
                            int valuePhoneMain = (int)currentCell.getNumericCellValue();
                            customer.setPhoneMain(String.valueOf(valuePhoneMain));
                            break;

                        case 5:
                            int valuePhone = (int)currentCell.getNumericCellValue();
                            customer.setPhone(String.valueOf(valuePhone));
                            break;

                        case 6:
                            int valueAccountNo = (int)currentCell.getNumericCellValue();
                            customer.setAccountNumber(String.valueOf(valueAccountNo));
                            break;

                        case 7:
                            customer.setWebsite(currentCell.getStringCellValue());
                            break;

                        case 8:
                            customer.setRelatedParty(currentCell.getStringCellValue());
                            break;

                        case 9:
                            customer.setNotes(currentCell.getStringCellValue());
                            break;

                        case 10:
                            customer.setCurrency(currentCell.getStringCellValue());
                            break;

                        case 11:
                            customer.setBillingAddress1(currentCell.getStringCellValue());
                            break;

//                        case 12:
//                            customer.setBillingAddress2(currentCell.getStringCellValue());
//                            break;
//
//                        case 13:
//                            customer.setBillingCountry(currentCell.getStringCellValue());
//                            break;
//
//                        case 14:
//                            customer.setBillingProvince(currentCell.getStringCellValue());
//                            break;

//                        case 15:
//                            customer.setBillingCity(currentCell.getStringCellValue());
//                            break;
//
//                        case 16:
//                            customer.setBillingPostal(currentCell.getStringCellValue());
//                            break;

//                        case 17:
//                            customer.setShippingName(currentCell.getStringCellValue());
//                            break;

                        case 18:
                            customer.setShippingAddress1(currentCell.getStringCellValue());
                            break;

                        case 19:
                            customer.setShippingAddress2(currentCell.getStringCellValue());
                            break;

                        case 20:
                            customer.setShippingCountry(currentCell.getStringCellValue());
                            break;

                        case 21:
                            customer.setShippingProvince(currentCell.getStringCellValue());
                            break;

                        case 22:
                            customer.setShippingCity(currentCell.getStringCellValue());
                            break;

                        case 23:
                            customer.setShippingPostal(currentCell.getStringCellValue());
                            break;

                        case 24:
                            customer.setDeliveryInstructions(currentCell.getStringCellValue());
                            break;

                        case 25:
                            int vat = (int)currentCell.getNumericCellValue();
                            customer.setVatNumber_Customer(String.valueOf(vat));
                            break;

                        case 26:
                            int additionalNumber_Customer = (int)currentCell.getNumericCellValue();
                            customer.setAdditionalNumber_Customer(String.valueOf(additionalNumber_Customer));
                            break;

                        case 27:
                            int otherSellerid_Customer = (int)currentCell.getNumericCellValue();
                            customer.setOtherSellerid_Customer(String.valueOf(otherSellerid_Customer));
                            break;

                        default:
                            break;
                    }

                    cellIdx++;
                }
                customer.setCompanyID(companyID);
                productMainList.add(customer);
            }

            workbook.close();

            return productMainList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public String saveAll(MultipartFile file, String companyID) {
        String response = "Record Uploaded";
        try {
            List<Customer> customerList = excelToProductList(file.getInputStream(), companyID);
//            repository.saveAll(customerList);
            for (Customer customerEnglish : customerList ) {
                Customer customerArabic = getCustomerArabicOnline(customerEnglish);
                response = customerHelper.save(customerEnglish, customerArabic);
            }
        } catch (IOException e) {
            response = "fail to store excel data: " + e.getMessage();
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
        return response;
    }

    private Customer getCustomerArabicOnline(Customer customerEnglish) {
        Customer customerArabic = new Customer();
        customerArabic.setFirstName(Translator.getTranslation(customerEnglish.getFirstName()));
        customerArabic.setLastName(Translator.getTranslation(customerEnglish.getLastName()));
        customerArabic.setCustomer(Translator.getTranslation(customerEnglish.getCustomer()));
        customerArabic.setBillingAddress1(Translator.getTranslation(customerEnglish.getBillingAddress1()));
//        customerArabic.setBillingAddress2(Translator.getTranslation(customerEnglish.getBillingAddress2()));
        customerArabic.setNotes(Translator.getTranslation(customerEnglish.getNotes()));
//        customerArabic.setBillingCountry(Translator.getTranslation(customerEnglish.getBillingCountry()));
//        customerArabic.setBillingProvince(Translator.getTranslation(customerEnglish.getBillingProvince()));
//        customerArabic.setBillingCity(Translator.getTranslation(customerEnglish.getBillingCity()));
//        customerArabic.setBillingPostal(Translator.getTranslation(customerEnglish.getBillingPostal()));
        customerArabic.setShippingAddress1(Translator.getTranslation(customerEnglish.getShippingAddress1()));
        customerArabic.setShippingAddress2(Translator.getTranslation(customerEnglish.getShippingAddress2()));
//        customerArabic.setShippingName(Translator.getTranslation(customerEnglish.getShippingName()));
        customerArabic.setShippingCountry(Translator.getTranslation(customerEnglish.getShippingCountry()));
        customerArabic.setShippingProvince(Translator.getTranslation(customerEnglish.getShippingProvince()));
        customerArabic.setShippingCity(Translator.getTranslation(customerEnglish.getShippingCity()));
        customerArabic.setShippingPostal(Translator.getTranslation(customerEnglish.getShippingPostal()));
        customerArabic.setDeliveryInstructions(Translator.getTranslation(customerEnglish.getDeliveryInstructions()));
        return customerArabic;
    }
}

