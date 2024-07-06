package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
        model.addAttribute("goallist", goalRepo.findByUserAndCompletion(user, false));
        model.addAttribute("goalsize", (goalRepo.findByUser_Id(user.getId()).isEmpty()));
        model.addAttribute("newgoal", new Goal());
        model.addAttribute("updategoal", new Goal());
        model.addAttribute("importance0", goalRepo.findAllByUserAndImportance(user, 0));
        model.addAttribute("importance1", goalRepo.findAllByUserAndImportance(user, 1));
        model.addAttribute("importance2", goalRepo.findAllByUserAndImportance(user, 2));

        return "goals";
    }

    @GetMapping("/completedgoals")
    public String completedgoals(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        User user = securedUser.getUser();
        List<Goal> compgoals = goalRepo.findByUserAndCompletion(user, true);
        model.addAttribute("goalsize", (compgoals.isEmpty()));
        if (!compgoals.isEmpty()) {
            model.addAttribute("comgoals", compgoals);
        }
        return "completedgoals";
    }

    @RequestMapping("/addgoal")
    public String addGoal(Model model, @ModelAttribute("newgoal") Goal goal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        User user = securedUser.getUser();
        goal.setUser(user);
        goalRepo.save(goal);
        model.addAttribute("goallist", goalRepo.findByUser(user));
        model.addAttribute("goalsize", (goalRepo.findByUser_Id(user.getId()).isEmpty()));
        model.addAttribute("newgoal", new Goal());
        return "redirect:/goals";
    }

    @PostMapping("/goaldone/{id}")
    public String goaldone(Model model, @ModelAttribute("updategoal") Goal goal, @PathVariable long id) {
        Optional<Goal> oldgoal = goalRepo.findById(id);
        // System.out.println(goal);
        // System.out.println(goal.getId());
        // System.out.println(goal.getName());
        if (oldgoal.isPresent()) {
            Goal updategoal = oldgoal.get();
            updategoal.setCompletion(true);
            goalRepo.save(updategoal);
        }
        return "redirect:/goals";
    }

    @RequestMapping("/moveup/{goalid}")
    public String increaseImportance(Model model, @PathVariable String goalid) {
        Goal temp = goalRepo.findById(Long.parseLong(goalid)).get();
        temp.setImportance(temp.getImportance() + 1);
        goalRepo.save(temp);
        return "redirect:/goals";
    }

    @RequestMapping("/movedown/{goalid}")
    public String decreaseImportance(Model model, @PathVariable String goalid) {
        Goal temp = goalRepo.findById(Long.parseLong(goalid)).get();
        temp.setImportance(temp.getImportance() - 1);
        goalRepo.save(temp);
        return "redirect:/goals";
    }

}
