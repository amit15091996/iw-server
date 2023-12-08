package com.intallysh.widom.service;

import com.intallysh.widom.dto.RegisterDto;
import com.intallysh.widom.dto.UpdateUserReqDto;

import org.springframework.stereotype.Service;

import java.util.Map;

import javax.security.sasl.AuthenticationException;

@Service
public interface UserService {

    public Map<String, Object> registerUser(RegisterDto registerDto);
    public Map<String, Object> updateProfile(UpdateUserReqDto updateUserReqDto)throws AuthenticationException ;
    public Map<String, Object> deleteProfile(long userId)throws AuthenticationException ;
    public Map<String, Object> getAllUsers(String type)throws AuthenticationException ;
    public Map<String, Object> blockUser(long userId)throws AuthenticationException ;
    public Map<String, Object> unBlockUser(long userId)throws AuthenticationException ;
    
}
