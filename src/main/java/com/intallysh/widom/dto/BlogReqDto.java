package com.intallysh.widom.dto;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogReqDto {

	private String blogId;
	@NotEmpty(message = "Blog Title is mandatory")
	private String blogTitle;
	@NotEmpty(message = "BLog Description is mandatory")
	private String blogDesc1;
	private String bloggerId;
	private Timestamp blogUploadedOn;
	private Timestamp modifiedOn;
	private String modifiedBy;
	
	@NotBlank(message = "file type required")
	private MultipartFile blogImage;

}
