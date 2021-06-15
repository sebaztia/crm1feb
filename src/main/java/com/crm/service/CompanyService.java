package com.crm.service;

import com.crm.dto.CompanyDto;
import com.crm.dto.Movie;
import com.crm.model.Company;
import com.crm.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CompanyService {

    private CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void saveProduct(CompanyDto companyDto) { companyRepository.save(toModel(companyDto)); }

    private Company toModel(CompanyDto dto) {
        Company company = new Company();
        company.setId(dto.getId());
        company.setName(dto.getName());
       String joining = dto.getMovies().stream().map(title -> title.getTitle()).collect(Collectors.joining(" , "));
        company.setContact(joining);
        company.setAddress(dto.getAddress());
        company.setTelephone(dto.getTelephone());
        company.setEmail(dto.getEmail());
        company.setWebsite(dto.getWebsite());
        company.setIsbn(dto.getIsbn());
        company.setClientSet(dto.getClientSet());
        company.setInActive(dto.getInActive());

        return company;
    }

    public Page<Company> getAllProduct(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    public void deleteCompany(Long id) {
        companyRepository.delete(id);
    }

    public CompanyDto getCompanyDtoById(Long id) { return toDto(companyRepository.findOne(id)); }
    public Company getCompanyById(Long id) { return companyRepository.findOne(id); }

    private CompanyDto toDto(Company company) {
        CompanyDto companyDto = new CompanyDto();

        companyDto.setId(company.getId());
        companyDto.setName(company.getName());
        String contact = company.getContact();
        List<Movie> bList = new ArrayList<>();
        if (null != contact) {
            /*String[] aList = contact.split(",");
            for (int i = 0; i < aList.length; i++) {
                if (i == 0)
                    bList.add(new Movie("Contact:", aList[i]));
                else
                    bList.add(new Movie("", aList[i]));
            }*/
            int i = 0;
            for (String title : contact.split(",")) {
                bList.add(new Movie(i++ == 0 ? "Contact:" : "", title.trim()));
            }
        } else {
            List moList = new ArrayList();
            bList.add(new Movie("Contact:", ""));
        }
        companyDto.setMovies(bList);
        companyDto.setAddress(company.getAddress());
        companyDto.setTelephone(company.getTelephone());
        companyDto.setEmail(company.getEmail());
        companyDto.setWebsite(company.getWebsite());
        companyDto.setIsbn(company.getIsbn());
        companyDto.setClientSet(company.getClientSet());
        companyDto.setInActive(company.getInActive());

        return companyDto;
    }

    public Long countAll() { return  companyRepository.count(); }

    public List<Company> findAll() { return  companyRepository.findAll(); }

    public List<Company> findAllActive() { return companyRepository.findAllByInActiveNull(); }

    public List<Company> findAllInactive() { return companyRepository.findAllByInActiveTrue(); }

    public void switchCompany(Long id) {
        Company company = companyRepository.findOne(id);
        Boolean inActive = company.getInActive();
        company.setInActive(inActive == null ? true : null);
        companyRepository.save(company);

    }
}
