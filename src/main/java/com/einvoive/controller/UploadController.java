package com.einvoive.controller;

import com.einvoive.constants.Constants;
import com.einvoive.helper.*;
import com.einvoive.model.ErrorCustom;
import com.einvoive.model.Logo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
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

    @Autowired
    InvoiceHelper invoiceHelper;

     //Save the uploaded file to this folder
    private static String UPLOADED_INVOICE_FOLDER = Constants.PROJECT_PATH+"//invoice//";
    private static String UPLOADED_FOLDER = Constants.PROJECT_PATH+"//help//";
    private static String UPLOAD_Product_FOLDER = Constants.PROJECT_PATH+"//products//";
    private static String UPLOAD_Customer_FOLDER = Constants.PROJECT_PATH+"//customers//";
    private static String UPLOAD_Logos_FOLDER = Constants.PROJECT_PATH+"//logos//";
    private Gson gson = new Gson();

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

    @PostMapping("/uploadInvoiceAttachment")
    public String uploadInvoiceAttachment(@RequestParam("file") MultipartFile file, @RequestParam String invoiceNo){
        if (file.isEmpty())
            return gson.toJson("Attached file is empty");
//        System.out.println(file.getContentType());
//        if(file.getContentType().substring(file.getContentType().length()-3, file.getContentType().length()).equalsIgnoreCase("pdf"))
//            return invoiceHelper.uploadFile(file);
        else
            return invoiceHelper.uploadFile(file, invoiceNo);
//        return gson.toJson("Attached file is invalid format");
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
    public ResponseEntity<String> uploadProducts(@RequestParam("file") MultipartFile file, @RequestParam String companyID, @RequestParam String userID) {
//        ErrorCustom error = new ErrorCustom();
//        String jsonError;
        if (file.isEmpty()) {
//            error.setErrorStatus("Error");
//            error.setError("Attach file is empty");
//            jsonError = gson.toJson(error);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Attached file is empty");
        }
        if (uploadProductsHelper.hasExcelFormat(file)) {
            try {
                return ResponseEntity.status(HttpStatus.OK).body(uploadProductsHelper.saveAll(file, companyID, userID));
//                byte[] bytes = file.getBytes();
//                Path path = Paths.get(UPLOAD_Product_FOLDER + file.getOriginalFilename());
//                Files.write(path, bytes);
//                return uploadProductsHelper.saveAll(file, companyID, userID);
//                return gson.toJson("Uploaded the file successfully: " + file.getOriginalFilename());
            } catch (Exception e) {
//                error.setErrorStatus("Error");
//                error.setError(e.getMessage());
//                jsonError = gson.toJson(error);
//                return jsonError;
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
            }
        }
        else {
//            error.setErrorStatus("Error");
//            error.setError("Attach file format is not excell");
//            jsonError = gson.toJson(error);
//            return jsonError;
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Attach file format is not excell");
        }
    }

    @PostMapping("/uploadCustomers") // //new annotation since 4.3
    public ResponseEntity<String> uploadCustomers(@RequestParam("file") MultipartFile file, @RequestParam String companyID) {
//        ErrorCustom error = new ErrorCustom();
//        String jsonError;
        if (file.isEmpty()) {
//            error.setErrorStatus("Error");
//            error.setError("Attach file is empty");
//            jsonError = gson.toJson(error);
//            return jsonError;
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Attached file is empty");
        }
        if (uploadProductsHelper.hasExcelFormat(file)) {
            try {
//                byte[] bytes = file.getBytes();
//                Path path = Paths.get(UPLOAD_Customer_FOLDER + file.getOriginalFilename());
//                Files.write(path, bytes);
                return ResponseEntity.status(HttpStatus.OK).body(uploadCustomersHelper.saveAll(file, companyID));
//                return gson.toJson("Uploaded the file successfully: " + file.getOriginalFilename());
//                message = "Uploaded the file successfully: " + file.getOriginalFilename();
            } catch (Exception e) {
//                error.setErrorStatus("Error");
//                error.setError(e.getMessage());
//                jsonError = gson.toJson(error);
//                return jsonError;
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
            }
        }
        else {
//            error.setErrorStatus("Error");
//            error.setError("Attach file format is not excell");
//            jsonError = gson.toJson(error);
//            return jsonError;
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Attach file format is not excell");
        }
    }

    @PostMapping("/uploadLogo")
    public ResponseEntity<?> uploadLogo(@RequestParam("file")MultipartFile file, @RequestParam String companyID) throws IOException {
        return new ResponseEntity<>(logoHelper.uploadLogo(file, companyID), HttpStatus.OK);
    }

    @GetMapping("/getLogo")
    public ResponseEntity<ByteArrayResource> getLogo(@RequestParam String companyID) throws IOException {
        Logo logo = logoHelper.getLogo(companyID);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(logo.getFileType() ))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + logo.getFilename() + "\"")
                .body(new ByteArrayResource(logo.getFile()));
    }

    @RequestMapping(value ="/loadInvoiceAttachment", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> load(@RequestParam String invoiceNo) throws MalformedURLException {
        Resource file = invoiceHelper.load(invoiceNo);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

//    @RequestMapping(value = "/loadInvoiceAttachment", method = RequestMethod.GET, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public String load(@RequestParam String invoiceNo) throws IOException {
//        String path = invoiceHelper.load(invoiceNo).getURL().getPath();
//        return gson.toJson(path);
////        return ResponseEntity.ok()
////                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
//    }

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
