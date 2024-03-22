package com.intallysh.widom.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.intallysh.widom.util.ConstantValues;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		List<String> errorMessages = new ArrayList();
		ex.getBindingResult().getAllErrors().forEach(error -> errorMessages.add(error.getDefaultMessage()));
		problemDetail.setProperty(ConstantValues.MESSAGE, String.join(", ", errorMessages));
		problemDetail.setProperty(ConstantValues.STATUS, ConstantValues.ERROR_MESSAGE);
		problemDetail.setStatus(HttpStatus.BAD_REQUEST);
		problemDetail.setType(ConstantValues.ERROR_MESSAGE_URL);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
	}

	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<ProblemDetail> handleDisabledException(DisabledException ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
		problemDetail.setProperty(ConstantValues.MESSAGE, ex.getMessage());
		problemDetail.setProperty(ConstantValues.STATUS, ConstantValues.ERROR_MESSAGE);
		problemDetail.setStatus(HttpStatus.UNAUTHORIZED);
		problemDetail.setType(ConstantValues.ERROR_MESSAGE_URL);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ProblemDetail> handleBadCredentialsException(BadCredentialsException ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
		problemDetail.setProperty(ConstantValues.MESSAGE, ex.getMessage());
		problemDetail.setProperty(ConstantValues.STATUS, ConstantValues.ERROR_MESSAGE);
		problemDetail.setStatus(HttpStatus.UNAUTHORIZED);
		problemDetail.setType(ConstantValues.ERROR_MESSAGE_URL);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
	}

	@ExceptionHandler(ResourceNotProcessedException.class)
	public ResponseEntity<ProblemDetail> handleResourceNotProcessedException(ResourceNotProcessedException ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		problemDetail.setProperty(ConstantValues.MESSAGE, ex.getMessage());
		problemDetail.setProperty(ConstantValues.STATUS, ConstantValues.ERROR_MESSAGE);
		problemDetail.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		problemDetail.setType(ConstantValues.ERROR_MESSAGE_URL);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
	}
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ProblemDetail> handleForBiddenException(ForbiddenException ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
		problemDetail.setProperty(ConstantValues.MESSAGE, ex.getMessage());
		problemDetail.setProperty(ConstantValues.STATUS, ConstantValues.ERROR_MESSAGE);
		problemDetail.setStatus(HttpStatus.FORBIDDEN);
		problemDetail.setType(ConstantValues.ERROR_MESSAGE_URL);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problemDetail);
	}
}
