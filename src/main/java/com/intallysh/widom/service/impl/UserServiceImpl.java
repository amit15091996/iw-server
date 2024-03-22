package com.intallysh.widom.service.impl;

import com.intallysh.widom.config.SecurityUtil;
import com.intallysh.widom.dto.ChangePasswordDto;
import com.intallysh.widom.dto.RegisterDto;
import com.intallysh.widom.dto.SearchUserResponseDto;
import com.intallysh.widom.dto.UpdateUserReqDto;
import com.intallysh.widom.entity.Roles;
import com.intallysh.widom.entity.User;
import com.intallysh.widom.entity.UserActivity;
import com.intallysh.widom.exception.ResourceNotProcessedException;
import com.intallysh.widom.repo.BlogRepo;
import com.intallysh.widom.repo.FilesDetailRepo;
import com.intallysh.widom.repo.UserActivityRepo;
import com.intallysh.widom.repo.UsersRepo;
import com.intallysh.widom.service.UserService;
import com.intallysh.widom.util.ConstantValues;
import com.intallysh.widom.util.Utils;

import jakarta.transaction.Transactional;

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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.security.sasl.AuthenticationException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UsersRepo usersRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private Utils utils;

	@Autowired
	private UserActivityRepo activityRepo;

	@Autowired
	private FilesDetailRepo filesDetailRepo;

	@Autowired
	private BlogRepo blogRepo;

	@Override
	@Transactional(rollbackOn = { Exception.class })
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
			UserActivity activity = UserActivity.builder().userActId(UUID.randomUUID().toString())
					.activityDoneBy(user.getUserId()).activityDone("Created User")
					.modifiedOn(new Timestamp(System.currentTimeMillis())).userId(user.getUserId()).isRead(false)
					.build();
			this.activityRepo.save(activity);

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
	@Transactional(rollbackOn = { Exception.class })
	public Map<String, Object> updateProfile(UpdateUserReqDto updateUserReqDto) throws AuthenticationException {
		Map<String, Object> map = new HashMap<>();
		User user = usersRepo.findById(updateUserReqDto.getUserId())
				.orElseThrow(() -> new ResourceNotProcessedException("Please Enter a valid User"));

		user.setAddress(updateUserReqDto.getAddress());
		user.setGstNo(updateUserReqDto.getGstNo());
		user.setPhone(updateUserReqDto.getPhone());
		user.setName(updateUserReqDto.getName());

		user.setModifiedOn(new Timestamp(System.currentTimeMillis()));
		user.setModifiedBy(SecurityUtil.getCurrentUserDetails().getUserId());

		try {
			user = usersRepo.save(user);
			UserActivity activity = UserActivity.builder().userActId(UUID.randomUUID().toString())
					.activityDoneBy(SecurityUtil.getCurrentUserDetails().getUserId())
					.activityDone("Updated User Account").modifiedOn(new Timestamp(System.currentTimeMillis()))
					.userId(user.getUserId()).isRead(false).build();
			this.activityRepo.save(activity);
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
	@Transactional(rollbackOn = { Exception.class })
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
			UserActivity activity = UserActivity.builder().userActId(UUID.randomUUID().toString())
					.activityDoneBy(SecurityUtil.getCurrentUserDetails().getUserId())
					.activityDone("Deleted User Account").modifiedOn(new Timestamp(System.currentTimeMillis()))
					.userId(user.getUserId()).isRead(false).build();
			this.activityRepo.save(activity);
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
			} else {
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
	@Transactional(rollbackOn = { Exception.class })
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
			UserActivity activity = UserActivity.builder().userActId(UUID.randomUUID().toString())
					.activityDoneBy(SecurityUtil.getCurrentUserDetails().getUserId())
					.activityDone("User Account Blocked").modifiedOn(new Timestamp(System.currentTimeMillis()))
					.userId(user.getUserId()).isRead(false).build();
			this.activityRepo.save(activity);
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
	@Transactional(rollbackOn = { Exception.class })
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
			UserActivity activity = UserActivity.builder().userActId(UUID.randomUUID().toString())
					.activityDoneBy(SecurityUtil.getCurrentUserDetails().getUserId())
					.activityDone("User Account Unblocked").modifiedOn(new Timestamp(System.currentTimeMillis()))
					.userId(user.getUserId()).isRead(false).build();
			this.activityRepo.save(activity);
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
	public Map<String, Object> getUserByUserName(String username) throws AuthenticationException {
		Map<String, Object> map = new HashMap<>();
		User user = usersRepo.findByEmailOrPhone(username, username);
		if (user == null) {
			throw new ResourceNotProcessedException("Not a vaid user ...");
		}
		map.put("message", "user fetched successfully");
		map.put("status", "Success");
		map.put("user", user);

		return map;
	}

	@Override
	@Transactional(rollbackOn = { Exception.class })
	public Map<String, Object> forgotPassword(String username) {
		Map<String, Object> map = new HashMap<>();

		User user = this.usersRepo.findByEmailOrPhone(username, username);
		if (user == null) {
			throw new ResourceNotProcessedException("Not a valid User !");
		}
		String generateRandomPassword = Utils.generateRandomPassword(10);
		System.out.println("Random Generated Password : " + generateRandomPassword);

		user.setPassword(passwordEncoder.encode(generateRandomPassword));
		String forgotPasswordMailTemplate = ConstantValues.FORGOT_PASSWORD_MAIL_TEMPLATE;
		forgotPasswordMailTemplate = forgotPasswordMailTemplate.replace("[UserName]", user.getEmail());
		forgotPasswordMailTemplate = forgotPasswordMailTemplate.replace("[Name]", user.getName());
		forgotPasswordMailTemplate = forgotPasswordMailTemplate.replace("[Password]", generateRandomPassword);
		boolean sendSimpleMail = utils.sendSimpleMail(user.getEmail(), "Intallysh Wisdom Password Reset",
				forgotPasswordMailTemplate);
		if (sendSimpleMail) {
			try {
				user=usersRepo.save(user);
				System.out.println("User Updated Successfully....");
				
				UserActivity activity = UserActivity.builder().userActId(UUID.randomUUID().toString())
						.activityDoneBy(user.getUserId())
						.activityDone("Password Reset sent to mail")
						.modifiedOn(new Timestamp(System.currentTimeMillis())).userId(user.getUserId()).isRead(false)
						.build();
				System.out.println(activity.toString());
				this.activityRepo.save(activity);
				System.out.println("User Activity updated Successfully");
				map.put("status", "Success");
				map.put("message", "Password Sent to your mail : " + user.getEmail());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new ResourceNotProcessedException("Password not updated ...");
		}

		return map;
	}

	@Override
	@Transactional(rollbackOn = { Exception.class })
	public Map<String, Object> ChangePassword(long userId, ChangePasswordDto changePasswordDto) {
		Map<String, Object> map = new HashMap<>();
		if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
			throw new ResourceNotProcessedException("New Password and confirm password should be same ...");
		}
		User user = usersRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotProcessedException("Please Enter a valid User"));
		if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
			throw new ResourceNotProcessedException("You have entered incorrect old password ...");
		}
		user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
		try {
			usersRepo.save(user);
			UserActivity activity = UserActivity.builder().userActId(UUID.randomUUID().toString())
					.activityDoneBy(SecurityUtil.getCurrentUserDetails().getUserId()).activityDone("Password Changed")
					.modifiedOn(new Timestamp(System.currentTimeMillis())).userId(user.getUserId()).isRead(false)
					.build();
			this.activityRepo.save(activity);
			map.put("status", "Success");
			map.put("message", "Your Password has been changed ..");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("Password not updated ...");
		}
		return map;
	}

	@Override
	public Map<String, Object> getCounts() {
		long usersCount = this.usersRepo.count();
		long blogsCount = this.blogRepo.count();
		long filesCount = this.filesDetailRepo.count();
		Map<String, Object> map = new HashMap<>();
		map.put("status", "Success");
		map.put("message", "Count fetched successfully");
		map.put("usersCount", usersCount);
		map.put("blogsCount", blogsCount);
		map.put("filesCount", filesCount);

		return map;
	}

	@Override
	public Map<String, Object> searchUser(String keyword) {
		Map<String, Object> map = new HashMap<>();
		List<User> searchUsers = this.usersRepo.searchUsers(keyword);
		List<SearchUserResponseDto> responseDtos = new ArrayList<>();
		searchUsers.forEach(s->{
			responseDtos.add(this.modelMapper.map(s, SearchUserResponseDto.class));			
		});
		map.put("status", "Success");
		map.put("message", "Count fetched successfully");
		map.put("users", responseDtos);
		map.put("usersCount", responseDtos.size());
		return map;
	}

}
