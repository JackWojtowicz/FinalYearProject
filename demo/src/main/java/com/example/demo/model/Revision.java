package com.example.demo.model;

import java.sql.Blob;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "revision")
public class Revision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;
    @Column
    private String filetype;
    @Column(nullable = false)
    @Lob
    private byte[] material;

    public Revision(String name, String filetype, byte[] material) {
        this.name = name;
        this.filetype = filetype;
        this.material = material;
    }

    public String getStringId() {
        return Long.toString(this.id);
    }
}
