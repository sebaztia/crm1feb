package com.crm.controller;

import com.crm.config.Utility;
import com.crm.model.*;
import com.crm.service.ClientService;
import com.crm.service.CommentsService;
import com.crm.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class ClientController {

    private CompanyService companyService;
    private ClientService clientService;
    private CommentsService commentsService;
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    public ClientController(CompanyService companyService, ClientService clientService, CommentsService commentsService) {
        this.companyService = companyService;
        this.clientService = clientService;
        this.commentsService = commentsService;
    }

    @GetMapping("clientAdd/{id}")
    public String addClient(@PathVariable("id") Long id, Model model) {

        Company company = companyService.getCompanyById(id);
        Client client = new Client();
        client.setCompany(company);
        model.addAttribute("company", company);
        model.addAttribute("client", client);

        return "add_client";
    }
    @GetMapping(value = "editClient/{id}")
    public String editClient(@PathVariable("id") Long id, Model model) {
        Client client = clientService.getClientById(id);
        Company company = companyService.getCompanyById(client.getCompany().getId());

        model.addAttribute("company", company);
        model.addAttribute("client", client);
        return "add_client";
    }

    @RequestMapping("clientAdd/clientSave")
    public String save(@ModelAttribute("client") @Valid Client client, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Company company = client.getCompany();
            model.addAttribute("company", company);
            return "add_client";
        }
       clientService.saveClient(client);
        return "redirect:/listClient";
    }
    @RequestMapping("editClient/clientSave")
    public String editSave(@ModelAttribute("client") @Valid Client client, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Company company = client.getCompany();
            model.addAttribute("company", company);
            return "add_client";
        }
        clientService.saveClient(client);
        return "redirect:/listClient";
    }
    @RequestMapping("/listClient")
    public String listClient(Model model) {

        model.addAttribute("clientList", clientService.findAll());
        return "clients_list";
    }

    @RequestMapping(value = "deleteClient/{id}", method = RequestMethod.GET)
    public String deleteClient(@PathVariable("id") Long id) {
        clientService.deleteCompany(id);

        return "redirect:/listClient";
    }
    @GetMapping("showClient/{id}")
    public String showClient(@PathVariable(value = "id") long id, Model model) {

        Client client = clientService.getClientById(id);
        List<Comments> commentsList = commentsService.findByClient(client);

        model.addAttribute("show_client", client);
        model.addAttribute("linkedComments", commentsList);
        return "sow_client";
    }

    @PostMapping("/submit_message")
    public String submit_message(HttpServletRequest request, Model model) {
        String message = request.getParameter("message");
        logger.info("====== message: " + message);
        String author = request.getParameter("author");
        logger.info("====== author: " + author);
        Long id = Long.valueOf(request.getParameter("id"));
        logger.info("====== ididid: " + id);


        Client client = clientService.getClientById(id);
        Comments comments = new Comments(message, author, client);
        commentsService.save(comments);
        List<Comments> commentsList = commentsService.findByClient(client);

        model.addAttribute("show_client", client);
        model.addAttribute("linkedComments", commentsList);

        return "sow_client";
    }

    /*@PostMapping("/posts/{postId}/comments")
    public Client createComment(@PathVariable (value = "postId") Long postId,
                                 @Valid @RequestBody Client comment) throws ResourceNotFoundException {
        return companyRepository.findById(postId).map(post -> {
            comment.setCompany(post);
            return clientRepository.save(comment);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }*/
}
