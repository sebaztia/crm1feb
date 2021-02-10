package com.crm.service;

import com.crm.model.FileUpload;
import com.crm.repository.FileUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileUploadService {
    private FileUploadRepository fileUploadRepository;

    @Autowired
    public FileUploadService(FileUploadRepository fileUploadRepository) {
        this.fileUploadRepository = fileUploadRepository;
    }

    public List<FileUpload> getAllByClientId(Long clientId) { return fileUploadRepository.findByClientIdOrderByIdDesc(clientId); }

    public void save (FileUpload fileUpload) { fileUploadRepository.save(fileUpload); }
}
