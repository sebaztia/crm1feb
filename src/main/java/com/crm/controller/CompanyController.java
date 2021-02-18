package com.crm.controller;

import com.crm.model.Client;
import com.crm.model.Company;
import com.crm.service.ClientService;
import com.crm.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class CompanyController {

    private static int currentPage = 1;
    private static int pageSize = 5;

    private CompanyService companyService;
    private ClientService clientService;

    @Autowired
    public CompanyController(CompanyService companyService, ClientService clientService) {
        this.companyService = companyService;
        this.clientService = clientService;
    }

    @GetMapping("companyAdd")
    public String addCompany(@Valid Model model) {
        model.addAttribute("company", new Company());

        return "add_company";
    }

    @PostMapping("companySave")
    public String save(@ModelAttribute("company") @Valid Company company, BindingResult result) {

        if (result.hasErrors()) {
            return "add_company";
        }
        companyService.saveProduct(company);

        return "redirect:/company";
    }
    @PostMapping("companyEdit/companySave")
    public String editSave(@ModelAttribute("company") @Valid Company company, BindingResult result) {

        if (result.hasErrors()) {
            return "add_company";
        }
        companyService.saveProduct(company);

        return "redirect:/company";
    }

    @GetMapping("company")
    public String index(Model model/*, @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size*/) {
        /*page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);

        Pageable pageable = *//*PageRequest.of*//* new PageRequest(currentPage - 1, pageSize);
        Page<Company> companyPage = companyService.getAllProduct(pageable);

        model.addAttribute("companyPage", companyPage);

        int totalPages = companyPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }*/

        model.addAttribute("companyList", companyService.findAll());

        return "company_list";
    }

    @RequestMapping(value = "companyDelete/{id}", method = RequestMethod.GET)
    public String deleteCompany(@PathVariable("id") Long id) {
        companyService.deleteCompany(id);

        return "redirect:/company";
    }

    @GetMapping(value = "companyEdit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        Company product = companyService.getCompanyById(id);
        model.addAttribute("company", product);

        return "add_company";
    }

    @GetMapping("/showCompany/{id}")
    public String showReference(@PathVariable(value = "id") long id, Model model) {

        Company company = companyService.getCompanyById(id);
        List<Client> clientList = clientService.findByCompany(company);

        model.addAttribute("show_company", company);
        model.addAttribute("linkedClients", clientList);
        return "show_company";
    }


}
