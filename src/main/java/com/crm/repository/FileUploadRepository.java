package com.crm.repository;

import com.crm.model.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileUploadRepository extends JpaRepository<FileUpload, Integer> {
    List<FileUpload>  findByClientIdOrderByIdDesc(Long clientId);
}
