package com.crm.controller;

import com.crm.config.Utility;
import com.crm.model.Register;
import com.crm.model.User;
import com.crm.dto.UserRegistrationDto;
import com.crm.service.RegisterService;
import com.crm.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
public class UserController {

    private UserService userService;
    private RegisterService registerService;
    private JavaMailSender mailSender;
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    public UserController(UserService userService, RegisterService registerService, JavaMailSender mailSender) {
        this.userService = userService; this.registerService = registerService; this.mailSender = mailSender;
    }

    @RequestMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping(value = "/login")
    public String showRegistrationForm(Model model) {
        return "login";
    }

    @GetMapping(value = "/")
    public String home(Model model) {
        if (isAuthenticated()) {
            return "redirect:home";
        }
        return "login";
    }
    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registerUserAccount(HttpServletRequest request, @ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result, Model model) {
        User existing = userService.findByEmail(userDto.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }

        if (!userDto.getEmail().endsWith("steelerose.co.uk")) {
            result.rejectValue("email", null, "Invalid email address, you must have Steele Rose email account.");
        }
        if (result.hasErrors()) {
            return "login";
        }

        String token = RandomString.make(45);
        logger.info("====== token: " + token);
        Register register = new Register(userDto.getUsername(), userDto.getEmail(), userDto.getPassword(), token);
        try {
            registerService.save(register);
            String registerUserLink = Utility.getSiteURL(request) + "/confirm_account?token=" + token;
            sendEmail(userDto.getEmail(), registerUserLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
            logger.info("====== registerUserLink: " + registerUserLink);

        } catch (UsernameNotFoundException | MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", e.getMessage());
        }

      //  userService.save(userDto);

        return "redirect:/login?success";
    }

    @GetMapping("/confirm_account")
    public String confirmAccount(@Param(value = "token") String token, Model model) {
        Register register = registerService.findByRegisterToken(token);

        model.addAttribute("token", token);

        if (register == null) {
            model.addAttribute("message", "Invalid Token");
            return "login";
        }

        userService.save(register);
        register.setRegisterToken(null);
        registerService.save(register);

        return "redirect:/login?registered";
    }

    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("donotreply@steelerose.co.uk", "Steele Rose Support");
        helper.setTo(recipientEmail);
        String subject = "Complete Registration!";

        String content = "<p>Hello,</p>"
                + "<p>To confirm your account, please click <a href=\"" + link + "\">Activate</a> here</p>"
                + "<br>"
                + "<p>Ignore this email if you have not made the request.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }
}
