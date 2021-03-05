package com.crm.repository;

import com.crm.model.SrcPng;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SrcRepository extends JpaRepository<SrcPng, Integer> {
    public SrcPng findByAuthor(String author);
}
