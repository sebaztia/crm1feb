package com.crm.controller;

import com.crm.dto.EmailDto;
import com.crm.model.*;
import com.crm.service.*;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.Valid;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

@Slf4j
@Controller
public class CallListController {

    private CallListService callListService;
    @Autowired
    private EnquiryCallListService enquiryCallListService;
    private StaffService staffService;
    private LegacyClientService legacyClientService;
    private EmailService emailService;

    @Autowired
    public CallListController(CallListService callListService, StaffService staffService, LegacyClientService legacyClientService, EmailService emailService) {
        this.callListService = callListService;
        this.staffService = staffService;
        this.legacyClientService = legacyClientService;
        this.emailService = emailService;
    }

    @GetMapping("callListOld")
    public String viewEnquiryCallList(Model model) {
        model.addAttribute("listCalls", callListService.getAllCallLists());
        return "show_enquiry_call_list";
    }

    @GetMapping("callListAdd")
    public String addCallList(@Valid Model model) {
        CallList callList = new CallList();
      //  callList.setRecordDate(new Date());
        List<Staff> staffList = staffService.getAllStaff();
        model.addAttribute("call_list", callList);
        model.addAttribute("staffList", staffList);

        return "add_call_list";
    }

    @GetMapping(value = "callListEdit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model) {
        CallList callList  = callListService.getCallListById(id);
        List<Staff> staffList = staffService.getAllStaff();
        model.addAttribute("call_list", callList);
        model.addAttribute("staffList", staffList);

        return "edit_call_list";
    }
    @GetMapping("callListEmail/{id}")
    public String showCallListEmailForm(@PathVariable(value = "id") Integer id, Model model) {

        CallList callList = callListService.getCallListById(id);

        EmailDto emailDto = new EmailDto();
        emailDto.setCallList(callList);
        emailDto.setSubject("CRM Call List Information");
        emailDto.setText("Hi, \nPlease find the below call sheet details." +
                "\n\nContact Name: " + callList.getContactName() +
                "\nContact Number: " + callList.getContactNumber() +
                "\nQuery: " + callList.getQuery()+
                "\nStaff Name: " +callList/*.getStaff()*/.getStaffName());

        model.addAttribute("email_list", emailDto);
        return "email_call_list";
    }

    @PostMapping("callListSave")
    public String save(@ModelAttribute("call_list") @Valid CallList callList, BindingResult result, Model model) {

        if (result.hasErrors()) {
            List<Staff> staffList = staffService.getAllStaff();
            model.addAttribute("staffList", staffList);
            return "add_call_list";
        }
        callListService.saveCallList(callList);

        return "redirect:/callList";
    }

    @PostMapping(value = "callListUpdate")
    public String update(@ModelAttribute("call_list") @Valid CallList callList, BindingResult result, Model model) {

        if (result.hasErrors()) {
            List<Staff> staffList = staffService.getAllStaff();
            model.addAttribute("staffList", staffList);
            return "edit_call_list";
        }
        callListService.saveCallList(callList);

        Integer callId = callList.getId();
        String reference = callList.getRefNumber();

        if ((null != reference && !reference.equals("")) && (null != callId && !callId.equals(""))) {

            LegacyClient legacyClient = legacyClientService.getLegacyClientByCallListId(callId);

            if (legacyClient == null) {
                legacyClient = new LegacyClient();
                legacyClient.setCallId(callList.getId());
            }
            legacyClient.setFullName(callList.getContactName());
            legacyClient.setCallSheet(callList.getQuery());
         //   legacyClient.setRecordDate(callList.getRecordDate());
            legacyClient.setRefNumber(callList.getRefNumber());
            legacyClient.setStaffName(callList./*getStaff().*/getStaffName());

            legacyClientService.save(legacyClient);
        }

        return "redirect:/callList";
    }


    @PostMapping("sendEmailCallList")
    public String sendEmailCallList(@ModelAttribute("email_list") @Valid EmailDto emailDto, BindingResult result) {

        if (result.hasErrors()) {
            return "email_call_list";
        }
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
    @RequestMapping("/archiveCallList/{id}")
    public String archiveCallList(@PathVariable(value = "id") Integer id) {

        this.callListService.archiveCallListById(id);
        return "redirect:/callList";
    }
    @GetMapping("/restoreCallList/{id}")
    public String restoreCallList(@PathVariable(value = "id") Integer id) {

        this.callListService.rollbackCallListById(id);
        return "redirect:/showArchive";
    }
    @GetMapping("/deleteCallList/{id}")
    public String deleteCallList(@PathVariable(value = "id") Integer id) {

        this.callListService.deleteCallListById(id);
        return "redirect:/showArchive";
    }

    @RequestMapping("/showArchive")
    public String showArchive(Model model) {

        model.addAttribute("archiveList", callListService.getAllArchiveList());
        return "archive_list";
    }

    @GetMapping("/emailActionDone/{id}")
    public String emailActionDone(@PathVariable(value = "id") Integer id) {

        this.callListService.emailActionDone(id);
        return "redirect:/callList";
    }
    @GetMapping("/callActionDone/{id}")
    public String callActionDone(@PathVariable(value = "id") Integer id) {

        this.callListService.callActionDone(id);
        return "redirect:/callList";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handlerException() {
        return "error/404";
    }
}
