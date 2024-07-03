package com.intallysh.widom.repo;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.intallysh.widom.entity.FileTransDetails;

public interface FileTransDetailsRepo extends JpaRepository<FileTransDetails, Long> {

	// Method to find FileTransDetails by userId and year
	Page<FileTransDetails> findByUserIdAndYear(long userId, int year, Pageable pageable);

	// Method to find FileTransDetails by userId and a custom date range
	@Query("SELECT f FROM FileTransDetails f WHERE f.userId = ?1 AND f.reportDate BETWEEN ?2 AND ?3")
	Page<FileTransDetails> findByUserIdAndCustomDate(long userId, Date fromDate, Date toDate, Pageable pageable);

	// Method to find FileTransDetails by userId
	Page<FileTransDetails> findByUserId(long userId, Pageable pageable);

	@Modifying
	@Query("DELETE FROM FileTransDetails f WHERE f.transId = :transId")
	int deleteByTransId(@Param("transId") String transId);

	public FileTransDetails findByTransId(String transId);

}
