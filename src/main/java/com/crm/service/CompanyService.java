package com.crm.service;

import com.crm.model.Company;
import com.crm.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CompanyService {

    private CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void saveProduct(Company company) { companyRepository.save(company); }

    public Page<Company> getAllProduct(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    public void deleteCompany(Long id) {
        companyRepository.delete(id);
    }

    public Company getCompanyById(Long id) { return companyRepository.findOne(id); }

    public Long countAll() { return  companyRepository.count(); }

    public List<Company> findAll() { return  companyRepository.findAll(); }
}
