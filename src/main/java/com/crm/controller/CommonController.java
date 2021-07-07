package com.crm.controller;

import com.crm.model.Client;
import com.crm.model.ClientStatus;
import com.crm.model.Company;
import com.crm.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommonController {

    private CommonService commonService;

    @Autowired
    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping("addEstateProperty/{id}")
    public String addEstateProperty(@PathVariable("id") Long clientId, Model model) {

        model.addAttribute("estateProperties", commonService.getEstateProperty(clientId));// getEstate());
        model.addAttribute("kinInfo", commonService.getKinInfo(clientId));
        model.addAttribute("familyContacts", commonService.getFamilyContacts(clientId));

        return "estate_property";
    }

    @GetMapping("/estateReports")
    public String estateReports(Model model) {

        model.addAttribute("estateReport", commonService.findReports());
        return "estate_reports";
    }

    @GetMapping("/searchReport")
    public String searchReport(HttpServletRequest request, Model model) {

        String eWorth = request.getParameter("eWorth");
        String eValue = request.getParameter("eValue");
        String textWorth = request.getParameter("textWorth");
        String textValue = request.getParameter("textValue");
        String choiceradio = request.getParameter("choice-radio");
        String maritalStatus = request.getParameter("maritalStatus");

        model.addAttribute("estateReport", commonService.findReports(eWorth, textWorth, eValue, textValue, choiceradio, maritalStatus));
        return "estate_reports";
    }
}
