package com.intallysh.widom.util;

import java.net.URI;

public class ConstantValues {

	public static final String FILE_NAME = "fileName";
	public static final String FOLDER = "folder";
	public static final String UPLOADED_DATE = "uploadedDate";
	public static final String FILE_ID = "fileId";	
	
	public static final String FILE_LOCATION = "C:/intallysh-wisdom/";
	
	public static final String FORGOT_PASSWORD_MAIL_TEMPLATE = "\r\n"
			+ "Hi [Name], <br /> \r\n" 
			+ "\r\n"
			+ "You recently requested to reset the password for your Intallysh Wisdom account. Below mentioned is the password for you account.\r\n"
			+ "<br /><br />\r\n"
			+ "UserName : [UserName] <br/>\r\n"
			+ "Password :  [Password]\r\n"
			+ "<br /><br />If you did not request a password reset, please don't share this password to anyone and please change your password.\r\n"
			+ "<br/><br/><br/>\r\n"
			+ "Thanks, <br />The Intallysh Wisdom team \r\n" ;
	
	
	public static final URI ERROR_MESSAGE_URL = URI.create("/error");
	public static final String ERROR_MESSAGE = "error";
	public static final String SUCCESS_MESSAGE = "success";
	public static final String MESSAGE = "message";
	public static final String STATUS = "status";

}
