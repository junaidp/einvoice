package com.einvoive.controller;

import com.einvoive.util.QRCodeGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RequestMapping("QRcontrol")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class QRCodeController {

    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/QRCode.png";


    @GetMapping(value = "/genrateAndDownloadQRCode/{codeText}/{width}/{height}")
    public void download(
            @PathVariable("codeText") String codeText,
            @PathVariable("width") Integer width,
            @PathVariable("height") Integer height)
            throws Exception {
        QRCodeGenerator.generateQRCodeImage(codeText, width, height, QR_CODE_IMAGE_PATH);
    }

    @GetMapping(value = "/generateQRCodeImage/{codeText}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> generateQRCodeImage(
            @PathVariable("codeText") String codeText)
            throws Exception {
        String[] params = StringUtils.split(codeText, "&");
        String sellerName =  params[0];
        String vatRegNo   =  params[1];
        String timeStamp  =  params[2];
        String invoiceTotal =  params[3];
        String vatTotal =  params[4];
        //return ResponseEntity.status(HttpStatus.OK).body(QRCodeGenerator.getQRCodeImageZatca("test Records","310122393500003", "2022-04-25T15:30:00Z", "1000.00", "150.00" ));
        return ResponseEntity.status(HttpStatus.OK).body(QRCodeGenerator.getQRCodeImageZatca(sellerName,vatRegNo, timeStamp, invoiceTotal, vatTotal ));

    }

    @GetMapping(value = "/genrateQRCode/{invoiceNo}" , produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(@PathVariable("invoiceNo") String invoiceNo) {
       String text = QRCodeGenerator.generateTextQRCode(invoiceNo);
        return ResponseEntity.status(HttpStatus.OK).body(QRCodeGenerator.getQRCodeImage(text));
    }
}