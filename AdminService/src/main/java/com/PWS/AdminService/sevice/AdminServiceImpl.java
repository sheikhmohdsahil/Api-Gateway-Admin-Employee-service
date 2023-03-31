package com.PWS.AdminService.sevice;
import com.PWS.AdminService.dto.*;
import com.PWS.AdminService.entity.*;
import com.PWS.AdminService.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRefRepository userRoleRefRepository;
    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private PermissionRepository permissionRepository;


    //users

    @Override
    public void userUpdatePassword(UpdatePasswordDto updatePasswordDto) throws Exception {

        Optional<User> optionalUser = userRepository.findUserByEmail(updatePasswordDto.getEmail());
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = null;
        if (!optionalUser.isPresent()) {
            throw new Exception("email not found");
        }
        user = optionalUser.get();
        if (encoder.matches(updatePasswordDto.getOldPassword(), user.getPassword())) {
            if (isStrongPassword(updatePasswordDto.getNewPassword())&&(updatePasswordDto.getNewPassword().equals(updatePasswordDto.getConfirmPassword()))) {
                if (!updatePasswordDto.getOldPassword().equals(updatePasswordDto.getNewPassword())) {
                    user.setPassword(encoder.encode(updatePasswordDto.getNewPassword()));
                    userRepository.save(user);
                } else {
                    throw new Exception(" old Password and new Password are same so please change the password");
                }
            } else {
                throw new Exception("Password should be strong or contains special characters");
            }
        } else {
            throw new Exception("please enter your correct old password");
        }
    }

@Override
public User userSignUp(SignUpDto signupDTO) throws Exception {

    if (!isStrongPassword(signupDTO.getPassword())) {
        throw new Exception("Password is not strong , at least one uppercase letter, one lowercase letter, one digit, and one special character needed");
    }
    Optional<User> optionalUser = userRepository.findUserByEmail(signupDTO.getEmail());
    if (optionalUser.isPresent())
        throw new Exception("User Already Exist with Email : " + signupDTO.getEmail());
    User user = new User();
    user.setDateOfBirth(signupDTO.getDob());
    user.setFirstName(signupDTO.getFirstName());
    user.setIsActive(true);
    user.setLastName(signupDTO.getLastName());
    user.setEmail(signupDTO.getEmail());
    user.setPhoneNumber(signupDTO.getPhoneNumber());
    PasswordEncoder encoder = new BCryptPasswordEncoder(8);
    // Set new password
    user.setPassword(encoder.encode(signupDTO.getPassword()));

    userRepository.save(user);
    return user;
}
    @Override
    public Optional <User> findById(Integer id) {
        return userRepository.findById(id);
    }
    @Override
    public void delete(Integer id) {userRepository.deleteById(id);}

    @Override
    public void updateResetPasswordToken(String token, String email) throws Exception{
        User optionalUser = userRepository.findByEmail(email);

        if (optionalUser==null) {
            throw new Exception("Could not find any user with the email " + email);

        } else {
            optionalUser.setResetToken(token);
            userRepository.save(optionalUser);
        }
    }

    @Override
    public Optional<User> getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

//    @Override
//    public void updatePassword(User user, String newPassword) throws Exception {
//
//    }

    @Override
    public void updatePassword(ResetUpdatepassword resetUpdatepassword) throws Exception {
        Optional<User> optionalUser = userRepository.findByResetPasswordToken(resetUpdatepassword.getToken());
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = null;
        if (!optionalUser.isPresent()) {
            throw new Exception("email not found");
        }
        user = optionalUser.get();
        if (!isStrongPassword(resetUpdatepassword.getNewPassword())){
            throw new Exception("password should be strong");}
        if (!encoder.matches(resetUpdatepassword.getNewPassword(), user.getPassword())) {
            if (resetUpdatepassword.getNewPassword().equals(resetUpdatepassword.getConfirmPassword())) {
                    user.setPassword(encoder.encode(resetUpdatepassword.getNewPassword()));
                    userRepository.save(user);
                } else {
                    throw new Exception("NewPassword and confirmPassword does not same");
                }

        } else {
            throw new Exception("old password cannot be your new password");
        }
    }






    @Override
    public List<User> saveUsers(List<User> user) {return (List<User>) userRepository.saveAll(user);}
    @Override
    public List<User> findAll() {return userRepository.findAll();}

    @Override
    public void updateUser(User user) throws Exception {
        Optional<User> optionalUser=userRepository.findById(user.getId());
        if(optionalUser.isPresent()){
            userRepository.save(user);
        }
        else {
            throw new Exception("User Id Not present");
        }
    }


















