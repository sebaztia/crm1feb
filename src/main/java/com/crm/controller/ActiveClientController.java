package com.crm.controller;

import com.crm.config.MyAccessDeniedHandler;
import com.crm.model.Client;
import com.crm.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
public class ActiveClientController {

    private ClientService clientService;
    private static Logger logger = LoggerFactory.getLogger(ActiveClientController.class);

    @Autowired
    public ActiveClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @RequestMapping("/activeClient")
    public String activeClient(Model model) {
        List<Client> firstLists = clientService.findByStatus("First Contact");
        List<Client> instructionsList = clientService.findByStatus("Instructions");
        List<Client> draftingList = clientService.findByStatus("Drafting");
        List<Client> signingList = clientService.findByStatus("Signing");
        List<Client> checkingList = clientService.findByStatus("Checking");
        List<Client> storedList = clientService.findByStatus("Stored");
        List<Client> archivedList = clientService.findByStatus("Archived");

        model.addAttribute("firstLists", firstLists);
        model.addAttribute("instructionsList", instructionsList);
        model.addAttribute("draftingList", draftingList);
        model.addAttribute("signingList", signingList);
        model.addAttribute("checkingList", checkingList);
        model.addAttribute("storedList", storedList);
        model.addAttribute("archivedList", archivedList);

        return "active_client_dashboard";
    }
}
