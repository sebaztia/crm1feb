package com.crm.repository;

import com.crm.model.CallList;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallListRepository extends JpaRepository<CallList, Integer> {

    List<CallList> findAllByArchiveFalseOrArchiveNullAndIsLeadsFalseOrIsLeadsNull();
    List<CallList> findAllByArchiveTrue();
    CallList findByClientId(Long clientId);
    List<CallList> findAllByIsLeadsTrueAndLeadsArchiveNull(/*Sort sort*/);
    Integer countByIsLeadsTrueAndLeadsArchiveNull();

    List<CallList> findByArchiveNullAndIsLeadsNull();

    Integer countByLeadsArchiveTrue();
    List<CallList> findByLeadsArchiveTrue(/*Sort sort*/);
}