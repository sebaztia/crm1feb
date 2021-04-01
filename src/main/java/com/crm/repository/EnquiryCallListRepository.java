package com.crm.repository;

import com.crm.model.CallList;
import com.crm.model.EnquiryCallList;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnquiryCallListRepository extends JpaRepository<EnquiryCallList, Long> {
    List<EnquiryCallList> findByArchiveNullAndIsLeadsNull();
}
