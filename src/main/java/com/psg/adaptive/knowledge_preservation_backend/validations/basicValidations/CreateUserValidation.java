package com.psg.adaptive.knowledge_preservation_backend.validations.basicValidations;

import com.psg.adaptive.knowledge_preservation_backend.dtos.CreateUserRequestDto;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class CreateUserValidation {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[6-9][0-9]{9}$");

    public void validate(CreateUserRequestDto dto) throws CommonException {

        if (dto == null) {
            throw new CommonException("Request cannot be empty",
                    HttpStatus.BAD_REQUEST.value());
        }

        validateFirstName(dto.getFirstName());
        validateLastName(dto.getLastName());
        validateEmail(dto.getEmail());
        validatePhoneNumber(dto.getPhoneNumber());
        validatePassword(dto.getPassword());
        validateSex(dto.getSex());
        validateDob(dto.getDob());
    }

    private void validateFirstName(String firstName) throws CommonException {

        if (firstName == null || firstName.trim().isEmpty()) {
            throw new CommonException("First name is required",
                    HttpStatus.BAD_REQUEST.value());
        }

        if (firstName.length() > 100) {
            throw new CommonException("First name should not exceed 100 characters",
                    HttpStatus.BAD_REQUEST.value());
        }
    }

    private void validateLastName(String lastName) throws CommonException {

        if (lastName != null && lastName.length() > 100) {
            throw new CommonException("Last name should not exceed 100 characters",
                    HttpStatus.BAD_REQUEST.value());
        }
    }

    private void validateEmail(String email) throws CommonException {

        if (email == null || email.isBlank()) {
            throw new CommonException("Email is required",
                    HttpStatus.BAD_REQUEST.value());
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new CommonException("Invalid email address",
                    HttpStatus.BAD_REQUEST.value());
        }
    }

    private void validatePhoneNumber(String phone) throws CommonException {

        if (phone == null || phone.isBlank()) {
            throw new CommonException("Phone number is required",
                    HttpStatus.BAD_REQUEST.value());
        }

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new CommonException("Invalid phone number",
                    HttpStatus.BAD_REQUEST.value());
        }
    }

    private void validatePassword(String password) throws CommonException {

        if (password == null || password.isBlank()) {
            throw new CommonException("Password is required",
                    HttpStatus.BAD_REQUEST.value());
        }

        if (password.length() < 8) {
            throw new CommonException("Password must contain at least 8 characters",
                    HttpStatus.BAD_REQUEST.value());
        }
    }

    private void validateSex(String sex) throws CommonException {

        if (sex == null || sex.isBlank()) {
            throw new CommonException("Sex is required",
                    HttpStatus.BAD_REQUEST.value());
        }
    }

    private void validateDob(String dob) throws CommonException {

        if (dob == null || dob.isBlank()) {
            throw new CommonException("Date of birth is required",
                    HttpStatus.BAD_REQUEST.value());
        }
    }
}
