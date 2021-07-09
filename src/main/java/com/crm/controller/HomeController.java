package com.crm.controller;

import com.crm.dto.UserDto;
import com.crm.model.Staff;
import com.crm.model.User;
import com.crm.service.CallListService;
import com.crm.service.ClientService;
import com.crm.service.StaffService;
import com.crm.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private UserService userService;
    private StaffService staffService;
    private CallListService callListService;
    private ClientService clientService;
    private static Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    public HomeController(UserService userService, StaffService staffService, CallListService callListService, ClientService clientService) {
        this.userService = userService;
        this.staffService = staffService;
        this.callListService = callListService;
        this.clientService = clientService;
    }

    @GetMapping("home")
    public String chart(Model model) {

        //first, add the regional sales
      /*  Integer northeastSales = 17089;
        Integer westSales = 10603;
        Integer midwestSales = 5223;
        Integer southSales = 10111;

        model.addAttribute("northeastSales", northeastSales);
        model.addAttribute("southSales", southSales);
        model.addAttribute("midwestSales", midwestSales);
        model.addAttribute("westSales", westSales);*/
        Integer yesClient = clientService.countByClearedTrue();
        Integer noClient = clientService.countByClearedFalseOrClearedNull();
        Integer deceased = clientService.countByDeceasedTrue();
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
       // model.addAttribute("userDto", userService.getUserDto());

        return "settings_page";
    }
    @GetMapping("/getUserDto")
    public @ResponseBody UserDto getUserDto(@RequestParam Long userId)
    {
       // String json = null;
        return userService.getUserDto(userId);
       /* // List<Object[]> list = countryService.getStatesByCountry(countryId);
        try {
            json = new ObjectMapper().writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logger.info("=== json: " + json);*/
    }
    @GetMapping("/updateUserDto")
    public @ResponseBody Object updateUserDto(@RequestParam Long userId, @RequestParam Boolean admin, @RequestParam Boolean roleUser,
                      @RequestParam Boolean wills, @RequestParam Boolean leads, @RequestParam Boolean todo)
    {
        return userService.updateRoles(userId, admin, roleUser, wills, leads, todo);
    }

    @GetMapping("/makeWillRole/{id}")
    public String makeWillRole(@PathVariable(value = "id") long id) {

        userService.updateRoles(id, "makeWillRole");
        return "redirect:/settingForm";
    }
    @GetMapping("/removeWillRole/{id}")
    public String removeWillRole(@PathVariable(value = "id") long id) {

        userService.updateRoles(id, "removeWillRole");
        return "redirect:/settingForm";
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
        return userService.findUsers();
    }
    @GetMapping("/getAllStaffNames")
    @ResponseBody
    public Object getAllStaffNames(@RequestParam Long userId)
    {
        String json = null;
        logger.info("getAllStaffNames=====" + userId);
       Staff staffList = staffService.findByStaffName("UNKNOWN");
        try {
            json = new ObjectMapper().writeValueAsString(staffList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logger.info("=== json: " + json);
        return json;
    }

    @GetMapping(value = "/api/customer/all")
    public Object getResource() {
        Map<String, String> result = new HashMap<>();
        logger.info("getResource=====" );
        result.put("status", "done");
        return result;
    }
    @GetMapping("/getStaffLists")
    public @ResponseBody Object getStaffLists(@RequestParam Long userId)
    {
        return staffService.getQuery();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handlerException() {
        return "error/404";
    }
}
