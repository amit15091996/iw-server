package com.intallysh.widom.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilesDetail {
    @Id
    private String fileDetailsId;
    private String fileLocation;
    private Timestamp fileStoredTime;
    private String fileType;
    private String fileExtension;
    private String fileName;

    private long userId;
}
