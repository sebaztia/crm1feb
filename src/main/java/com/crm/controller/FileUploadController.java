package com.crm.controller;

import com.crm.model.FileUpload;
import com.crm.service.AWSS3Service;
import com.crm.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileUploadController {

    AWSS3Service awss3Service;
    FileUploadService fileUploadService;

    @Autowired
    public FileUploadController(AWSS3Service awss3Service, FileUploadService fileUploadService) {
        this.awss3Service = awss3Service;
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(value = "/upload/{clientId}")
    public /*ResponseEntity<*/String uploadFile(@RequestParam(value = "file") final MultipartFile multipartFile, @PathVariable("clientId") long clientId, RedirectAttributes attributes) {
        logger.info("========== clientId::: " + clientId);
        FileUpload fileUpload = new FileUpload(awss3Service.uploadFile(multipartFile), clientId, "");
        fileUploadService.save(fileUpload);
        final String response = "[" + multipartFile.getOriginalFilename() + "] uploaded successfully.";

        attributes.addAttribute("id", clientId);
        return "redirect:/showClient/{id}";
    }

    private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @GetMapping(value = "/downloadFile/{fileName:.+}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(value = "fileName") final String keyName) {
        logger.info("==== fileName:::" + keyName);
        final byte[] data = awss3Service.downloadFile(keyName);
        final ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + keyName + "\"")
                .body(resource);
    }
}
