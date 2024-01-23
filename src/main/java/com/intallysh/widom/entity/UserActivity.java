package com.intallysh.widom.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivity {	
	@Id
	private String userActId;
	private long userId;
	private boolean isRead;
	private Timestamp modifiedOn;
	private String activityDone;
	private long activityDoneBy;
	private String fileTransId;
	
}
