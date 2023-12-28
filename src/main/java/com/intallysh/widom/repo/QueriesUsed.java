package com.intallysh.widom.repo;

public class QueriesUsed {

	public static final String GET_UPLOADED_FILE_YEARS_BY_USERID = "SELECT DISTINCT YEAR(report_date) AS report_year\r\n"
			+ "FROM files_detail\r\n"
			+ "WHERE user_id = ?1";
	
	
	public static final String GET_FILEDETAIL_BY_YEAR_AND_USERID = "SELECT fd\r\n"
			+ "FROM FilesDetail fd\r\n"
			+ "WHERE fd.user_id = :userId AND FUNCTION('YEAR', fd.report_date) = :selectedYear\r\n"
			+ "AND fd.file_details_id IN (\r\n"
			+ "    SELECT DISTINCT fd2.file_details_id\r\n"
			+ "    FROM FilesDetail fd2\r\n"
			+ "    WHERE fd2.user_id = :userId AND FUNCTION('YEAR', fd2.report_date) = :selectedYear\r\n"
			+ "    ORDER BY fd2.report_date DESC\r\n"
			+ ")\r\n"
			+ "";
	
}
