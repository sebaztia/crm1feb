package com.crm.service;

import com.crm.model.Register;
import com.crm.repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    private RegisterRepository registerRepository;

    @Autowired
    public RegisterService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    public Register findByRegisterToken(String token) { return registerRepository.findByRegisterToken(token); }
    public Register save(Register register) { return registerRepository.save(register); }

}
