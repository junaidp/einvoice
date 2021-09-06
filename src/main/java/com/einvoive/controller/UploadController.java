package com.einvoive.controller;

import com.einvoive.helper.UploadProductsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "C://help//";
    private static String UPLOAD_Product_FOLDER = "C://products//";

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

    @PostMapping("/uploadProducts") // //new annotation since 4.3
    public String uploadProducts(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadProducts";
        }
        if (uploadProductsHelper.hasExcelFormat(file)) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_Product_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);
                uploadProductsHelper.saveAll(file);
                redirectAttributes.addFlashAttribute("message",
                        "Uploaded the file successfully: '" + file.getOriginalFilename() + "'");
//                message = "Uploaded the file successfully: " + file.getOriginalFilename();
            } catch (Exception e) {
                e.printStackTrace();
//                message = "Sorry!!! Could not upload the file: " + file.getOriginalFilename() + "!";
            }
        }
        return "redirect:/uploadProducts";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    @GetMapping("/uploadProducts")
    public String uploadProducts() {
        return "Products Uploaded";
    }

}
