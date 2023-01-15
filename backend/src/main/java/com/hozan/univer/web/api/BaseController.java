package com.hozan.univer.web.api;

import com.hozan.univer.exception.InternalException;
import com.hozan.univer.web.DefaultExceptionAttributes;
import com.hozan.univer.web.ExceptionAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Map;

@ControllerAdvice
public class BaseController {
   protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(
            ConstraintViolationException exception, HttpServletRequest request) {

        logger.info("> handleConstraintViolationException");

        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes
                .getExceptionAttributes(exception, request,
                        HttpStatus.BAD_REQUEST);

        logger.info("< handleConstraintViolationException");
        return new ResponseEntity<Map<String, Object>>(responseBody,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, Object>> handleIOException(
            NoResultException noResultException, HttpServletRequest request) {

        logger.info("> handleIOException");

        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes
                .getExceptionAttributes(noResultException, request,
                        HttpStatus.BAD_REQUEST);

        logger.info("< handleIOException");
        return new ResponseEntity<Map<String, Object>>(responseBody,
                HttpStatus.BAD_REQUEST);
    }

   @ExceptionHandler(NoResultException.class)
   public ResponseEntity<Map<String, Object>> handleNoResultException(
           NoResultException noResultException, HttpServletRequest request) {

      logger.info("> handleNoResultException");

      ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

      Map<String, Object> responseBody = exceptionAttributes
              .getExceptionAttributes(noResultException, request,
                      HttpStatus.NOT_FOUND);

      logger.info("< handleNoResultException");
      return new ResponseEntity<Map<String, Object>>(responseBody,
              HttpStatus.NOT_FOUND);
   }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException methodArgNotValid, HttpServletRequest request) {

        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes
                .getExceptionAttributes(methodArgNotValid, request,
                        HttpStatus.BAD_REQUEST);


        String errorMsg = methodArgNotValid.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(methodArgNotValid.getMessage());

        responseBody.replace("message", errorMsg);

        return new ResponseEntity<Map<String, Object>>(responseBody,
                HttpStatus.BAD_REQUEST);
    }

   @ExceptionHandler(EntityExistsException.class)
   public ResponseEntity<Map<String, Object>> handleEntityExistsException(
           EntityExistsException entityExists, HttpServletRequest request) {
      logger.info("> handleEntityExistsException");

      ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

      Map<String, Object> responseBody = exceptionAttributes
              .getExceptionAttributes(entityExists, request,
                      HttpStatus.BAD_REQUEST);

       logger.info("< handleEntityExistsException");
      return new ResponseEntity<Map<String, Object>>(responseBody,
              HttpStatus.BAD_REQUEST);
   }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityExistsException(
            EntityNotFoundException entityNotFound, HttpServletRequest request) {
        logger.info("> handleEntityNotFoundException");

        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes
                .getExceptionAttributes(entityNotFound, request,
                        HttpStatus.BAD_REQUEST);

        logger.info("< handleEntityNotFoundException");
        return new ResponseEntity<Map<String, Object>>(responseBody,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCreidentialsExcepton(
            BadCredentialsException badCridentials , HttpServletRequest request) {
        logger.info("> handleBadCridentialsException");

        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes
                .getExceptionAttributes(badCridentials, request,
                        HttpStatus.BAD_REQUEST);

        logger.info("< handleBadCridentialsException");
        return new ResponseEntity<Map<String, Object>>(responseBody,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<Map<String, Object>> handleInternalException(
            InternalException internalException, HttpServletRequest request) {

        logger.info("> handleInternalException");

        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes
                .getExceptionAttributes(internalException, request,
                        HttpStatus.INTERNAL_SERVER_ERROR);

        logger.info("< handleInternalException");
        return new ResponseEntity<Map<String, Object>>(responseBody,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFoundException(
            UsernameNotFoundException userNotFound, HttpServletRequest request) {

        logger.info("> handleUsernameNotFoundException");

        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes
                .getExceptionAttributes(userNotFound, request,
                        HttpStatus.BAD_REQUEST);

        logger.info("< handleUsernameNotFoundException");
        return new ResponseEntity<Map<String, Object>>(responseBody,
                HttpStatus.BAD_REQUEST);
    }


   @ExceptionHandler(Exception.class)
   public ResponseEntity<Map<String, Object>> handleException(
           Exception exception, HttpServletRequest request) {

      logger.error("> handleException");
      logger.error("- Exception: ", exception);

      ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

      Map<String, Object> responseBody = exceptionAttributes
              .getExceptionAttributes(exception, request,
                      HttpStatus.INTERNAL_SERVER_ERROR);

      logger.error("< handleException");
      return new ResponseEntity<Map<String, Object>>(responseBody,
              HttpStatus.INTERNAL_SERVER_ERROR);
   }
}
