package com.PWS.AdminService.repository;

import com.PWS.AdminService.entity.UserRoleRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRefRepository extends JpaRepository<UserRoleRef,Integer> {
}
