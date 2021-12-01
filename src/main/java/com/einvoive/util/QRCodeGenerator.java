package com.einvoive.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;

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

    public static byte[] getQRCodeImageZatca(String sellerName, String vatRegNum, String timeStamp , String invoiceTotal, String vatTotal ) {
        try{

            byte[] sellerNameByte = getTLVFor("1", sellerName);
            byte[] vatRegNumBytes =getTLVFor("2", vatRegNum);
            byte[] timeStampByte =getTLVFor("3", timeStamp);
            byte[] invoiceTotalByte =getTLVFor("4", invoiceTotal);
            byte[] vatTotalByte =getTLVFor("5", vatTotal);

           byte[] concatedBytes =  joinByteArray(sellerNameByte, vatRegNumBytes, timeStampByte, invoiceTotalByte, vatTotalByte );
            String encoded = Base64.getEncoder().encodeToString(concatedBytes);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(encoded, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            return pngData;
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }
    }

    private static byte[] getTLVFor(String tagNum, String value){
        String valueLength = value.length()+"";

        byte[] tagBuf = tagNum.getBytes(StandardCharsets.UTF_8);
        byte[] tagValueLenghtBuf = valueLength.getBytes(StandardCharsets.UTF_8);
        byte[] tagValueBuf = value.getBytes(StandardCharsets.UTF_8);

        byte[] outputStream = joinByteArrayForTlv(tagBuf, tagValueLenghtBuf, tagValueBuf);

        return outputStream;
    }

    private static ByteArrayOutputStream getByteArrayOutputStream(byte[]... tagBytes) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        try {
            for(byte[] theByte : tagBytes)
            {
                outputStream.write(theByte);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }

    public static byte[] joinByteArray(byte[]... tagBytes) {

        return ByteBuffer.allocate(tagBytes[0].length + tagBytes[1].length + tagBytes[2].length + tagBytes[3].length + tagBytes[4].length )
                .put(tagBytes[0])
                .put(tagBytes[1])
                .put(tagBytes[2])
                .put(tagBytes[3])
                .put(tagBytes[4])
                .array();

    }

    public static byte[] joinByteArrayForTlv(byte[]... tagBytes) {

        return ByteBuffer.allocate(tagBytes[0].length + tagBytes[1].length + tagBytes[2].length)
                .put(tagBytes[0])
                .put(tagBytes[1])
                .put(tagBytes[2])
                .array();

    }


}