package com.intallysh.widom.dto;

import jakarta.validation.constraints.Email;
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
public class RegisterDto {

    @NotBlank(message = "Phone is mandatory")
    private String phone;
    @NotBlank(message = "name is mandatory")
    private String name;
    @NotBlank(message = "email is mandatory")
    @Email(message = "Invalid Email")
    private String email;
    @Pattern(regexp = "^(?=.*[a-z]|[A-Z]|[0-9])(?=\\S+$).{8,}$", message = "Please Enter a valid Password")
    private String password;
    @NotBlank(message = "GST is mandatory")
    private String gst;
    private String address;

}
