package com.intallysh.widom.repo;

import com.intallysh.widom.entity.User;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepo extends JpaRepository<User,Long> {
    User findByEmailOrPhone(String email, String phone);
    Page<User> findAllByIsDeleted(boolean isDeleted, Pageable paging);
    Page<User> findAllByIsEnabled(boolean isEnabled, Pageable paging);
}
