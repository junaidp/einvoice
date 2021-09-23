package com.einvoive.controller;

import com.einvoive.helper.CompanyHelper;
import com.einvoive.helper.LogoHelper;
import com.einvoive.helper.UploadCustomersHelper;
import com.einvoive.helper.UploadProductsHelper;
import com.einvoive.model.Logo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("uploader")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UploadController {

    @Autowired
    UploadProductsHelper uploadProductsHelper;

    @Autowired
    UploadCustomersHelper uploadCustomersHelper;

    @Autowired
    CompanyHelper companyHelper;

    @Autowired
    LogoHelper logoHelper;

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "D://Envoice//help//";
    private static String UPLOAD_Product_FOLDER = "D://Envoice//products//";
    private static String UPLOAD_Customer_FOLDER = "D://Envoice//customers//";
    private static String UPLOAD_Logos_FOLDER = "D://Envoice//logos//";

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

//    @PostMapping("/uploadLogo") // //new annotation since 4.3
//    public String uploadLogo(@RequestParam("file") MultipartFile file, String companyID) {
//
//        if (file.isEmpty()) {
////            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
//            return "uploadProducts file is empty";
//        }
//        String message = null;
////        if (uploadProductsHelper.hasExcelFormat(file)) {
//            try {
//                byte[] bytes = file.getBytes();
//                Path path = Paths.get(UPLOAD_Logos_FOLDER + file.getOriginalFilename());
//                Files.write(path, bytes);
//                companyHelper.uploadCompanyLogo(UPLOAD_Logos_FOLDER + file.getOriginalFilename(), companyID);
////                redirectAttributes.addFlashAttribute("message",
////                        "Uploaded the file successfully: '" + file.getOriginalFilename() + "'");
//                message = "Uploaded the file successfully: " + file.getOriginalFilename();
//            } catch (Exception e) {
//                e.printStackTrace();
//                message = "Sorry!!! Could not upload the file: " + file.getOriginalFilename() + "!";
//            }
////        }
//        return message;
//    }

    @PostMapping("/uploadProducts") // //new annotation since 4.3
    public String uploadProducts(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
//            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "uploadProducts file is empty";
        }
        String message = null;
        if (uploadProductsHelper.hasExcelFormat(file)) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_Product_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);
                uploadProductsHelper.saveAll(file);
//                redirectAttributes.addFlashAttribute("message",
//                        "Uploaded the file successfully: '" + file.getOriginalFilename() + "'");
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
            } catch (Exception e) {
                e.printStackTrace();
                message = "Sorry!!! Could not upload the file: " + file.getOriginalFilename() + "!";
            }
        }
        return message;
    }

    @PostMapping("/uploadCustomers") // //new annotation since 4.3
    public String uploadCustomers(@RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadCustomers";
        }
        if (uploadProductsHelper.hasExcelFormat(file)) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_Customer_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);
                uploadCustomersHelper.saveAll(file);
                redirectAttributes.addFlashAttribute("message",
                        "Uploaded the file successfully: '" + file.getOriginalFilename() + "'");
//                message = "Uploaded the file successfully: " + file.getOriginalFilename();
            } catch (Exception e) {
                e.printStackTrace();
//                message = "Sorry!!! Could not upload the file: " + file.getOriginalFilename() + "!";
            }
        }
        return "Customers uploaded";
    }

    @PostMapping("/uploadLogo")
    public ResponseEntity<?> uploadLogo(@RequestParam("file")MultipartFile file) throws IOException {
        return new ResponseEntity<>(logoHelper.uploadLogo(file), HttpStatus.OK);
    }

    @GetMapping("/getLogo/{id}")
    public ResponseEntity<ByteArrayResource> getLogo(@PathVariable String id) throws IOException {
        Logo logo = logoHelper.getLogo(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(logo.getFileType() ))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + logo.getFilename() + "\"")
                .body(new ByteArrayResource(logo.getFile()));
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    @GetMapping("/uploadCustomers")
    public String uploadCustomers() {
        return "uploadCustomers";
    }

    @GetMapping("/uploadProducts")
    public String uploadProducts() {
        return "Products Uploaded";
    }

}
