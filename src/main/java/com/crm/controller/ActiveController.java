package com.crm.controller;

import com.crm.model.Client;
import com.crm.model.RecentActivity;
import com.crm.model.SrcPng;
import com.crm.repository.SrcRepository;
import com.crm.service.ClientService;
import com.crm.service.RecentActivityService;
import com.crm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class ActiveController {

    private ClientService clientService;
    private RecentActivityService recentActivityService;
    private SrcRepository srcRepository;
    private UserService userService;

    @Autowired
    public ActiveController(ClientService clientService, RecentActivityService recentActivityService, SrcRepository srcRepository,
                            UserService userService) {
        this.clientService = clientService;
        this.recentActivityService = recentActivityService;
        this.srcRepository = srcRepository;
        this.userService = userService;
    }

    private static Logger logger = LoggerFactory.getLogger(ActiveController.class);

    @RequestMapping(value = "/tasks/update/status/{clientId}", method = RequestMethod.POST)
    public Object updateTaskStatus(@PathVariable("clientId") long clientId/*, @RequestParam("projectId") long projectId*/, @RequestParam("colName") String colName){

      //   logger.info("colName:::" + colName+", clientId:::" + clientId);
        HashMap<String, String> result = new HashMap<>();
        Client client = clientService.getClientById(clientId);
        if (null != client) {
            String oldStatus = client.getStatus();
            client.setStatus(getStatus(colName));
            clientService.saveClient(client);
            result.put("status", "successful (columnName:"+colName+ ", cId:"+clientId+")");
            String author = getUsername();
            SrcPng srcPng = srcRepository.findByAuthor(author);
            if (null == srcPng) {
                srcPng = srcRepository.findByAuthor("Sebastian");
            }
            recentActivityService.save(new RecentActivity(client.getName(), "moved", (srcPng.getAuthor().equals("Sebastian")? author : srcPng.getAuthor()),
                    srcPng.getSrc(), oldStatus, getStatus(colName)));
        } else {
            result.put("status", "Unable to find the (clientId:"+clientId+")");
        }
        return result;
    }

    private String getStatus(String colName) {
        if (colName.equals("first"))
            return "First Contact";
        else if (colName.equals("instructions"))
            return "Instructions";
        else if (colName.equals("drafting"))
            return "Drafting";
        else if (colName.equals("signing"))
            return "Signing";
        else if (colName.equals("checking"))
            return "Checking";
        else if (colName.equals("stored"))
            return "Stored";
        else
            return "Archived";
    }
    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByEmail(authentication.getName()).getUsername();
    }
}
