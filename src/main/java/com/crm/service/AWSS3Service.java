package com.crm.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class AWSS3Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(AWSS3Service.class);

    private AmazonS3 amazonS3;
    @Value("${aws.bucketName}")
    private String bucketName;

    public AWSS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    /** @Async annotation ensures that the method is executed in a different background thread
    but not consume the main thread. */
    @Async
    public String uploadFile(final MultipartFile multipartFile) {
        String filename = null;
        LOGGER.info("File upload in progress::: bucketName: " + bucketName);
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            filename = uploadFileToS3Bucket(bucketName, file);
            LOGGER.info("File upload is completed.");
            file.delete();  // To remove the file locally created in the project folder.
        } catch (final AmazonServiceException ex) {
            LOGGER.info("File upload is failed.");
            LOGGER.error("Error= {} while uploading file.", ex.getMessage());
        }
        return filename;
    }

    @Async
    public byte[] downloadFile(final String keyName) {
        byte[] content = null;
        LOGGER.info("Downloading an object with key= " + keyName);
        final S3Object s3Object = amazonS3.getObject(bucketName, keyName);
        final S3ObjectInputStream stream = s3Object.getObjectContent();
        try {
            content = IOUtils.toByteArray(stream);
            LOGGER.info("File downloaded successfully.");
            s3Object.close();
        } catch(final IOException ex) {
            LOGGER.info("IO Error Message= " + ex.getMessage());
        }
        return content;
    }

    public List<String> getAllFileNameInBucket(String bucketNames) {
        List<String> bucketLst = new ArrayList<>();
        ObjectListing objectListing = amazonS3.listObjects(bucketName);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        for(S3ObjectSummary objectSummary : objectSummaries){
            bucketLst.add(objectSummary.getKey());
        }
        return bucketLst;
    }

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ex) {
            LOGGER.error("Error converting the multi-part file to file= ", ex.getMessage());
        }
        return file;
    }
    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
    private String uploadFileToS3Bucket(final String bucketName, final File file) {
        final String uniqueFileName = getFileNameWithoutExtension(file) + "_" + df.format(new Date()) + "." + getFileExtension(file);
        LOGGER.info("Uploading file with name::: " + uniqueFileName);
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);
        amazonS3.putObject(putObjectRequest);

        return uniqueFileName;
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

    private static final Pattern ext = Pattern.compile("(?<=.)\\.[^.]+$");

    public static String getFileNameWithoutExtension(File file) {
        return ext.matcher(file.getName()).replaceAll("");
    }
}
