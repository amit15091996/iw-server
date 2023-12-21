package com.intallysh.widom.service.impl;

import com.intallysh.widom.config.SecurityUtil;
import com.intallysh.widom.dto.RegisterDto;
import com.intallysh.widom.dto.UpdateUserReqDto;
import com.intallysh.widom.entity.Roles;
import com.intallysh.widom.entity.User;
import com.intallysh.widom.exception.ResourceNotProcessedException;
import com.intallysh.widom.repo.UsersRepo;
import com.intallysh.widom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.security.sasl.AuthenticationException;

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

		User userFromDB = usersRepo.findByEmailOrPhone(reqDto.getEmail(), reqDto.getPhone());
		if (userFromDB != null) {
			throw new ResourceNotProcessedException("User is already present with given phone or email");
		}

		User user = this.modelMapper.map(reqDto, User.class);
		user.setPassword(passwordEncoder.encode(reqDto.getPassword()));
		user.setDeleted(false);
		user.setModifiedOn(new Timestamp(System.currentTimeMillis()));
		user.setRegisterDate(new Timestamp(System.currentTimeMillis()));
		Set<Roles> roles = new HashSet<>();
		roles.add(new Roles(101, "NORMAL_ROLE", "Normal User", user));
		user.setRoles(roles);
		try {
			user = this.usersRepo.save(user);
			System.out.println(user);
			Map<String, Object> map = new HashMap<>();
			if (user != null) {
				map.put("status", "Success");
				map.put("message", "User Saved Successfully !!!");
				map.put("userId", user.getUserId());
				return map;
			} else
				throw new ResourceNotProcessedException("User not saved !!!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("User not saved !!!");
		}
	}

	@Override
	public Map<String, Object> updateProfile(UpdateUserReqDto updateUserReqDto) throws AuthenticationException {
		Map<String, Object> map = new HashMap<>();
		User user = usersRepo.findById(updateUserReqDto.getUserId())
				.orElseThrow(() -> new ResourceNotProcessedException("Please Enter a valid User"));

		user.setAddress(updateUserReqDto.getAddress());
		user.setGstNo(updateUserReqDto.getGstNo());
		user.setPhone(updateUserReqDto.getPhone());
		user.setName(updateUserReqDto.getPhone());

		user.setModifiedOn(new Timestamp(System.currentTimeMillis()));
		user.setModifiedBy(SecurityUtil.getCurrentUserDetails().getUserId());

		try {
			user = usersRepo.save(user);
			map.put("message", "user updated successfully");
			map.put("status", "Success");
			map.put("userId", user.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("User not updated ...");
		}
		return map;
	}

	@Override
	public Map<String, Object> deleteProfile(long userId) throws AuthenticationException {
		Map<String, Object> map = new HashMap<>();
		User user = usersRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotProcessedException("Please Enter a valid User"));
		user.setDeleted(true);
		user.setEnabled(false);
		user.setModifiedBy(SecurityUtil.getCurrentUserDetails().getUserId());
		user.setModifiedOn(new Timestamp(System.currentTimeMillis()));
		try {
			user = usersRepo.save(user);
			map.put("message", "user deleted successfully");
			map.put("status", "Success");
			map.put("userId", user.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("User not updated ...");
		}
		return map;
	}

	@Override
	public Map<String, Object> getAllUsers(String type, Pageable paging) throws AuthenticationException {
		Map<String, Object> map = new HashMap<>();
		Page<User> pageList;
		try {

			switch (type) {
			case "NOT_DELETED": {
				pageList = usersRepo.findAllByIsDeleted(false, paging);
				break;
			}
			case "DELETED": {
				pageList = usersRepo.findAllByIsDeleted(true, paging);
				break;
			}
			case "BLOCKED_USERS": {
				pageList = usersRepo.findAllByIsEnabled(false, paging);
				break;
			}
			case "UN_BLOCKED_USERS": {
				pageList = usersRepo.findAllByIsEnabled(true, paging);
				break;
			}
			default:
				pageList = usersRepo.findAll(paging);
				break;
			}
			if (pageList.hasContent()) {
				map.put("usersList", pageList.getContent());
			}else {
				map.put("usersList", new ArrayList<>());
			}
			map.put("totalPages", pageList.getTotalPages());
	        map.put("totalResults", pageList.getTotalElements());
	        map.put("currentPage", pageList.getNumber());
	        map.put("noOfElements", pageList.getNumberOfElements());
	        map.put("isLastPage", pageList.isLast());
	        map.put("isFirstPage", pageList.isFirst());
			map.put("message", "Fetched Users successfully");
			map.put("status", "Success");

		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("Something went wrong try again ...");
		}
		return map;
	}

	@Override
	public Map<String, Object> blockUser(long userId) throws AuthenticationException {
		Map<String, Object> map = new HashMap<>();
		User user = usersRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotProcessedException("Please Enter a valid User"));
		// user.setDeleted(true);
		user.setEnabled(false);
		user.setModifiedBy(SecurityUtil.getCurrentUserDetails().getUserId());
		user.setModifiedOn(new Timestamp(System.currentTimeMillis()));
		try {
			user = usersRepo.save(user);
			map.put("message", "user blocked successfully");
			map.put("status", "Success");
			map.put("userId", user.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("User not blocked ...");
		}
		return map;
	}

	@Override
	public Map<String, Object> unBlockUser(long userId) throws AuthenticationException {
		Map<String, Object> map = new HashMap<>();
		User user = usersRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotProcessedException("Please Enter a valid User"));
		// user.setDeleted(true);
		user.setEnabled(true);
		user.setModifiedBy(SecurityUtil.getCurrentUserDetails().getUserId());
		user.setModifiedOn(new Timestamp(System.currentTimeMillis()));
		try {
			user = usersRepo.save(user);
			map.put("message", "user unblocked successfully");
			map.put("status", "Success");
			map.put("userId", user.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("User not unblocked ...");
		}
		return map;
	}
	@Override
	public Map<String, Object> getUserByUserName(String  username) throws AuthenticationException {
		Map<String, Object> map = new HashMap<>();
		User user = usersRepo.findByEmailOrPhone(username, username);
		if(user == null) {
			throw new ResourceNotProcessedException("Not a vaid user ...");
		}		
			
			map.put("message", "user fetched successfully");
			map.put("status", "Success");
			map.put("user", user);
		
		return map;
	}
}