//userRole Reference

    public void saveOrUpdateUserRoleXref(UserRoleRefDto userRoleRefDto) throws Exception {
        Optional<UserRoleRef> OptionaluserRoleRef = userRoleRefRepository.findById(userRoleRefDto.getId());
        UserRoleRef userRoleRef = null;
        if (OptionaluserRoleRef.isPresent()) {
            userRoleRef = OptionaluserRoleRef.get();
        } else {
            userRoleRef = new UserRoleRef();
        }
        Optional<User> optionalUser = userRepository.findById(userRoleRefDto.getUserId());
        if (optionalUser.isPresent()) {
            userRoleRef.setUser(optionalUser.get());
        } else {
            throw new Exception("UserId not present");
       }

        Optional<Role> optionalRole=roleRepository.findById(userRoleRefDto.getRoleId());
        if( optionalRole.isPresent()){
            userRoleRef.setRole(optionalRole.get());
        }
        else {
            throw new Exception("Role Id  not present");
        }
        userRoleRef.setIsActive(userRoleRefDto.getIsActive());
        userRoleRefRepository.save(userRoleRef);
    }

    @Override
    public void deleteUserRoleById(Integer id) throws Exception {
        userRoleRefRepository.deleteById(id);
    }

    @Override
    public Optional<UserRoleRef> fetchUserRoleRefId(Integer id) {
        return userRoleRefRepository.findById(id);
    }

    @Override
    public List<UserRoleRef> fetchAllUserRoleRef() {
        return userRoleRefRepository.findAll();
    }

    @Override
    public void updateUserRoleRef(UserRoleRefDto userRoleRefDto) throws Exception {
        Optional<UserRoleRef> OptionaluserRoleRef = userRoleRefRepository.findById(userRoleRefDto.getId());
        UserRoleRef userRoleRef = null;
        if (OptionaluserRoleRef.isPresent()) {
            userRoleRef = OptionaluserRoleRef.get();
        } else {

            throw new Exception(" Id not Found");
        }
        Optional<User> optionalUser = userRepository.findById(userRoleRefDto.getUserId());
        if (optionalUser.isPresent()) {
            userRoleRef.setUser(optionalUser.get());
        } else {
            throw new Exception("UserId not present");
        }

        Optional<Role> optionalRole=roleRepository.findById(userRoleRefDto.getRoleId());
        if( optionalRole.isPresent()){
            userRoleRef.setRole(optionalRole.get());
        }
        else {
            throw new Exception("Role Id  not present");
        }
        userRoleRef.setIsActive(userRoleRefDto.getIsActive());
        userRoleRefRepository.save(userRoleRef);
    }














    //models
    public Model saveModel(Model model) {return modelRepository.save(model);}
    @Override
    public List<Model> saveModels(List<Model> model) {return (List<Model>) modelRepository.saveAll(model);}
    @Override
    public Optional<Model> findModelById(Integer id) {return modelRepository.findById(id);
    }
    @Override
    public List<Model> findAllModel() {return modelRepository.findAll();}
    @Override
    public void deleteModel(Integer id) {modelRepository.deleteById(id);}

    @Override
    public void updateModel(Model model) throws Exception {
        Optional<Model> optionalModel=modelRepository.findById(model.getId());
        if(optionalModel.isPresent()){
            modelRepository.save(model);
        }
        else {
            throw new Exception("model Id Not present");
        }
    }










