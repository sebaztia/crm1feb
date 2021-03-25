package com.crm.controller;

import com.crm.model.CallList;
import com.crm.model.Client;
import com.crm.model.Comments;
import com.crm.model.Company;
import com.crm.service.CallListService;
import com.crm.service.ClientService;
import com.crm.service.CommentsService;
import com.crm.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LeadsController {

    private CallListService callListService;
    private ClientService clientService;
    private CompanyService companyService;
    private CommentsService commentsService;

    @Autowired
    public LeadsController(CallListService callListService, ClientService clientService, CompanyService companyService,
                           CommentsService commentsService) {
        this.callListService = callListService;
        this.clientService = clientService;
        this.companyService = companyService;
        this.commentsService = commentsService;
    }

    @RequestMapping("/leadsClient")
    public String leadsClient(Model model) {

        model.addAttribute("listCalls", callListService.getAllLeadsClient());
        return "leads_clients";
    }

    @GetMapping(value = "makeLeads/{id}")
    public String makeLeads(@PathVariable("id") Long clientId, Model model, RedirectAttributes attributes) {

        CallList callList = callListService.getCallListByClientId(clientId);
        Client client = clientService.getClientById(clientId);
        client.setIsLeads(true);
        callList.setIsLeads(true);
        clientService.saveClient(client);
        callListService.saveCallList(callList);
        attributes.addAttribute("id", clientId);
        return "redirect:/showClient/{id}";
    }

    @GetMapping(value = "makeLeadsCallList/{id}")
    public String makeLeadsCallList(@PathVariable("id") Integer callListId) {

        CallList callList = callListService.getCallListById(callListId);
        callList.setIsLeads(true);
        Client client = clientService.findByCallListId(callListId);
        if (client == null) {
            client = new Client();
            client.setName(callList.getContactName());
            client.setPhone(callList.getContactNumber());
            client.setContact("Call List");
            client.setStatus("First Contact");
            client.setCompany(companyService.getCompanyById(101L));
            client.setCallListId(callListId);
            client.setIsLeads(true);
            client = clientService.saveClient(client);

            commentsService.save(new Comments(callList.getQuery(), getUsername(), client));

            callList.setClientId(client.getId());
            callListService.saveCallList(callList);
        }
        return "redirect:/callList";
    }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
