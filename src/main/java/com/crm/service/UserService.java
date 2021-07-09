package com.crm.service;

import com.crm.dto.UserDto;
import com.crm.model.Register;
import com.crm.model.User;
import com.crm.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserService extends UserDetailsService {

    User findByEmail(String email);
    User save(Register userRegistrationDto);
    List<User> findUsers();
    User updateRoles(long id, String makeAdmin);
    void updateResetPassword(String token, String email) throws UsernameNotFoundException;
    User getUserByPasswordToken(String token);
    void updatePassword(User user, String newPassword);
    List<UserDto> getUserDto();
    UserDto getUserDto(Long userId);
    Map updateRoles(Long userId, Boolean admin, Boolean roleUser, Boolean wills, Boolean leads, Boolean todo);
}
