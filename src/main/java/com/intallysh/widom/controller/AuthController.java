package com.intallysh.widom.controller;

import com.intallysh.widom.config.JwtTokenHelper;
import com.intallysh.widom.dto.LoginRequestDto;
import com.intallysh.widom.dto.RegisterDto;
import com.intallysh.widom.entity.User;
import com.intallysh.widom.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import javax.security.sasl.AuthenticationException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {


    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequestDto loginReq){

        this.authenticate(loginReq.getUsername(), loginReq.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(loginReq.getUsername());
        String token = this.jwtTokenHelper.generateToken(userDetails);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("tokenExpTime", this.jwtTokenHelper.getExpirationDateFromToken(token).toString());
        map.put("tokenExpTimeInMillis", this.jwtTokenHelper.getExpirationDateFromToken(token).getTime());
        map.put("message", "Token Generated Successfully ...");
        map.put("status", "Success");
        map.put("userName", userDetails.getUsername());
        map.put("userRoles", userDetails.getAuthorities().stream().map(auth -> auth.getAuthority()));
        try {
			Map<String,Object> userByUserName = userService.getUserByUserName(loginReq.getUsername());
			User user= (User)userByUserName.get("user");
			map.put("name", user.getName());
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        map.put("name", );
        return ResponseEntity.ok().body(map);
    }

    private void authenticate(String username, String password){

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);
        try {

            this.authenticationManager.authenticate(authenticationToken);

        } catch (DisabledException e) {
            throw new DisabledException("User is disabled !");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad Credentials !");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody RegisterDto req){


        return ResponseEntity.status(201).body(userService.registerUser(req));
    }
    
    @PostMapping("/forgot-password/{userName}")
    public ResponseEntity<Map<String, Object>> forgotPassword(@PathVariable String userName){


        return ResponseEntity.status(201).body(userService.forgotPassword(userName));
    }
}
