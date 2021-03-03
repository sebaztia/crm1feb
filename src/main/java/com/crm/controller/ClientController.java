package com.crm.controller;

import com.crm.model.*;
import com.crm.service.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
public class ClientController {

    private CompanyService companyService;
    private ClientService clientService;
    private CommentsService commentsService;
    private UserService userService;
    private ClientStatusService clientStatusService;
    private CallListService callListService;
    private StaffService staffService;
    private FileUploadService fileUploadService;
    private PersonalAssetService personalAssetService;

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    public ClientController(CompanyService companyService, ClientService clientService, CommentsService commentsService, UserService userService,
                            ClientStatusService clientStatusService, CallListService callListService, StaffService staffService,
                            FileUploadService fileUploadService, PersonalAssetService personalAssetService) {
        this.companyService = companyService;
        this.clientService = clientService;
        this.commentsService = commentsService;
        this.userService = userService;
        this.clientStatusService = clientStatusService;
        this.callListService = callListService;
        this.staffService = staffService;
        this.fileUploadService = fileUploadService;
        this.personalAssetService = personalAssetService;
    }

    @GetMapping("clientAdd/{id}")
    public String addClient(@PathVariable("id") Long id, Model model) {

        Company company = companyService.getCompanyById(id);
        Client client = new Client();
        client.setCompany(company);
        model.addAttribute("company", company);
        model.addAttribute("client", client);
        List<ClientStatus> statusList = clientStatusService.getAllClientStatus();
        model.addAttribute("statusList", statusList);

        return "add_client";
    }

    @GetMapping(value = "editClient/{id}")
    public String editClient(@PathVariable("id") Long id, Model model) {
        Client client = clientService.getClientById(id);
        Company company = companyService.getCompanyById(client.getCompany().getId());

        model.addAttribute("company", company);
        model.addAttribute("client", client);
        List<ClientStatus> statusList = clientStatusService.getAllClientStatus();
        model.addAttribute("statusList", statusList);
        return "add_client";
    }

    @RequestMapping("clientSave")
    public String save(@ModelAttribute("client") @Valid Client client, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            Company company = client.getCompany();
            model.addAttribute("company", company);
            List<ClientStatus> statusList = clientStatusService.getAllClientStatus();
            model.addAttribute("statusList", statusList);
            return "add_client";
        }
        clientService.saveClient(client);
        attributes.addAttribute("id", client.getId());
        return "redirect:/showClient/{id}";
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
        PersonalAsset personalAsset = personalAssetService.findByClientId(id);
        if (personalAsset == null) {
            personalAsset = new PersonalAsset();
            personalAsset.setClientId(id);
        }

        model.addAttribute("show_client", client);
        model.addAttribute("linkedComments", commentsList);
        model.addAttribute("personalAsset", personalAsset);

        List<ClientStatus> statusList = clientStatusService.getAllClientStatus();
        model.addAttribute("statusList", statusList);
        List<FileUpload> allFileNameInBucket = fileUploadService.getAllByClientId(id);
        model.addAttribute("s3FileNames", allFileNameInBucket);
        return "sow_client";
    }

    @PostMapping("/submit_message")
    public String submit_message(HttpServletRequest request, RedirectAttributes attributes) {
        String message = request.getParameter("message");
        String author = userService.findByEmail(request.getUserPrincipal().getName()).getUsername();
        Long id = Long.valueOf(request.getParameter("id"));

        Client client = clientService.getClientById(id);
        Comments comments = new Comments(message, author, client);
        commentsService.save(comments);
       /* List<ClientStatus> statusList = clientStatusService.getAllClientStatus();
        model.addAttribute("statusList", statusList);
        List<Comments> commentsList = commentsService.findByClient(client);

        model.addAttribute("show_client", client);
        model.addAttribute("linkedComments", commentsList);
        List<FileUpload> allFileNameInBucket = fileUploadService.getAllByClientId(id);
        model.addAttribute("s3FileNames", allFileNameInBucket);

        PersonalAsset personalAsset = personalAssetService.findByClientId(id);
        if (personalAsset == null) {
            personalAsset = new PersonalAsset();
            personalAsset.setClientId(id);
        }
        model.addAttribute("personalAsset", personalAsset);
        return "sow_client";*/

        attributes.addAttribute("id", id);
        return "redirect:/showClient/{id}";
    }

    @PostMapping("/editComment")
    public String editComment(HttpServletRequest request, RedirectAttributes attributes) {

        String message = request.getParameter("comMessage");
        Long id = Long.valueOf(request.getParameter("cId"));
        Long comId = Long.valueOf(request.getParameter("comId"));

     //   logger.info("clientId:" + id + ", commentId:" + comId + ", message:" + message);
        Comments comments = commentsService.findOne(comId);
        comments.setMessage(message);
        commentsService.save(comments);

        attributes.addAttribute("id", id);
        return "redirect:/showClient/{id}";
    }

    @PostMapping("/deleteModalComments")
    public String deleteModalComments(HttpServletRequest request, RedirectAttributes attributes) {
        Long id = Long.valueOf(request.getParameter("clttId"));
        Long comId = Long.valueOf(request.getParameter("delId"));
      //  logger.info("clientId:" + id + ", commentId:" + comId);
        commentsService.deleteComment(comId);
        attributes.addAttribute("id", id);
        return "redirect:/showClient/{id}";
    }

    @PostMapping("show_client_save")
    public String showClientSave(@ModelAttribute("show_client") Client client, BindingResult result, Model model, RedirectAttributes attributes) {

        clientService.saveClient(client);
        Boolean deceased = client.getDeceased();
        if (null != deceased && deceased) {
            //    logger.info("deceased=== TRue" );
            if (null == callListService.getCallListByClientId(client.getId())) {
                CallList callList = new CallList();
                callList.setContactName(client.getName());
                callList.setRecordDate(new Date());
                callList.setContactNumber(client.getPhone());
                callList.setQuery("This client is DECEASED, the status is " + client.getStatus() + ".");
                callList.setClientId(client.getId());
                callList.setStaff(staffService.findByStaffName("Stacie"));
                callListService.saveCallList(callList);
            }
        }
        attributes.addAttribute("id", client.getId());
        return "redirect:/showClient/{id}";
    }

    @PostMapping("personal_asset_save")
    public String personalAssetSave(@ModelAttribute("personalAsset") PersonalAsset personalAsset, BindingResult result, Model model, RedirectAttributes attributes) {

        personalAssetService.savePersonalAsset(personalAsset);

        attributes.addAttribute("id", personalAsset.getClientId());
        return "redirect:/showClient/{id}";
    }
}
