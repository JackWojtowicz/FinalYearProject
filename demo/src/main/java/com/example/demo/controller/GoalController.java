package com.example.demo.controller;

import java.util.Collection;
import java.util.Collections;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Goal;
import com.example.demo.model.SecuredUser;
import com.example.demo.model.User;
import com.example.demo.repositary.GoalRepo;
import com.example.demo.repositary.UserRepo;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GoalController extends BaseController {
    @Autowired
    GoalRepo goalRepo;
    @Autowired
    UserRepo userrepo;

    @RequestMapping("/goals")
    public String goalsPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        User user = securedUser.getUser();
        model.addAttribute("goallist", goalRepo.findByUserAndCompletion(user, false));
        model.addAttribute("goalsize", (goalRepo.findByUser_Id(user.getId()).isEmpty()));
        model.addAttribute("newgoal", new Goal());
        model.addAttribute("updategoal", new Goal());
        model.addAttribute("alllist", goalRepo.findByUser(user));
        if (model.getAttribute("importance0") == null) {
            model.addAttribute("importance0", goalRepo.findAllByUserAndImportanceAndCompletion(user, 0, false));
        }
        if (model.getAttribute("importance1") == null) {
            model.addAttribute("importance1", goalRepo.findAllByUserAndImportanceAndCompletion(user, 1, false));
        }
        if (model.getAttribute("importance2") == null) {
            model.addAttribute("importance2", goalRepo.findAllByUserAndImportanceAndCompletion(user, 2, false));
        }
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
        goal.setDuedate(goal.getDuedate());
        goalRepo.save(goal);
        model.addAttribute("goallist", goalRepo.findByUser(user));
        model.addAttribute("goalsize", (goalRepo.findByUser_Id(user.getId()).isEmpty()));
        model.addAttribute("newgoal", new Goal());
        return "redirect:/goals";
    }

    @RequestMapping("/goaldone/{id}")
    public String goaldone(Model model, @ModelAttribute("updategoal") Goal goal, @PathVariable long id) {
        Optional<Goal> oldgoal = goalRepo.findById(id);
        // System.out.println(goal);
        // System.out.println(goal.getId());
        // System.out.println(goal.getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        User user = securedUser.getUser();
        if (oldgoal.isPresent()) {
            Goal updategoal = oldgoal.get();
            updategoal.setCompletion(true);
            goalRepo.save(updategoal);
            user.setScore(user.getScore() + 1);
            userrepo.save(user);
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

    @RequestMapping("/goalupdate")
    public String updateGoal(Model model, @ModelAttribute("updategaol") Goal goal) {
        Goal temp = goalRepo.findById(goal.getId()).get();
        temp.setDescription(goal.getDescription());
        temp.setName(goal.getName());
        goalRepo.save(temp);
        return "redirect:/goals";

    }

    @RequestMapping("/sortasc/{importance}")
    public String sortasc(Model model, @PathVariable int importance, RedirectAttributes redirectAttrs) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        User user = securedUser.getUser();
        List<Goal> asc = goalRepo.findAllByUserAndImportanceAndCompletion(user, importance, false);
        Collections.sort(asc);
        model.addAttribute("importance" + importance, asc);
        model.addAttribute("goallist", goalRepo.findByUserAndCompletion(user, false));
        model.addAttribute("goalsize", (goalRepo.findByUser_Id(user.getId()).isEmpty()));
        model.addAttribute("newgoal", new Goal());
        model.addAttribute("updategoal", new Goal());
        model.addAttribute("alllist", goalRepo.findByUser(user));
        if (importance == 0) {
            model.addAttribute("importance1", goalRepo.findAllByUserAndImportanceAndCompletion(user, 1, false));
            model.addAttribute("importance2", goalRepo.findAllByUserAndImportanceAndCompletion(user, 2, false));
        }
        if (importance == 1) {
            model.addAttribute("importance0", goalRepo.findAllByUserAndImportanceAndCompletion(user, 0, false));
            model.addAttribute("importance2", goalRepo.findAllByUserAndImportanceAndCompletion(user, 2, false));
        }
        if (importance == 2) {
            model.addAttribute("importance1", goalRepo.findAllByUserAndImportanceAndCompletion(user, 1, false));
            model.addAttribute("importance0", goalRepo.findAllByUserAndImportanceAndCompletion(user, 0, false));
        }
        return "goals";

    }

    @RequestMapping("/sortdsc/{importance}")
    public String sortdsc(Model model, @PathVariable int importance) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        User user = securedUser.getUser();
        List<Goal> asc = goalRepo.findAllByUserAndImportanceAndCompletion(user, importance, false);
        Collections.sort(asc, Collections.reverseOrder());
        model.addAttribute("importance" + importance, asc);
        model.addAttribute("goallist", goalRepo.findByUserAndCompletion(user, false));
        model.addAttribute("goalsize", (goalRepo.findByUser_Id(user.getId()).isEmpty()));
        model.addAttribute("newgoal", new Goal());
        model.addAttribute("updategoal", new Goal());
        model.addAttribute("alllist", goalRepo.findByUser(user));
        if (importance == 0) {
            model.addAttribute("importance1", goalRepo.findAllByUserAndImportanceAndCompletion(user, 1, false));
            model.addAttribute("importance2", goalRepo.findAllByUserAndImportanceAndCompletion(user, 2, false));
        }
        if (importance == 1) {
            model.addAttribute("importance0", goalRepo.findAllByUserAndImportanceAndCompletion(user, 0, false));
            model.addAttribute("importance2", goalRepo.findAllByUserAndImportanceAndCompletion(user, 2, false));
        }
        if (importance == 2) {
            model.addAttribute("importance1", goalRepo.findAllByUserAndImportanceAndCompletion(user, 1, false));
            model.addAttribute("importance0", goalRepo.findAllByUserAndImportanceAndCompletion(user, 0, false));
        }
        return "goals";

    }
}
