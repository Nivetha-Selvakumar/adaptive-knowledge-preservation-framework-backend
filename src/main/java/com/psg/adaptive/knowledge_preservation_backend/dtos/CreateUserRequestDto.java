package com.psg.adaptive.knowledge_preservation_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequestDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;

    private String address;

    private String dob;

    private String sex;

    private String role;

}
