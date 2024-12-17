package com.demo.d2s_demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private long fileSize;
    private LocalDateTime createdAt;

    // Constructor
    public FileMetadata(String filename, long fileSize, LocalDateTime createdAt) {
        this.fileName = filename;
        this.fileSize = fileSize;
        this.createdAt = createdAt;
    }
}

