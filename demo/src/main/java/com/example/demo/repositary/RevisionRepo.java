package com.example.demo.repositary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Revision;

@Repository
public interface RevisionRepo extends JpaRepository<Revision, Long> {
}
