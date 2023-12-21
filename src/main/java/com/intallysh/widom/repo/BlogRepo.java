package com.intallysh.widom.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intallysh.widom.entity.blog.Blog;

public interface BlogRepo extends JpaRepository<Blog, String>{

}
