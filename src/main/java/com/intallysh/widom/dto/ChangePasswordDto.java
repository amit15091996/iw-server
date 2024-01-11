package com.intallysh.widom.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {

	private String oldPassword;
	@Pattern(regexp = "^(?=.*[a-z]|[A-Z]|[0-9])(?=\\S+$).{8,}$", message = "Please Enter a valid Password")
	private String newPassword;
	@Pattern(regexp = "^(?=.*[a-z]|[A-Z]|[0-9])(?=\\S+$).{8,}$", message = "Please Enter a valid Password")
	private String confirmPassword;
	
}
