package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Goal;
import com.example.demo.repositary.GoalRepo;

@Service
public class GoalService {
    @Autowired
    GoalRepo goalRepo;

    public void addGoal(Goal goal) {
        goalRepo.save(goal);
    }
}
