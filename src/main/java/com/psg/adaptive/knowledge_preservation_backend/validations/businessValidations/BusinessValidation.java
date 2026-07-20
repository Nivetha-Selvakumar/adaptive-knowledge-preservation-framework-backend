package com.psg.adaptive.knowledge_preservation_backend.validations.businessValidations;


import com.psg.adaptive.knowledge_preservation_backend.entities.UserEntity;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumStatus;
import com.psg.adaptive.knowledge_preservation_backend.exception.CommonException;
import com.psg.adaptive.knowledge_preservation_backend.repositories.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BusinessValidation {

    private static final Logger logger =
            LoggerFactory.getLogger(BusinessValidation.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepo userRepo;

    public UserEntity checkUserEmailExist(String email) throws CommonException {
        logger.info("Checking for Email and Password Exist");
        Optional<UserEntity> userEntity = userRepo.findByEmail(email);
        UserEntity user = userEntity.orElseThrow(() ->
                new CommonException("Email not exist", HttpStatus.BAD_REQUEST.value())
        );

        if (user.getStatus() != EnumStatus.ACTIVE) {
            throw new CommonException("User is not active", HttpStatus.BAD_REQUEST.value());
        }
        return user;
    }

    public void checkPassword(String passwordStored, String passwordReceived) throws CommonException {
        logger.info("Validating Password");
        if (!passwordEncoder.matches(passwordReceived, passwordStored)) {
            logger.warn("Password validation failed");
            throw new CommonException("Password doesn't match", HttpStatus.UNAUTHORIZED.value());
        }
    }
//
//    public void checkAdminRole(String role) throws CommonException {
//        logger.info("Validating Admin Role");
//        if (!role.equalsIgnoreCase(EnumRole.ADMIN.getCode())) {
//            logger.info("Error in validating Admin");
//            throw new CommonException("You are not an admin. Not Allowed to logging in", HttpStatus.BAD_REQUEST.value());
//        }
//    }
//
//    public void checkEmailExistAndStatus(String email) throws CommonException {
//        logger.info("Checking for Email Exist");
//        Optional<UserEntity> adminFacultyData = userRepo.findByEmailAndRole(email,EnumRole.ADMIN);
//        if (adminFacultyData.isPresent() && adminFacultyData.get().getStatus().equals(EnumStatus.ACTIVE)) {
//            logger.error("Email Already exist");
//            throw new CommonException("Email Already exist for Admin", HttpStatus.BAD_REQUEST.value());
//        }
//    }

    public void validateUserAndAuthToken(String id, String createdBy) throws CommonException {
        logger.info("Validate User Id and Auth User Id");
        if (!id.equals(createdBy)) {
            logger.info("Error in User Id and Auth User Id");
            throw new CommonException("Auth token and Creating User Not Match", HttpStatus.UNAUTHORIZED.value());
        }
    }

    public void checkEmailAlreadyExists(String email)
            throws CommonException {

        if (userRepo.existsByEmail(email)) {

            throw new CommonException(
                    "Email already exists",
                    HttpStatus.BAD_REQUEST.value()
            );

        }
    }

//    public void checkAdminFaculty(String role) throws CommonException {
//        logger.info("Check for Admin");
//        if(!role.equalsIgnoreCase(EnumRole.ADMIN.getCode())){
//            logger.info("Error Not an Admin");
//            throw new CommonException("You are not an Admin",HttpStatus.BAD_REQUEST.value());
//        }
//    }
//
//    public UserEntity checkFacultyEmailExist(String email) throws CommonException {
//        logger.info("Checking for Email and Password Exist for Faculty");
//        Optional<UserEntity> facultyEntity = userRepo.findByEmail(email);
//        UserEntity faculty = facultyEntity.orElseThrow(() ->
//                new CommonException("Email not exist", HttpStatus.BAD_REQUEST.value())
//        );
//
//        if (faculty.getRole() != EnumRole.FACULTY) {
//            throw new CommonException("You are not a Faculty", HttpStatus.BAD_REQUEST.value());
//        }
//
//        if (faculty.getStatus() != EnumStatus.ACTIVE) {
//            throw new CommonException("Faculty is not active", HttpStatus.BAD_REQUEST.value());
//        }
//        return faculty;
//    }
}
