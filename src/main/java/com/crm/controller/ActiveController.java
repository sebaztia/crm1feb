package com.crm.controller;

import com.crm.model.Client;
import com.crm.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class ActiveController {

    private ClientService clientService;

    @Autowired
    public ActiveController(ClientService clientService) {
        this.clientService = clientService;
    }

    private static Logger logger = LoggerFactory.getLogger(ActiveController.class);

    @RequestMapping(value = "/tasks/update/status/{clientId}", method = RequestMethod.POST)
    public Object updateTaskStatus(@PathVariable("clientId") long clientId/*, @RequestParam("projectId") long projectId*/, @RequestParam("colName") String colName){

      //   logger.info("colName:::" + colName+", clientId:::" + clientId);
        HashMap<String, String> result = new HashMap<>();
        Client client = clientService.getClientById(clientId);
        if (null != client) {
            client.setStatus(getStatus(colName));
            clientService.saveClient(client);
            result.put("status", "successful (columnName:"+colName+ ", cId:"+clientId+")");
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

}
