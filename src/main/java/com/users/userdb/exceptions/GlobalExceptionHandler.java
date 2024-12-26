package com.users.userdb.exceptions;

import com.users.userdb.dto.response.ErrorDTO;
import com.users.userdb.dto.response.KeyValueErrorDTO;
import com.users.userdb.dto.response.ValidationErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.users.userdb.constants.ApplicationConstants.DATE_FORMAT;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDTO<KeyValueErrorDTO>> userNotFound(UserNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorDTO<>(false, e.getMessage(), new KeyValueErrorDTO(e.getKey(), e.getValue())),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO<List<ValidationErrorDTO>>> invalidField(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<ValidationErrorDTO> errorList = new ArrayList<>();

        for (FieldError error : result.getFieldErrors()) {
            if (error.getDefaultMessage() != null && !error.getField().equals("birthdate")) {
                errorList.add(new ValidationErrorDTO(
                        error.getField(), error.getDefaultMessage(),
                        (String) error.getRejectedValue())
                );
            }

            if (error.getDefaultMessage() != null && error.getField().equals("birthdate")) {
                Date value = (Date) error.getRejectedValue();

                if (value != null) {
                    errorList.add(new ValidationErrorDTO(
                            error.getField(), error.getDefaultMessage(),
                            DATE_FORMAT.format((Date) error.getRejectedValue()))
                    );
                }
                else errorList.add(new ValidationErrorDTO(error.getField(), error.getDefaultMessage(), null));
            }
        }

        return new ResponseEntity<>(
                new ErrorDTO<>(false, "Invalid field data", errorList), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDTO<KeyValueErrorDTO>> illegalArgument(MethodArgumentTypeMismatchException e) {
        return new ResponseEntity<>(
                new ErrorDTO<>(false, "Value of id must be numeric",
                        new KeyValueErrorDTO("id", (String) e.getValue())),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDTO<KeyValueErrorDTO>> entityNotFound(NoResourceFoundException e) {
        return new ResponseEntity<>(
                new ErrorDTO<>(false, "Requested resource not found",
                        new KeyValueErrorDTO("path", "/" + e.getResourcePath())),
                HttpStatus.NOT_FOUND);
    }
}
