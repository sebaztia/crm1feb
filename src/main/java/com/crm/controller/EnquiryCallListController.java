package com.crm.controller;

import com.crm.dto.EmailDto;
import com.crm.model.Addressbook;
import com.crm.model.CallList;
import com.crm.model.EnquiryCallList;
import com.crm.model.LegacyClient;
import com.crm.service.CallListService;
import com.crm.service.EmailService;
import com.crm.service.EnquiryCallListService;
import com.crm.service.LegacyClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class EnquiryCallListController {

    private CallListService callListService;
    @Autowired
    private LegacyClientService legacyClientService;
    private EmailService emailService;
    private static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    public EnquiryCallListController(CallListService callListService, EmailService emailService) {
        this.callListService = callListService;
        this.emailService = emailService;
    }

    @GetMapping("callList")
    public String enquiryModal(Model model) {
        model.addAttribute("data", callListService.getAllCallLists());
        return "show_enquiry_modal";
    }

    @PostMapping("/savedd2")
    public String saveAddBook2 (CallList callList) {
        Integer callId = callList.getId();
        if (null == callId) {
            callList.setCreatedBy(getUsername());
        }
        callList = callListService.saveCallListModal(callList);

        String reference = callList.getRefNumber();

        if ((null != reference && !reference.equals("")) && (null != callId && !callId.equals(""))) {

            LegacyClient legacyClient = legacyClientService.getLegacyClientByCallListId(callId);

            if (legacyClient == null) {
                legacyClient = new LegacyClient();
                legacyClient.setCallId(callId);
            }
            legacyClient.setFullName(callList.getContactName());
            legacyClient.setCallSheet(callList.getQuery());
            legacyClient.setRecordDate(callList.getCreatedAt());
            legacyClient.setRefNumber(callList.getRefNumber());
            legacyClient.setStaffName(callList.getStaffName());

            legacyClientService.save(legacyClient);
        }
        return "redirect:/callList";
    }

    @GetMapping("/findOneCallList")
    @ResponseBody
    public CallList findOneCallList (Integer id) {
       return callListService.getCallListById(id);
    }

    @GetMapping("/archive")
    public String archive (Integer id) {
        callListService.archiveCallListById(id);
        return "redirect:/callList";
    }

    @GetMapping("/findOneEmail")
    @ResponseBody
    public EmailDto findOneEmail (Integer id) {
        CallList callList = callListService.getCallListById(id);

        EmailDto emailDto = new EmailDto();
      //  emailDto.setCallList(callList);
        emailDto.setSubject("CRM Call List Information");
        emailDto.setText("Hi, \nPlease find the below call sheet details." +
                "\n\nContact Name: " + callList.getContactName() +
                "\nContact Number: " + callList.getContactNumber() +
                "\nQuery: " + callList.getQuery()+
                "\nStaff Name: " +callList.getStaffName());
        return emailDto;
    }

    @PostMapping("/sendEmail")
    public String sendEmail (EmailDto emailDto) {
        String subject = emailDto.getSubject();
        String text = emailDto.getText();
        if (null == subject || subject.equals("")) {
            subject = "CRM Call List Information";
        }
        if (null == text || text.equals("")) {
            text = "Please find the below call sheet details.";
        }
        emailService.sendSimpleMessage(emailDto.getTo(), subject, text);
        return "redirect:/callList";
    }

    @GetMapping("/emailDone/{id}")
    public String emailDone(@PathVariable(value = "id") Integer id) {

        this.callListService.emailActionDone(id);
        return "redirect:/callList";
    }
    @GetMapping("/callDone/{id}")
    public String callDone(@PathVariable(value = "id") Integer id) {

        this.callListService.callActionDone(id);
        return "redirect:/callList";
    }
    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
