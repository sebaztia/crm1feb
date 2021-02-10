package com.crm.repository;

import com.crm.model.Register;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterRepository extends JpaRepository<Register, Long> {
    Register findByRegisterToken(String registerToken);
}
