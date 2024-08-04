package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.model.SecuredUser;
import com.example.demo.model.User;

@Controller
public class BaseController {
    @ModelAttribute("userRole")
    public String userRole(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            SecuredUser secureduser = (SecuredUser) authentication.getPrincipal();
            User user = secureduser.getUser();
            return user.getRole();
        }
        return null;
    }
}
