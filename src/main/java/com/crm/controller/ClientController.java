package com.crm.controller;

import com.crm.config.Utility;
import com.crm.model.*;
import com.crm.repository.SrcRepository;
import com.crm.service.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ClientController {

    private CompanyService companyService;
    private ClientService clientService;
    private CommentsService commentsService;
    private ClientStatusService clientStatusService;
    private CallListService callListService;
    private StaffService staffService;
    private FileUploadService fileUploadService;
    private PersonalAssetService personalAssetService;
    private RecentActivityService recentActivityService;
    private SrcRepository srcRepository;
    private JavaMailSender mailSender;
    private NotificationService notificationService;

    @Value("${daniel.email}")
    private String emailTo;

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    public ClientController(CompanyService companyService, ClientService clientService, CommentsService commentsService, JavaMailSender mailSender,
                            ClientStatusService clientStatusService, CallListService callListService, StaffService staffService,
                            FileUploadService fileUploadService, PersonalAssetService personalAssetService, RecentActivityService recentActivityService, SrcRepository srcRepository,
                            NotificationService notificationService) {
        this.companyService = companyService;
        this.clientService = clientService;
        this.commentsService = commentsService;
        this.mailSender = mailSender;
        this.clientStatusService = clientStatusService;
        this.callListService = callListService;
        this.staffService = staffService;
        this.fileUploadService = fileUploadService;
        this.personalAssetService = personalAssetService;
        this.recentActivityService = recentActivityService;
        this.srcRepository = srcRepository;
        this.notificationService = notificationService;
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
        Long clientId = client.getId();
        if (client.getCallListId() != null && client.getIsLeads() != null && client.getIsLeads()) {
            CallList callList = callListService.getCallListById(client.getCallListId());
            callList.setContactName(client.getName());
            callList.setContactNumber(client.getPhone());
            callListService.saveCallList(callList);
        }
        client = clientService.saveClient(client);
        String author = getUsername();
        SrcPng srcPng = srcRepository.findByAuthor(author);
        if (null == srcPng) {
            srcPng = srcRepository.findByAuthor("Sebastian");
        }
        recentActivityService.save(new RecentActivity(client.getName(), clientId == null ? "addedClient" : "editedClient", (srcPng.getAuthor().equals("Sebastian")? author : srcPng.getAuthor()),
                srcPng.getSrc(), client.getId()));
        attributes.addAttribute("id", client.getId());
        return "redirect:/showClient/{id}";
    }

    @RequestMapping("/listClient")
    public String listClient(Model model) {

        model.addAttribute("clientList", clientService.findClients());
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
        String author = getUsername();
        Long id = Long.valueOf(request.getParameter("id"));
      //  author ="Dora";
        Client client = clientService.getClientById(id);
        Comments comments = new Comments(message, author, client);
        comments = commentsService.save(comments);

        SrcPng srcPng = srcRepository.findByAuthor(author);
        if (null == srcPng) {
            srcPng = srcRepository.findByAuthor("Sebastian");
        }

        saveNotification(message, author, client);

        recentActivityService.save(new RecentActivity(client.getName(), message, comments.getId(), "addComment", (srcPng.getAuthor().equals("Sebastian")? author : srcPng.getAuthor()),
                srcPng.getSrc(), client.getId()));
         attributes.addAttribute("id", id);
        return "redirect:/showClient/{id}";
    }

    private void saveNotification(String message, String author, Client client) {
        String[] myArray = {"@AmyWinder", "@Dean", "@Daniel", "@Dora", "@Angela", "@Hollie", "@SimonCooper", "@Stacie", "@Claire", "@Sebastian"};
        for (String username : message.trim().split("\\s+")) {
            if (stringContainsItemFromList(username, myArray)) {
                // userList.add(myUsers);
                notificationService.save(new Notification(author, username, client.getId(), client.getName(), false, userMap.get(username)));
            }
        }
    }

    public static boolean stringContainsItemFromList(String inputStr, String[] items) {
        return Arrays.stream(items).anyMatch(inputStr::contains);
    }
    private static final Map<String, String> userMap = new HashMap<>();
    static {
        userMap.put("@AmyWinder", "Amy");
        userMap.put("@Dean", "deansteele");
        userMap.put("@Daniel", "Daniel");
        userMap.put("@Dora", "Dora");
        userMap.put("@Angela", "Angela");
        userMap.put("@Hollie", "Hollie");
        userMap.put("@SimonCooper", "Simon");
        userMap.put("@Stacie", "Stacie Griffin");
        userMap.put("@Claire", "Claire Northcote");
        userMap.put("@Sebastian", "Sebastian");
    }

    @PostMapping("/editComment")
    public String editComment(HttpServletRequest request, RedirectAttributes attributes) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy hh:mm");
        String message = request.getParameter("comMessage");
        Long id = Long.valueOf(request.getParameter("cId"));
        Long comId = Long.valueOf(request.getParameter("comId"));

     //   logger.info("clientId:" + id + ", commentId:" + comId + ", message:" + message);
        Comments comments = commentsService.findOne(comId);
        comments.setMessage(message);
        String author = getUsername();
        comments.setEditedAuthor((comments.getEditedAuthor()==null? "":comments.getEditedAuthor())
                + author + " on " + sdf.format(new Date()) + ", ");
        commentsService.save(comments);
        Client client = clientService.getClientById(id);
        saveNotification(message, author, client);
        RecentActivity recentActivity = recentActivityService.findByCommentId(comId);

        if (null == recentActivity) {
            SrcPng srcPng = srcRepository.findByAuthor(author);
            if (null == srcPng) {
                srcPng = srcRepository.findByAuthor("Sebastian");
            }
            recentActivityService.save(new RecentActivity(comments.getClient().getName(), message, comments.getId(), "editedComment", (srcPng.getAuthor().equals("Sebastian")? author : srcPng.getAuthor()),
                    srcPng.getSrc(), client.getId()));
        } else {
            recentActivity.setAuthor(author);
            recentActivity.setType("editedComment");
            recentActivity.setOnMessage(message);
            recentActivityService.save(recentActivity);
        }
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
    public String showClientSave(HttpServletRequest request, @ModelAttribute("show_client") Client client, BindingResult result, Model model, RedirectAttributes attributes) {
        SrcPng srcPng = srcRepository.findByAuthor(getUsername());
        if (null == srcPng) {
            srcPng = srcRepository.findByAuthor("Sebastian");
        }
        recentActivityService.save(new RecentActivity(client.getName(),"editedClient",
                (srcPng.getAuthor().equals("Sebastian")? getUsername() : srcPng.getAuthor()), srcPng.getSrc(), client.getId()));

        Boolean deceased = client.getDeceased();
        if (null != deceased && deceased) {
            //    logger.info("deceased=== TRue" );
            if (null == callListService.getCallListByClientId(client.getId())) {
                CallList callList = new CallList();
                callList.setContactName(client.getName());
        //        callList.setRecordDate(new Date());
                callList.setContactNumber(client.getPhone());
                callList.setQuery("This client is DECEASED, the status is " + client.getStatus() + ".");
                callList.setClientId(client.getId());
           //     callList.setStaff(staffService.findByStaffName("Stacie"));
                callList.setStaffName("Stacie");
                callList = callListService.saveCallList(callList);
                String deceasedLink = Utility.getSiteURL(request) + "/callList";
                try {
                    sendEmail(emailTo, deceasedLink, client.getName());
                } catch (MessagingException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                client.setCallListId(callList.getId());
                clientService.saveClient(client);
            }
        } else {
            clientService.saveClient(client);
        }
        attributes.addAttribute("id", client.getId());
        return "redirect:/showClient/{id}";
    }

    private void sendEmail(String recipientEmail, String deceasedLink, String clientName)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("donotreply@steelerose.co.uk", "Steele Rose Support");
        helper.setTo(recipientEmail);
        String content = "<p>Hello Daniel,</p>"
                + "<p>Please check the <a href=\"" + deceasedLink + "\"> CRM </a>.</p>"
                + "<p> <b>" + clientName + "</b> is <b>deceased</b>, please make a comment on this client to let Stacie know what needs to be done.</p>";

        helper.setSubject("Client " + clientName+ " is Deceased");
        helper.setText(content, true);
        mailSender.send(message);
    }

    @PostMapping("personal_asset_save")
    public String personalAssetSave(@ModelAttribute("personalAsset") PersonalAsset personalAsset, BindingResult result, Model model, RedirectAttributes attributes) {

        personalAssetService.savePersonalAsset(personalAsset);

        attributes.addAttribute("id", personalAsset.getClientId());
        return "redirect:/showClient/{id}";
    }
    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
