package com.crm.service;

import com.crm.model.Staff;
import com.crm.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    private StaffRepository staffRepository;

    @Autowired
    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public List<Staff> getAllStaff() { return staffRepository.findAll(); }

    public void deleteStaff(Integer id) { staffRepository.delete(id); }

    public Staff saveStaff(Staff staff) { return staffRepository.save(staff); }

    public Staff findByStaffName(String staffName) { return staffRepository.findByStaffName(staffName); }
}
