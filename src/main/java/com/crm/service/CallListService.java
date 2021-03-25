package com.crm.service;

import com.crm.model.CallList;
import com.crm.model.Staff;
import com.crm.repository.CallListRepository;
import com.crm.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CallListService {

    private CallListRepository callListRepository;
    private StaffRepository staffRepository;

    @Autowired
    public CallListService(CallListRepository callListRepository, StaffRepository staffRepository) {
        this.callListRepository = callListRepository;
        this.staffRepository = staffRepository;
    }

    public List<CallList> getAllCallLists() {
        return callListRepository.findAllByArchiveFalseOrArchiveNullAndIsLeadsFalseOrIsLeadsNull();
    }

    public List<CallList> getAllLeadsClient() {
        return callListRepository.findAllByIsLeadsTrue(new Sort(Sort.Direction.DESC, "updatedAt"));
    }

    public CallList saveCallList(CallList callList) {
        Staff staff = null;
        if (callList.getStaff() == null || callList.getStaff().getStaffName() == null)
            staff = staffRepository.findByStaffName("UNKNOWN");
        else
            staff = staffRepository.findByStaffName(callList.getStaff().getStaffName());
        callList.setStaff(staff);
       return callListRepository.save(callList);
    }

    public CallList getCallListById(Integer id) {
        return callListRepository.findOne(id);
    }

    public CallList getCallListByClientId(Long clientId) { return callListRepository.findByClientId(clientId); }

    public List<CallList> getAllArchiveList() {
        return callListRepository.findAllByArchiveTrue();
    }

    public void archiveCallListById(Integer id) {
        CallList callList = callListRepository.findOne(id);
        callList.setArchive(true);
        this.callListRepository.save(callList);
    }

    public void rollbackCallListById(Integer id) {
        CallList callList = callListRepository.findOne(id);
        callList.setArchive(false);
        this.callListRepository.save(callList);
    }

    public void emailActionDone(Integer id) {
        CallList callList = callListRepository.findOne(id);
        callList.setEmailDone(true);
        this.callListRepository.save(callList);
    }

    public void callActionDone(Integer id) {
        CallList callList = callListRepository.findOne(id);
        callList.setCallDone(true);

        this.callListRepository.save(callList);
    }

    public void deleteCallListById(Integer id) { this.callListRepository.delete(id); }
    public Integer countByIsLeads() { return this.callListRepository.countByIsLeadsTrue(); }
}
