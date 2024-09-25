package com.olympus.exception;

import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppExceptionHandlerTest {
    private AppExceptionHandler appExceptionHandler;

    @BeforeEach
    void setUp() {
        appExceptionHandler = new AppExceptionHandler();
    }

    @Test
    void handleGeneralException() {
        Exception ex = new Exception("General error");
        ResponseEntity<?> responseEntity = appExceptionHandler.handleGeneralException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void handleMethodArgsException() {
        MethodArgumentNotValidException ex = createMethodArgumentNotValidException();
        ResponseEntity<?> responseEntity = appExceptionHandler.handleMethodArgsException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void handleConstraintException() {
        ConstraintViolationException ex = createConstraintViolationException();
        ResponseEntity<?> responseEntity = appExceptionHandler.handleConstraintException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void handleMessagingException() {
        MessagingException ex = new MessagingException("Message error");
        ResponseEntity<?> responseEntity = appExceptionHandler.handleMessagingException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void handleIOException() {
        IOException ex = new IOException("IO error");
        ResponseEntity<?> responseEntity = appExceptionHandler.handleIOException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void handleUserNotFoundException() {
        UserNotFoundException ex = new UserNotFoundException("User not found");
        ResponseEntity<?> responseEntity = appExceptionHandler.handleUserNotFoundException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void handleInvalidImageException() {
        InvalidImageException ex = new InvalidImageException();
        ResponseEntity<?> responseEntity = appExceptionHandler.handleInvalidImageException();
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void handleReportCreateException() {
        ReportCreateException ex = new ReportCreateException("Error creating report");
        ResponseEntity<?> responseEntity = appExceptionHandler.handleReportCreateException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    // Helpers to create exceptions for testing
    private MethodArgumentNotValidException createMethodArgumentNotValidException() {
        FieldError fieldError = new FieldError("objectName", "field", "default message");
        BindException bindException = new BindException(new Object(), "objectName");
        bindException.addError(fieldError);
        return new MethodArgumentNotValidException(null, bindException);
    }

    private ConstraintViolationException createConstraintViolationException() {
        ConstraintViolation<?> mockConstraintViolation = mock(ConstraintViolation.class);
        when(mockConstraintViolation.getMessage()).thenReturn("default message");

        // Mocking the path as before
        Path mockPath = mock(Path.class);
        when(mockConstraintViolation.getPropertyPath()).thenReturn(mockPath);
        Path.Node mockLastNode = mock(Path.Node.class);
        when(mockLastNode.getName()).thenReturn("fieldName");
        when(mockPath.iterator()).thenReturn(Collections.singletonList(mockLastNode).iterator());

        // Creating a set of ConstraintViolation as before
        Set<ConstraintViolation<?>> constraintViolations = Collections.singleton(mockConstraintViolation);

        // Now providing the message along with the set of ConstraintViolations
        return new ConstraintViolationException("Validation failed", constraintViolations);
    }
}