package com.intallysh.widom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserReqDto {
	private long userId;
	@NotBlank(message = "name is mandatory")
	private String name;
	@NotBlank(message = "Phone is mandatory")	
	@Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$", message = "Please Enter a valid Phone No")
	private String phone;	
	private String address;
	@NotBlank(message = "GST is mandatory")
	private String gstNo;

}
