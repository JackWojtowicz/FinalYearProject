package com.example.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Getter
@Table(name = "goals")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean completion;

    @OneToOne
    private User user;

    public Goal(String name, String description, boolean completion, User user) {
        this.name = name;
        this.description = description;
        this.completion = completion;
        this.user = user;
    }

    public boolean getCompletion() {
        return this.completion;
    }

    public long getId() {
        return this.id;
    }
}
