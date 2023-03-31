package com.PWS.AdminService.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    private int id;

    private String firstName;

    private String lastName;


//    @Column(nullable = false)
    private Date dob;

    private String email;

    private long phoneNumber;
    private String password;

    private Boolean isActive;

}