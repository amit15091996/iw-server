package com.intallysh.widom.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileTransDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long fileTransDetailsId;
	private String transId;
	private long userId;
	private int year;
	private Date reportDate;
	private String fileType;
	private String fileDescription;
}
