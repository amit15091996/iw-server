package com.intallysh.widom.repo;

import com.intallysh.widom.entity.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepo extends JpaRepository<User,Long> {
    User findByEmailOrPhone(String email, String phone);
    List<User> findAllByIsDeleted(boolean isDeleted);
    List<User> findAllByIsEnabled(boolean isEnabled);
}
