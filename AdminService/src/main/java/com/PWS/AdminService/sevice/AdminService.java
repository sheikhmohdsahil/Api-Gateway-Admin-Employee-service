package com.PWS.AdminService.sevice;

import com.PWS.AdminService.dto.*;
import com.PWS.AdminService.entity.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    //user
    public void userUpdatePassword(UpdatePasswordDto updatePasswordDto) throws Exception;
    public User userSignUp(SignUpDto signupDTO) throws Exception;
    Optional<User> findById(Integer id);
    public void updateUser(User user) throws Exception;
    List<User> findAll();


    List<User> saveUsers(List<User> user);
    void delete(Integer id);


    void updateResetPasswordToken(String token, String email) throws Exception;

    public Optional<User> getByResetPasswordToken(String token);

    public void updatePassword(ResetUpdatepassword resetUpdatepassword)throws Exception;
    //roleUser user, String newPassword, String confirmPassword
    public Role saveRole(Role role);
    Optional<Role> findRoleById(Integer id);
    List<Role> findAllRole();
    void updateRole(Role role) throws Exception;
    void deleteRole(Integer id);
    List<Role> saveRoles(List<Role> role);






//Model
    public Model saveModel(Model model);
    List<Model> saveModels(List<Model> model);
    Optional<Model> findModelById(Integer id);
    List<Model> findAllModel();
    void deleteModel(Integer id);
    public void updateModel(Model model) throws Exception;






    //user role reference
    public void saveOrUpdateUserRoleXref(UserRoleRefDto userRoleRefDto) throws Exception;
    void deleteUserRoleById(Integer id)throws Exception;
    Optional<UserRoleRef> fetchUserRoleRefId(Integer id);
    List<UserRoleRef> fetchAllUserRoleRef();
    public void updateUserRoleRef(UserRoleRefDto userRoleRefDto) throws Exception;








//permission

    Optional<Permission> fetchByPermissionId(Integer id);
    List<Permission> fetchAllPermission();
    void deletePermissionById(Integer id) throws Exception;
    public ResponseEntity<Object> addOrUpdatePermission(PermissionDto permissionDto) throws Exception;
    public void updatePermission(PermissionDto permissionDto) throws Exception;

//    void updatePassword(User user, String password) throws Exception;
}