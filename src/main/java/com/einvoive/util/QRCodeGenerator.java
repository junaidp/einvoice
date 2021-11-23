package com.einvoive.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.einvoive.helper.InvoiceHelper;
import com.einvoive.model.Invoice;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class QRCodeGenerator {

    @Autowired
    static
    MongoOperations mongoOperations;

    public static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

    }

    public static String generateTextQRCode(String invoiceNo) {
//        Invoice invoice = invoiceHelper.getInvoiceByInvoiceNoQR(invoiceNo);
        Invoice invoice = mongoOperations.findOne(new Query(Criteria.where("invoiceNumber").is(invoiceNo)), Invoice.class);
        String text = invoice.getCustomerName()+"-"+invoice.getTotalExcludingVAT()+"-"+invoice.getTotalAmountDue();
        return text;
    }

    public static byte[] getQRCodeImage(String text) {
        try{
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            return pngData;
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }
    }

}