package com.crm.controller;

import com.crm.model.CallList;
import com.crm.model.Role;
import com.crm.model.Staff;
import com.crm.model.User;
import com.crm.service.CallListService;
import com.crm.service.StaffService;
import com.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Controller
public class HomeController {

    private UserService userService;
    private StaffService staffService;
    private CallListService callListService;

    @Autowired
    public HomeController(UserService userService, StaffService staffService, CallListService callListService) {
        this.userService = userService;
        this.staffService = staffService;
        this.callListService = callListService;
    }

    @GetMapping("home")
    public String chart(Model model) {

        //first, add the regional sales
        Integer northeastSales = 17089;
        Integer westSales = 10603;
        Integer midwestSales = 5223;
        Integer southSales = 10111;

        model.addAttribute("northeastSales", northeastSales);
        model.addAttribute("southSales", southSales);
        model.addAttribute("midwestSales", midwestSales);
        model.addAttribute("westSales", westSales);

        //now add sales by lure type
        List<Integer> inshoreSales = Arrays.asList(4074, 3455, 4112);
        List<Integer> nearshoreSales = Arrays.asList(3222, 3011, 3788);
        List<Integer> offshoreSales = Arrays.asList(7811, 7098, 6455);

        model.addAttribute("inshoreSales", inshoreSales);
        model.addAttribute("nearshoreSales", nearshoreSales);
        model.addAttribute("offshoreSales", offshoreSales);

        return "home";
    }

    @GetMapping("/settingForm")
    public String settingForm(Model model) {

        model.addAttribute("users", getUserList());
        model.addAttribute("staffList", staffService.getAllStaff());
        model.addAttribute("staff", new Staff());

        return "settings_page";
    }

    @GetMapping("/makeAdmin/{id}")
    public String makeAdmin(@PathVariable(value = "id") long id) {

        userService.updateRoles(id, "makeAdmin");
        return "redirect:/settingForm";
    }
    @GetMapping("/removeAdmin/{id}")
    public String removeAdmin(@PathVariable(value = "id") long id) {

        userService.updateRoles(id, "removeAdmin");
        return "redirect:/settingForm";
    }
    @GetMapping("/deleteStaff/{id}")
    public String deleteStaff(@PathVariable(value = "id") Integer id) {

        staffService.deleteStaff(id);
        return "redirect:/settingForm";
    }
    @PostMapping("staffSave")
    public String staffSave(@ModelAttribute("staff") @Valid Staff staff, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("users", getUserList());
            model.addAttribute("staffList", staffService.getAllStaff());
            return "settings_page";
        }
        staffService.saveStaff(staff);

        return "redirect:/settingForm";
    }
    private List<User> getUserList() {
        List<User> userList = userService.findUsers();
        for (User user: userList) {
            Collection<Role> roles = user.getRoles();
            for (Role role: roles) {
                if (role.getName().equals("ADMIN")) {
                    user.setAdmin(true);
                    continue;
                } else user.setAdmin(false);
            }
        }
        return userList;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handlerException() {
        return "error/404";
    }
}
