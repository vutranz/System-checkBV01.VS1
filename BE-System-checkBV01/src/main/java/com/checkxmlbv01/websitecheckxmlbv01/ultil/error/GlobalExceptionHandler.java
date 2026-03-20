package com.checkxmlbv01.websitecheckxmlbv01.ultil.error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.checkxmlbv01.websitecheckxmlbv01.domain.DTO.RestResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ================= VALIDATION ERROR =================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        return ResponseEntity
                .badRequest()
                .body(new RestResponse<>(
                        false,
                        "Dữ liệu không hợp lệ",
                        List.of("VALIDATION_ERROR"),
                        errors
                ));
    }

    // ================= BUSINESS ERROR =================
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<RestResponse<Void>> handleBusiness(BaseException ex) {

        return ResponseEntity
                .badRequest()
                .body(RestResponse.error(
                        ex.getMessage(),
                        List.of(ex.getCode())
                ));
    }

    // ================= METHOD NOT ALLOWED (405) =================
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<RestResponse<Void>> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex) {

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(RestResponse.error(
                        "Phương thức không được hỗ trợ",
                        List.of("METHOD_NOT_ALLOWED")
                ));
    }

    // ================= NOT FOUND (404) =================
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<RestResponse<Void>> handleNotFound(
            NoHandlerFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(RestResponse.error(
                        "API không tồn tại",
                        List.of("NOT_FOUND")
                ));
    }

    // ================= SYSTEM ERROR (500) =================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Void>> handleSystem(Exception ex) {

        log.error("SYSTEM ERROR", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(RestResponse.error(
                        "Lỗi hệ thống",
                        List.of("SYSTEM_ERROR")
                ));
    }
}


