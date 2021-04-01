package com.crm.repository;

import com.crm.model.Addressbook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressbookRepository extends JpaRepository<Addressbook, Long> {

}
