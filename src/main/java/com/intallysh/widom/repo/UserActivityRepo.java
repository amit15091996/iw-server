package com.intallysh.widom.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.intallysh.widom.entity.UserActivity;

public interface UserActivityRepo extends JpaRepository<UserActivity, String> {
	Page<UserActivity> findAllByUserId(long userId, Pageable paging);

}
