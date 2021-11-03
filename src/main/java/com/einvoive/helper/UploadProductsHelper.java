package com.einvoive.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.einvoive.model.ProductMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadProductsHelper {
    private static LoginHelper loginHelper;
    @Autowired
    ProductMainHelper productMainHelper;
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "Id", "Title", "Description", "Published" };
    static String SHEET = "Sheet1";

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<ProductMain> excelToProductList(InputStream is, String companyID, String loggedInUserID) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<ProductMain> productMainList = new ArrayList<ProductMain>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                ProductMain productMain = new ProductMain();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
//                        case 0:
//                            productMain.setId(currentCell.getStringCellValue());
//                            break;

                        case 0:
                            productMain.setProductName(currentCell.getStringCellValue());
                            break;

                        case 1:
                            productMain.setDescription(currentCell.getStringCellValue());
                            break;

                        case 2:
                            int value = (int)currentCell.getNumericCellValue();
                            productMain.setPrice(String.valueOf(value));
                            break;

                        case 3:
                            int valueCode = (int)currentCell.getNumericCellValue();
                            productMain.setCode(String.valueOf(valueCode));
                            break;

                        case 4:
                            productMain.setAssignedChartofAccounts(currentCell.getStringCellValue());
                            break;

//                        case 5:
////                            int valueUserID = (int)currentCell.getNumericCellValue();
////                            productMain.setUserId(String.valueOf(valueUserID));
////                            productMain.setUserId(currentCell.getStringCellValue());
//                            productMain.setUserId(loginHelper.getLoggedInUserID());
//                            break;

//                        case 6:
////                            int valueCompanyID = (int)currentCell.getNumericCellValue();
////                            productMain.setCompanyID(String.valueOf(valueCompanyID));
////                            productMain.setCompanyID(currentCell.getStringCellValue());
//                            productMain.setCompanyID(loginHelper.getCompanyID());
//                            break;

                        default:
                            break;
                    }

                    cellIdx++;
                }
                productMain.setUserId(loggedInUserID);
                productMain.setCompanyID(companyID);
                productMainList.add(productMain);
            }

            workbook.close();

            return productMainList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public void saveAll(MultipartFile file, String companyID, String loggedInUserID) {
        try {
            List<ProductMain> productMainList = excelToProductList(file.getInputStream(), companyID, loggedInUserID);
//            productMainHelper.repository.saveAll(productMainList);
            for (ProductMain productEnglish : productMainList ) {
                ProductMain productArabic = productMainHelper.getProductArabicOnline(productEnglish);
                productMainHelper.save(productEnglish, productArabic);
            }
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

}

