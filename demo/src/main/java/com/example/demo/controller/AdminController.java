package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Goal;
import com.example.demo.model.User;
import com.example.demo.repositary.GoalRepo;
import com.example.demo.repositary.UserRepo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AdminController {
    @Autowired
    UserRepo userrepo;
    @Autowired
    GoalRepo goalrepo;

    @GetMapping("/admin")
    public String admindashboard(Model model) {
        int totalusers = userrepo.findAll().size();
        List<Integer> grade1 = userrepo.findScoreByGrade1();
        List<Integer> grade2 = userrepo.findScoreByGrade2();
        List<Integer> grade3 = userrepo.findScoreByGrade3();
        List<Integer> grade4 = userrepo.findScoreByGrade4();
        List<Integer> grade5 = userrepo.findScoreByGrade5();
        model.addAttribute("AverageGrade1", calculateAverage(grade1));
        model.addAttribute("AverageGrade2", calculateAverage(grade2));
        model.addAttribute("AverageGrade3", calculateAverage(grade3));
        model.addAttribute("AverageGrade4", calculateAverage(grade4));
        model.addAttribute("AverageGrade5", calculateAverage(grade5));
        model.addAttribute("totalusers", totalusers);
        model.addAttribute("latestusers", userrepo.findTop5ByOrderByIdDesc());

        return "admin";
    }

    @GetMapping("/admin/users")
    public String adminuser(Model model) {
        model.addAttribute("users", userrepo.findAll());
        return "adminusers";
    }

    @RequestMapping("/admin/updateuser/{id}")
    public String requestMethodName(Model model, @PathVariable long id) {
        model.addAttribute("user", userrepo.findById(id).get());
        model.addAttribute("updateuser", new User());
        return "adminupdateuser";
    }

    @GetMapping("/admin/updateuser/accept/{id}")
    public String accepeteduserupdate(Model model, @ModelAttribute("updateuser") User user, @PathVariable long id) {
        User temp = userrepo.findById(id).get();
        temp.setUsername(user.getUsername());
        temp.setEmail(user.getEmail());
        temp.setPassword(user.getPassword());
        temp.setGrade(user.getGrade());
        temp.setScore(user.getScore());
        userrepo.save(temp);
        return "redirect:/admin/users";
    }

    private double calculateAverage(List<Integer> x) {
        return x.stream()
                .mapToDouble(d -> d)
                .average()
                .orElse(0.0);
    }
}
