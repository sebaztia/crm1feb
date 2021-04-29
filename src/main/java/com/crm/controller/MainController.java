package com.crm.controller;

import com.crm.model.Notification;
import com.crm.service.CallListService;
import com.crm.service.ClientService;
import com.crm.service.CompanyService;
import com.crm.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    private ClientService clientService;
    private CompanyService companyService;
    private CallListService callListService;
    private NotificationService notificationService;

    @Autowired
    public MainController(ClientService clientService, CompanyService companyService, CallListService callListService
                            , NotificationService notificationService) {
        this.clientService = clientService;
        this.companyService = companyService;
        this.callListService = callListService;
        this.notificationService = notificationService;
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

    @GetMapping("/getNotifications")
    public @ResponseBody Object getNotifications() {
        return notificationService.getNotificationByMentioned(getUsername());
    }

    @GetMapping("showClientAndHaveSeen/{id}")
    public String showClientAndHaveSeen(@PathVariable(value = "id") long id, RedirectAttributes attributes) {
        Notification notification = notificationService.findOne(id);
        notification.setSeen(true);
        notificationService.save(notification);
        attributes.addAttribute("id", notification.getClientId());
        return "redirect:/showClient/{id}";
    }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handlerException() {
        return "error/404";
    }
}
