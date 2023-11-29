package com.intallysh.widom.service.impl;

import com.intallysh.widom.config.ModelMapperConfig;
import com.intallysh.widom.dto.RegisterDto;
import com.intallysh.widom.entity.Roles;
import com.intallysh.widom.entity.User;
import com.intallysh.widom.repo.UsersRepo;
import com.intallysh.widom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Map<String, Object> registerUser(RegisterDto reqDto) {
        User user = this.modelMapper.map(reqDto, User.class);
        user.setPassword(passwordEncoder.encode(reqDto.getPassword()));
        Set<Roles> roles = new HashSet<>();
        roles.add(new Roles(101, "NORMAL_ROLE","Normal User",user));
        user.setRoles(roles);
        try{
            user =  this.usersRepo.save(user);
            System.out.println(user);
            Map<String, Object> map = new HashMap<>();
            if(user != null){
                map.put("status","Success");
                map.put("message","User Saved Successfully !!!");
                map.put("userId",user.getUserId());
                return  map;
            } else throw new Exception("User not saved !!!");
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
