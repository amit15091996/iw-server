package com.intallysh.widom.dto;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponseDto {

	
	private String blogId;
	private String blogTitle;
	private String blogDesc1;
	private String bloggerId;
	private Timestamp blogUploadedOn;
	private Timestamp modifiedOn;
	private String modifiedBy;
	public boolean isDeleted;	
	private String fileLocation;
	private Map<String, Object> fileDetails = new HashMap<>();
}
