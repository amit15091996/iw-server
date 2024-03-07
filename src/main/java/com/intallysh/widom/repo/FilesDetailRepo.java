package com.intallysh.widom.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.intallysh.widom.entity.FilesDetail;

public interface FilesDetailRepo extends JpaRepository<FilesDetail, Long> {

	@Query(nativeQuery = true, value = QueriesUsed.GET_UPLOADED_FILE_YEARS_BY_USERID)
	List<Long> getUploadedFileYearByUserId(long userId); 
	

	List<FilesDetail> findByTransId(String transId);
	
	
	
}
