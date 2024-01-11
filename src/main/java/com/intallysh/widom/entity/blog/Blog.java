package com.intallysh.widom.entity.blog;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Blog {
	@Id
	private String blogId;
	@Column(nullable = false)
	private String blogTitle;
	@Column(nullable = false, length = 1000)
	private String blogDesc1;
//	private String blogDesc2;
//	private String blogDesc3;
//	private String blogDesc4;
//	private String blogDesc5;
	@Column(nullable = false)
	private String bloggerId;
	@Column(nullable = false)
	private Timestamp blogUploadedOn;
	private Timestamp modifiedOn;
	private String modifiedBy;


	
	
}
