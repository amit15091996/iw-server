package com.intallysh.widom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.sql.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileReqDto {

	@NotBlank(message = "file type required")
    private String fileType;
	
	@Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", message = "Please Enter a valid report date")   
	@NotBlank(message = "Report Date is mandatory")
    private String reportDate;
	
    private List<MultipartFile> files;

}
