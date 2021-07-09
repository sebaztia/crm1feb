package com.crm.service;

import com.crm.dto.UserDto;
import com.crm.model.Register;
import com.crm.model.Role;
import com.crm.model.User;
import com.crm.dto.UserRegistrationDto;
import com.crm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User save(Register registration){
        User user = new User();
        user.setUsername(registration.getUsername());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        if (registration.getEmail().equals("tinoj@sebastian.com"))
            user.setRoles(Arrays.asList(new Role("ROLE_USER"), new Role("ADMIN")));
        user.setAdmin(false);
        return userRepository.save(user);
    }

    @Override
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateRoles(long id, String type) {
        User user = userRepository.findOne(id);

        if (type.equals("makeAdmin")) {
            Collection<Role> roles = user.getRoles();
            roles.add(new Role("ADMIN"));
            user.setAdmin(true);
            user=  userRepository.save(user);
        } else if (type.equals("removeAdmin")) {
            Collection<Role> roles = user.getRoles();
            Role role1 = null;
            for (Role role: roles) {
                if (role.getName().equals("ADMIN")) {
                    role1 = role;
                }
            }
            roles.remove(role1);
            user.setAdmin(false);
            user= userRepository.save(user);
        } else  if (type.equals("makeWillRole")) {
            Collection<Role> roles = user.getRoles();
            roles.add(new Role("ROLE_WILLS"));
            user.setWillRole(true);
            user=  userRepository.save(user);
        }
        else if (type.equals("removeWillRole")) {
            Collection<Role> roles = user.getRoles();
            Role role1 = null;
            for (Role role: roles) {
                if (role.getName().equals("ROLE_WILLS")) {
                    role1 = role;
                }
            }
            roles.remove(role1);
            user.setWillRole(false);
            user= userRepository.save(user);
        }
        return user;
    }

    @Override
    public void updateResetPassword(String token, String email) throws UsernameNotFoundException{
        User user = userRepository.findByEmail(email);

        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("Invalid email." + email);
        }
    }

    @Override
    public User getUserByPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);

        userRepository.save(user);
    }

    @Override
    public List<UserDto> getUserDto() {
        List<User> userList = userRepository.findAll();

        if (userList == null)
            return null;
        return userList.stream().map(this:: toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserDto(Long userId) {
        return toDto(userRepository.findOne(userId));
    }

    @Override
    public Map updateRoles(Long userId, Boolean admin, Boolean roleUser, Boolean wills, Boolean leads, Boolean todo) {

        User user = userRepository.findOne(userId);
        HashMap<String, String> result = new HashMap<>();
        if (null != user) {
            Collection<Role> roles = user.getRoles();
            if (admin) {
                roles.add(new Role("ADMIN"));
                user.setAdmin(true);
            }
            else {
                roles =  roles.stream().filter( r -> !r.getName().equals("ADMIN")).collect(Collectors.toList());
                user.setAdmin(false);
            }
            if (roleUser)
                roles.add(new Role("ROLE_USER"));
            else
                roles =  roles.stream().filter( r -> !r.getName().equals("ROLE_USER")).collect(Collectors.toList());
            if (wills) {
                roles.add(new Role("ROLE_WILLS"));
                user.setWillRole(true);
            }
            else {
                roles =  roles.stream().filter( r -> !r.getName().equals("ROLE_WILLS")).collect(Collectors.toList());
                user.setWillRole(false);
            }
            if (leads)
                roles.add(new Role("LEADS"));
            else
                roles =  roles.stream().filter( r -> !r.getName().equals("LEADS")).collect(Collectors.toList());
            if (todo)
                roles.add(new Role("TODO"));
            else
                roles = roles.stream().filter(r -> !r.getName().equals("TODO")).collect(Collectors.toList());

            user.setRoles(roles);
            userRepository.save(user);
        }
        result.put("status", "successful");
        return result;
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();

        Collection<Role> roles = user.getRoles();
        for (Role role: roles) {
            if (role.getName().equals("ADMIN"))
                dto.setAdmin(true);
            else if (role.getName().equals("ROLE_USER"))
                dto.setRoleUser(true);
            else if (role.getName().equals("ROLE_WILLS"))
                dto.setWills(true);
            else if (role.getName().equals("LEADS"))
                dto.setLeads(true);
            else if (role.getName().equals("TODO"))
                dto.setTodo(true);
        }

        return dto;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
