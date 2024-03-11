package com.example.demo.repositary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Goal;
import java.util.List;
import com.example.demo.model.User;

@Repository
public interface GoalRepo extends JpaRepository<Goal, Long> {
    Optional<Goal> findAllByUser_Id(long user_id);

    List<Goal> findByUser(User user);
}
