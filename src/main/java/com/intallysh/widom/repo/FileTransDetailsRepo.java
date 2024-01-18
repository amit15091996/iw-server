package com.intallysh.widom.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.intallysh.widom.entity.FileTransDetails;

public interface FileTransDetailsRepo extends JpaRepository<FileTransDetails, Long>{

	Page<FileTransDetails> findByUserIdAndYear(long userId,int year, Pageable pageable);
	
	Page<FileTransDetails> findByUserId(long userId, Pageable pageable);
	
}
