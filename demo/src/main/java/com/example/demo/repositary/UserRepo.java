package com.example.demo.repositary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findTop10ByOrderByScoreDesc();

    List<User> findAllByOrderByScoreDesc();

    List<User> findTop5ByOrderByIdDesc();

    @Query("SELECT score FROM User user WHERE user.grade = 1")
    List<Integer> findScoreByGrade1();

    @Query("SELECT score FROM User user WHERE user.grade = 2")
    List<Integer> findScoreByGrade2();

    @Query("SELECT score FROM User user WHERE user.grade = 3")
    List<Integer> findScoreByGrade3();

    @Query("SELECT score FROM User user WHERE user.grade = 4")
    List<Integer> findScoreByGrade4();

    @Query("SELECT score FROM User user WHERE user.grade = 5")
    List<Integer> findScoreByGrade5();
}
