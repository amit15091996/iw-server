package com.intallysh.widom.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.intallysh.widom.entity.blog.Blog;

public interface BlogRepo extends JpaRepository<Blog, String>{

	Page<Blog> findAllByIsDeleted(boolean isDeleted, Pageable paging);
}
