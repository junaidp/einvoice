package com.einvoive.controller;

import com.einvoive.util.QRCodeGenerator;
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

    @GetMapping(value = "/genrateQRCode/{invoiceNo}" , produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(@PathVariable("invoiceNo") String invoiceNo) {
       String text = QRCodeGenerator.generateTextQRCode(invoiceNo);
        return ResponseEntity.status(HttpStatus.OK).body(QRCodeGenerator.getQRCodeImage(text));
    }
}