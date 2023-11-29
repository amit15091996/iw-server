package com.intallysh.widom.service;

import com.intallysh.widom.dto.RegisterDto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserService {

    public Map<String, Object> registerUser(RegisterDto registerDto);


}
