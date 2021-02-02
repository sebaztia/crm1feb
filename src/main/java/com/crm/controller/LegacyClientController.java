package com.crm.controller;

import com.crm.dto.EmailDto;
import com.crm.model.LegacyClient;
import com.crm.model.Product;
import com.crm.service.EmailService;
import com.crm.service.LegacyClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class LegacyClientController {

    private LegacyClientService legacyClientService;
    private EmailService emailService;

    private static int currentPage = 1;
    private static int pageSize = 5;
    private static final Logger LOG = Logger.getLogger(ProductController.class.getName());

    @Autowired
    public LegacyClientController(LegacyClientService legacyClientService, EmailService emailService) {
        this.legacyClientService = legacyClientService;
        this.emailService = emailService;
    }

    @GetMapping("legacyClient")
    public String index(Model model, @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
        page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);

        Pageable pageable = new PageRequest(currentPage - 1, pageSize);
        Page<LegacyClient> legacyClientPage = legacyClientService.getAllLegacyClient(pageable);

        model.addAttribute("legacyClientPage", legacyClientPage);

        int totalPages = legacyClientPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "show_legacy_client";
    }

    @GetMapping("/listClientEmailForm/{id}")
    public String listClientEmailForm(@PathVariable( value = "id") Integer id, Model model) {

        LegacyClient dto = legacyClientService.getLegacyClientById(id);

        EmailDto emailDto = new EmailDto();
        //   emailDto.setCallListDto(d);
        emailDto.setSubject("CRM Client Information");
        emailDto.setText("Hi, \nPlease find the below client details." +
                "\n\nFull Name: " + dto.getFullName() +
                "\nReference Number: " + dto.getRefNumber() +
                "\nDate: " + dto.getRecordDate() +
                "\nCall Sheet: " + dto.getCallSheet());

        model.addAttribute("email_client", emailDto);
        return "email_legacy_client";
    }

    @PostMapping("/sendEmailClient")
    public String sendEmailClient(@ModelAttribute("email_client") @Valid EmailDto emailDto, BindingResult result) {

        if (result.hasErrors()) {
            return "email_legacy_client";
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

        return "redirect:/legacyClient";
    }
    @GetMapping("/showReference/{id}")
    public String showReference(@PathVariable(value = "id") Integer id, Model model) {

        LegacyClient showRefDto = legacyClientService.getLegacyClientById(id);
        List<LegacyClient> linkRef = legacyClientService.getAllByRefNumber(showRefDto.getRefNumber(), id);

        model.addAttribute("show_ref", showRefDto);
        model.addAttribute("linkedReference", linkRef);
        return "show_linked_legacy_client";
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handlerException() {
        return "error/404";
    }
}
