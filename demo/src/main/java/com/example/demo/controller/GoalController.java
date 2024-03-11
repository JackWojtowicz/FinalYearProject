package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Goal;
import com.example.demo.model.SecuredUser;
import com.example.demo.model.User;
import com.example.demo.repositary.GoalRepo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GoalController {
    @Autowired
    GoalRepo goalRepo;

    @GetMapping("/goals")
    public String goalsPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        User user = securedUser.getUser();
        model.addAttribute("goallist", goalRepo.findAllByUser_Id((int) user.getId()));
        model.addAttribute("goalsize", (goalRepo.findAllByUser_Id(user.getId()).isEmpty()));
        model.addAttribute("newgoal", new Goal());
        return "goals";
    }

    @RequestMapping("/addgoal")
    public String addGoal(Model model, @ModelAttribute("newgoal") Goal goal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        User user = securedUser.getUser();
        goal.setUser(user);
        goalRepo.save(goal);
        model.addAttribute("goallist", goalRepo.findByUser(user));
        model.addAttribute("goalsize", (goalRepo.findAllByUser_Id(user.getId()).isEmpty()));
        model.addAttribute("newgoal", new Goal());
        return "redirect:/goals";
    }

}
