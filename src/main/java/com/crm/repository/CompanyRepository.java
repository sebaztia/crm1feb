package com.crm.repository;

import com.crm.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findByIsbn(String isBn);
    List<Company> findAllByInActiveTrue();
    List<Company> findAllByInActiveNull();
}
