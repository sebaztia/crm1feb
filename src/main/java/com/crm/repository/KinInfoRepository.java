package com.crm.repository;

import com.crm.model.KinInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KinInfoRepository extends JpaRepository<KinInfo, Long> {
    KinInfo findByClientId(Long clientId);
}
