package com.PWS.AdminService.dto;

import com.PWS.AdminService.entity.Role;
import com.PWS.AdminService.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleRefDto {

    private Integer id;

    private Integer userId;

    private Integer roleId;

    private Boolean isActive;
}
