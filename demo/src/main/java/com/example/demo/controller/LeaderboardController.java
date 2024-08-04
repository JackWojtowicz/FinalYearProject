package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.demo.model.SecuredUser;
import com.example.demo.model.User;
import com.example.demo.repositary.UserRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LeaderboardController extends BaseController {
    @Autowired
    UserRepo userrepo;

    @GetMapping("/leaderboard")
    public String getLeaderBoard(Model model) {
        model.addAttribute("top10", userrepo.findTop10ByOrderByScoreDesc());
        List<User> users = userrepo.findAllByOrderByScoreDesc();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        User user = securedUser.getUser();
        int found = users.indexOf(user);
        model.addAttribute("index", found + 1);
        model.addAttribute("founduser", user);
        return "leaderboard";
    }

}
