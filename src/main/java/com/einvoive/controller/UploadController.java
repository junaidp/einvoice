package com.einvoive.controller;

import com.einvoive.constants.Constant;
import com.einvoive.helper.CompanyHelper;
import com.einvoive.helper.LogoHelper;
import com.einvoive.helper.UploadCustomersHelper;
import com.einvoive.helper.UploadProductsHelper;
import com.einvoive.model.ErrorCustom;
import com.einvoive.model.Logo;
import com.google.gson.Gson;
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
    private static String UPLOADED_INVOICE_FOLDER = "D://Envoice//invoice//";
    private static String UPLOADED_FOLDER = "D://Envoice//help//";
    private static String UPLOAD_Product_FOLDER = "D://Envoice//products//";
    private static String UPLOAD_Customer_FOLDER = "D://Envoice//customers//";
    private static String UPLOAD_Logos_FOLDER = "D://Envoice//logos//";
    private Gson gson;

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
            Path path = Paths.get(UPLOADED_INVOICE_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    public String uploadInvoice(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty())
            return "Attached file is empty";

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_INVOICE_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            return "You successfully uploaded " + file.getOriginalFilename();
        } catch (IOException e) {
            return e.getMessage();
        }
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
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        if (file.isEmpty()) {
            error.setErrorStatus("Error");
            error.setError("Attach file is empty");
            jsonError = gson.toJson(error);
            return jsonError;
        }
        if (uploadProductsHelper.hasExcelFormat(file)) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_Product_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);
                uploadProductsHelper.saveAll(file, Constant.COMPANY_ID, Constant.LOGGED_IN_USER_ID);
                return gson.toJson("Uploaded the file successfully: " + file.getOriginalFilename());
            } catch (Exception e) {
                error.setErrorStatus("Error");
                error.setError(e.getMessage());
                jsonError = gson.toJson(error);
                return jsonError;
            }
        }
        else {
            error.setErrorStatus("Error");
            error.setError("Attach file format is not excell");
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

    @PostMapping("/uploadCustomers") // //new annotation since 4.3
    public String uploadCustomers(@RequestParam("file") MultipartFile file) {
        ErrorCustom error = new ErrorCustom();
        String jsonError;
        if (file.isEmpty()) {
            error.setErrorStatus("Error");
            error.setError("Attach file is empty");
            jsonError = gson.toJson(error);
            return jsonError;
        }
        if (uploadProductsHelper.hasExcelFormat(file)) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_Customer_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);
                uploadCustomersHelper.saveAll(file);
                return gson.toJson("Uploaded the file successfully: " + file.getOriginalFilename());
//                message = "Uploaded the file successfully: " + file.getOriginalFilename();
            } catch (Exception e) {
                error.setErrorStatus("Error");
                error.setError(e.getMessage());
                jsonError = gson.toJson(error);
                return jsonError;
//                message = "Sorry!!! Could not upload the file: " + file.getOriginalFilename() + "!";
            }
        }
        else {
            error.setErrorStatus("Error");
            error.setError("Attach file format is not excell");
            jsonError = gson.toJson(error);
            return jsonError;
        }
    }

    @PostMapping("/uploadLogo")
    public ResponseEntity<?> uploadLogo(@RequestParam("file")MultipartFile file) throws IOException {
        return new ResponseEntity<>(logoHelper.uploadLogo(file), HttpStatus.OK);
    }

    @GetMapping("/getLogo")
    public ResponseEntity<ByteArrayResource> getLogo(@RequestParam String companyID) throws IOException {
        Logo logo = logoHelper.getLogo(companyID);
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