//permission
    public ResponseEntity<Object> addOrUpdatePermission(PermissionDto permissionDto) throws Exception {
        Optional<Permission> optionalPermission=permissionRepository.findById(permissionDto.getId());
        Permission permission=null;
        if(optionalPermission.isPresent()){
            permission=optionalPermission.get();
        }
        else{
            permission=new Permission();
        }
        permission.setIsActive(permissionDto.getIsActive());
        permission.setIsView(permissionDto.getIsView());
        permission.setIsAdd(permissionDto.getIsAdd());
        permission.setIsUpdate(permissionDto.getIsUpdate());
        permission.setIsDelete(permissionDto.getIsDelete());
        Optional<Model> optionalModel=modelRepository.findById(permissionDto.getModelId());
        if(optionalModel.isPresent()){
            permission.setModel(optionalModel.get());
        }
        else{
            throw new Exception("Module Id Not found");
        }
        Optional<Role> optionalRole=roleRepository.findById(permissionDto.getRoleId());
        if(optionalRole.isPresent()){
            permission.setRole(optionalRole.get());
        }
        else{
            throw new Exception("Role Id Not found");
        }
        permissionRepository.save(permission);
        return ResponseEntity.ok("data Saved successfully");
    }



    @Override
    public Optional<Permission> fetchByPermissionId(Integer id) {
        return permissionRepository.findById(id);
    }

    @Override
    public List<Permission> fetchAllPermission() {
        return permissionRepository.findAll();
    }

    @Override
    public void deletePermissionById(Integer id) throws Exception {
        permissionRepository.deleteById(id);
    }

    @Override
    public void updatePermission(PermissionDto permissionDto) throws Exception {
        Optional<Permission> optionalPermission=permissionRepository.findById(permissionDto.getId());
        Permission permission=null;
        if(optionalPermission.isPresent()){
            permission=optionalPermission.get();
        }
        else{
//            permission=new Permission();
            throw new Exception(" Id Not found");

        }
        permission.setIsActive(permissionDto.getIsActive());
        permission.setIsView(permissionDto.getIsView());
        permission.setIsAdd(permissionDto.getIsAdd());
        permission.setIsUpdate(permissionDto.getIsUpdate());
        permission.setIsDelete(permissionDto.getIsDelete());
        Optional<Model> optionalModel=modelRepository.findById(permissionDto.getModelId());
        if(optionalModel.isPresent()){
            permission.setModel(optionalModel.get());
        }
        else{
            throw new Exception("Module Id Not found");
        }
        Optional<Role> optionalRole=roleRepository.findById(permissionDto.getRoleId());
        if(optionalRole.isPresent()){
            permission.setRole(optionalRole.get());
        }
        else{
            throw new Exception("Role Id Not found");
        }
        permissionRepository.save(permission);
//        return ResponseEntity.ok("data Saved successfully");
    }

//    public void updatePassword(User user, String newPassword) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode(newPassword);
//        user.setPassword(encodedPassword);
//
//        user.setResetPasswordToken(null);
//        userRepository.save(user);
//    }


    //role
    public Role saveRole(Role role) {return roleRepository.save(role);}
    @Override
    public Optional<Role> findRoleById(Integer id) {return roleRepository.findById(id);}
    @Override
    public List<Role> findAllRole() {return  roleRepository.findAll();}
    @Override
    public void deleteRole(Integer id) {roleRepository.deleteById(id);}

    @Override
    public List<Role> saveRoles(List<Role> role) {
        return (List<Role>) roleRepository.saveAll(role);}

    public void updateRole(Role role) throws Exception {
        Optional<Role> optionalRole=roleRepository.findById(role.getId());
        if(optionalRole.isPresent()){
            roleRepository.save(role);
        }
        else {
            throw new Exception("Role Id Not present");
        }
    }


    private boolean isStrongPassword(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        // check for at least one uppercase letter, one lowercase letter, one digit, and one special character
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (isSpecialChar(ch)) {
                hasSpecialChar = true;
            }
        }

        // check if password meets all criteria
        return password.length() >= 8 && hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    private boolean isSpecialChar(char ch) {
        String specialChars = "!@#$%^&*()_-+=[{]};:<>|./?";
        return specialChars.contains(Character.toString(ch));
    }


}





























