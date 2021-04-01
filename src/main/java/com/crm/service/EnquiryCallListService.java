package com.crm.service;

import com.crm.model.Addressbook;
import com.crm.model.CallList;
import com.crm.model.EnquiryCallList;
import com.crm.repository.EnquiryCallListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnquiryCallListService {

    private EnquiryCallListRepository repository;

    @Autowired
    public EnquiryCallListService(EnquiryCallListRepository repository) {
        this.repository = repository;
    }

    public List<EnquiryCallList> getAllEnquiryCallLists() {
        return repository.findByArchiveNullAndIsLeadsNull(/*new Sort(Sort.Direction.DESC, "id")*/);
    }

    public EnquiryCallList saveCallListModal(EnquiryCallList ab) { return repository.save(ab);    }

    public EnquiryCallList findOne(Long id) { return repository.findOne(id); }

    public void archiveCallListById(Long id) {
        EnquiryCallList callList = repository.findOne(id);
        callList.setArchive(true);
        this.repository.save(callList);
    }

    public void emailActionDone(Long id) {
        EnquiryCallList callList = repository.findOne(id);
        callList.setEmailDone(true);
        this.repository.save(callList);
    }

    public void callActionDone(Long id) {
        EnquiryCallList callList = repository.findOne(id);
        callList.setCallDone(true);
        this.repository.save(callList);
    }
}
