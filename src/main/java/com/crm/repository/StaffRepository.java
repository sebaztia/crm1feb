package com.crm.repository;

import com.crm.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Integer> {

    Staff findByStaffName(String staffName);

    @Query(value = "SELECT s.staff_name FROM staff s", nativeQuery = true)
    List<String> getStaffQuery();
}
