package com.crm.service;

import com.crm.dto.CompanyDto;
import com.crm.model.Company;
import com.crm.model.Contact;
import com.crm.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CompanyService {

    private CompanyRepository companyRepository;
    private ContactService contactService;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, ContactService contactService) {
        this.companyRepository = companyRepository;
        this.contactService = contactService;
    }

    private static Logger logger = LoggerFactory.getLogger(CompanyService.class);

    public void saveProduct(CompanyDto companyDto) {
        Company company = companyRepository.save(toModel(companyDto));
        Long companyId = company.getId();
        List<Contact> contactList = companyDto.getContacts();

        contactList.forEach(f -> f.setCompanyId(companyId));
        contactService.save(contactList);
    }

    private Company toModel(CompanyDto dto) {
        Company company = new Company();
        company.setId(dto.getId());
        company.setName(dto.getName());
      // String joining = dto.getMovies().stream().map(title -> title.getTitle()).collect(Collectors.joining(" , "));
      //  company.setContact(joining);
        company.setAddress(dto.getAddress());
       // String teleJoin = dto.getMovies().stream().map(phone -> phone.getPhone()).collect(Collectors.joining(" , "));
       // company.setTelephone(teleJoin);
      //  String emailJoin = dto.getMovies().stream().map(email -> email.getEml()).collect(Collectors.joining(" , "));
      //  company.setEmail(emailJoin);
        company.setWebsite(dto.getWebsite());
        company.setIsbn(dto.getIsbn());
        company.setClientSet(dto.getClientSet());
        company.setInActive(dto.getInActive());
      //  company.setContactSet(dto.getContacts());

        return company;
    }

    public Page<Company> getAllProduct(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    public void deleteCompany(Long id) {
        companyRepository.delete(id);
    }

    public CompanyDto getCompanyDtoById(Long id) {

        return toDto(companyRepository.findOne(id));
    }
    public Company getCompanyById(Long id) { return companyRepository.findOne(id); }

    private CompanyDto toDto(Company company) {
        CompanyDto companyDto = new CompanyDto();

        companyDto.setId(company.getId());
        companyDto.setName(company.getName());
      //  String contact = company.getContact();
      /*  List<Movie> bList = new ArrayList<>();
        if (null != contact) {
            *//*String[] aList = contact.split(",");
            for (int i = 0; i < aList.length; i++) {
                if (i == 0)
                    bList.add(new Movie("Contact:", aList[i]));
                else
                    bList.add(new Movie("", aList[i]));
            }*//*
            int i = 0;
            for (String title : contact.split(",")) {
                bList.add(new Movie(i++ == 0 ? "Contact:" : "", title.trim()));
            }
        } else {
            List moList = new ArrayList();
            bList.add(new Movie("Contact:", ""));
        }*/
      //  companyDto.setMovies(bList);
        companyDto.setAddress(company.getAddress());
     //   companyDto.setTelephone(company.getTelephone());
      //  companyDto.setEmail(company.getEmail());
        companyDto.setWebsite(company.getWebsite());
        companyDto.setIsbn(company.getIsbn());
        companyDto.setClientSet(company.getClientSet());
        companyDto.setInActive(company.getInActive());
        List<Contact> contactList = contactService.findAllByClientId(company.getId());
        companyDto.setContacts(contactList);
        String title = "";
        String phone = "";
        String eml = "";
        for (Contact con: contactList) {
            title = title + con.getTitle() + ", ";
            phone = phone + con.getPhone() + ", ";
            eml = eml + con.getEml() + ", ";
        }
        companyDto.setContact(title);
        companyDto.setTelephone(phone);
        companyDto.setEmail(eml);
        companyDto.setCreatedAt(company.getCreatedAt());

        return companyDto;
    }

    public Long countAll() { return  companyRepository.count(); }

    public List<Company> findAll() { return  companyRepository.findAll(); }

    public List<CompanyDto> findAllActive() {
        return companyRepository.findAllByInActiveNull().stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<CompanyDto> findAllInactive() { return companyRepository.findAllByInActiveTrue().stream().map(this::toDto).collect(Collectors.toList()); }

    public void switchCompany(Long id) {
        Company company = companyRepository.findOne(id);
        Boolean inActive = company.getInActive();
        company.setInActive(inActive == null ? true : null);
        companyRepository.save(company);

    }
}
