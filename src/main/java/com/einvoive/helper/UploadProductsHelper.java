package com.einvoive.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.einvoive.repository.ProductMainRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.einvoive.model.ProductMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadProductsHelper {
    @Autowired
    ProductMainRepository repository;
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "Id", "Title", "Description", "Published" };
    static String SHEET = "Tutorials";

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<ProductMain> excelToProductList(InputStream is) {
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

                        case 1:
                            productMain.setProductName(currentCell.getStringCellValue());
                            break;

                        case 2:
                            productMain.setDescription(currentCell.getStringCellValue());
                            break;

                        case 3:
                            productMain.setPrice(currentCell.getStringCellValue());
                            break;

                        case 4:
                            productMain.setCode(currentCell.getStringCellValue());
                            break;

                        case 5:
                            productMain.setAssignedChartofAccounts(currentCell.getStringCellValue());
                            break;

                        case 6:
                            productMain.setUserId(currentCell.getStringCellValue());
                            break;

                        case 7:
                            productMain.setCompanyID(currentCell.getStringCellValue());
                            break;

                        default:
                            break;
                    }

                    cellIdx++;
                }

                productMainList.add(productMain);
            }

            workbook.close();

            return productMainList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public void saveAll(MultipartFile file) {
        try {
            List<ProductMain> productMainList = excelToProductList(file.getInputStream());
            repository.saveAll(productMainList);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

}

