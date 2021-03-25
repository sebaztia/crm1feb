package com.crm.controller;

import com.crm.service.CallListService;
import com.crm.service.ClientService;
import com.crm.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {

    private ClientService clientService;
    private CompanyService companyService;
    private CallListService callListService;

    @Autowired
    public MainController(ClientService clientService, CompanyService companyService, CallListService callListService) {
        this.clientService = clientService;
        this.companyService = companyService;
        this.callListService = callListService;
    }

    @GetMapping("/showWillsPage")
    public String showWillsPage(Model model) {
       /* Integer yesClient = clientService.countByClearedTrue();
        Integer noClient = clientService.countByClearedFalseOrClearedNull();
        Integer midwestSales = 1;
        Integer southSales = 1;*/



        model.addAttribute("totalCompanies", companyService.countAll());
        model.addAttribute("totalClients", clientService.countAll());
        model.addAttribute("executed", clientService.countByClearedTrue());
        model.addAttribute("deceased", clientService.countByDeceasedTrue());

        //now add sales by lure type
        /*List<Integer> inshoreSales = Arrays.asList(4074, 3455, 4112);
        List<Integer> nearshoreSales = Arrays.asList(3222, 3011, 3788);
        List<Integer> offshoreSales = Arrays.asList(7811, 7098, 6455);

        model.addAttribute("inshoreSales", inshoreSales);
        model.addAttribute("nearshoreSales", nearshoreSales);
        model.addAttribute("offshoreSales", offshoreSales);*/
        return "wills_dashboard";
    }

    @GetMapping("/probatePage")
    public String probatePage(Model model) {
        model.addAttribute("totalLeadsClients", callListService.countByIsLeads());
        return "probate_dashboard";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handlerException() {
        return "error/404";
    }
}
