package com.PWS.AdminService.controller;

import com.PWS.AdminService.Utility.ApiSuccess;
import com.PWS.AdminService.Utility.CommonUtils;
import com.PWS.AdminService.configuration.SwaggerLogsConstants;
import com.PWS.AdminService.dto.*;
import com.PWS.AdminService.entity.*;
import com.PWS.AdminService.jwtconfig.JwtUtil;
import com.PWS.AdminService.sevice.AdminService;
import io.jsonwebtoken.impl.DefaultClaims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {
    Logger log = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private AdminService adminService;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Operation(summary = "Authenticating the User")
    @PostMapping("/public/authenticate")
    public String generateToken(@RequestBody LoginDto loginDto) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        return new String("token:" + jwtUtil.generateToken(loginDto.getEmail()));
    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
//        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
//        jwtUtil.addTokenToBlacklist(token);
//        return ResponseEntity.ok().build();
//    }




    @RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
    public ResponseEntity<?> refreshtoken(HttpServletRequest httpServletRequest) throws Exception {
        // From the HttpRequest get the claims
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) httpServletRequest.getAttribute("claims");

        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
      //  String token = jwtUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
        return ResponseEntity.ok( jwtUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString()));
    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;

    }

    @DeleteMapping("/private/logout/token")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            if(jwtUtil.isTokenBlacklisted(jwt))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalidated.");
            String userDetails = jwtUtil.getUsernameFromToken(jwt);
            // Invalidate the token
            jwtUtil.invalidateToken(jwt);
            // Clear user details from session
            request.getSession().removeAttribute("userDetails");
            return ResponseEntity.ok("Successfully logged out.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }
    }



//    @PostMapping
//   public ResponseEntity createUser (@RequestBody UserDto userDto) {
//        jwtUserDetailsService.loadUserByUsername(userDto);
//        return ResponseEntity.ok().build();
//   }


    //user

    @ApiResponse(description = "creating a user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_SAVED_200_SUCCESSFULL)})}),@ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_SAVE_400_FAILURE)})})})
    @Operation(summary = "Creating a new User")
    @PostMapping("/public/user")
    public User userSignUp(@RequestBody SignUpDto signupDTO) throws Exception{
        return adminService.userSignUp(signupDTO);

    }


    @Operation(summary = "Updating User Password")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User's Password Updated  Successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_SAVED_200_SUCCESSFULL)})}),@ApiResponse(responseCode = "400", description = "Invalid password", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_SAVE_400_FAILURE)})})})
    @PostMapping("/private/user/password")
    public ResponseEntity<Object> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto)throws  Exception{
         adminService.userUpdatePassword(updatePasswordDto);
         return ResponseEntity.ok("password updated successfully");
    }
//    @ApiResponse(description = "creating a user")
//    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USERS_SAVED_200_SUCCESSFULL)})}), @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USERS_SAVE_400_FAILURE)})}), @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
//    @Operation(summary = "Saving all Users")
//    @PostMapping("/saveUsers")
//    public List<User> saveAllUser(@RequestBody List<User> user) {return adminService.saveUsers(user);}

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "user deleted", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_DELETED_200_SUCCESSFULL)})}), @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.DELETE_USER_400_FAILURE)})}), })
    @Operation(summary = "Deleting User by its id")
    @DeleteMapping("/private/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Integer id) {
        adminService.delete(id);
        return new ResponseEntity<String>("user with '" + id + "' has been deleted ", HttpStatus.OK);
    }
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "user fetched with id successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_FETCHED_WITH_ID_200_SUCCESSFULL)})}), @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.FETCHING_USERDETAIL_WITH_ID_400_FAILURE)})})})
    @Operation(summary = "Get a user by its id")
    @GetMapping("/private/user/{id}")
    public Optional<User> getById(@PathVariable("id") Integer id) {return adminService.findById(id);}


    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = " fetched all users  successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_FETCHED_WITH_ID_200_SUCCESSFULL)})}), @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.FETCHING_USERDETAIL_WITH_ID_400_FAILURE)})})})
    @Operation(summary = "Get all Users")
    @GetMapping("/private/alluser")
    public List<User> getAll() {return adminService.findAll();}


//    @PutMapping("/update/user")
//    public ResponseEntity<Object> updateUser(@RequestBody User user) throws Exception {
//        adminService.updateUser(user);
//        return ResponseEntity.ok("Data Saved successfully");
//    }
















    //role
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "created role successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.CREATED_ROLE_200_SUCCESSFULL)})}), @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.ROLE_CREATING_400_FAILURE)})})})
    @Operation(summary = "Creating a role")
    @PostMapping("/private/roles")
    public Role addRole(@RequestBody Role role) {return adminService.saveRole(role);}

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Role fetched with id successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.ROLE_FETCHED_WITH_ID_200_SUCCESSFULL)})}), @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.FETCHING_ROLE_WITH_ID_400_FAILURE)})})})
    @Operation(summary = "fetching a role by its id")
    @GetMapping("/private/role/{id}")
    public Optional<Role> getRoleById(@PathVariable("id") Integer id) {return adminService.findRoleById(id);}



    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "fetched all roles successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.ROLE_FETCHED_WITH_ID_200_SUCCESSFULL)})}), @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.FETCHING_ROLE_WITH_ID_400_FAILURE)})})})
    @Operation(summary = "Fetching all the roles")
    @GetMapping("/private/allRole")
    public List<Role> getAllRole() {return adminService.findAllRole();}

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "role deleted", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.ROLE_DELETED_200_SUCCESSFULL)})}), @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.ROLE_DELETED_400_FAILURE)})})})
    @Operation(summary = "Delete role by its id")
    @DeleteMapping("/private/deleteRole/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable("id") Integer id) {
        adminService.deleteRole(id);
        return new ResponseEntity<String>("user with '" + id + "' has been deleted ", HttpStatus.OK);
    }


    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "roles saved successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.ROLE_DELETED_200_SUCCESSFULL)})}), @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.ROLE_DELETED_400_FAILURE)})})})
    @Operation(summary = "Saving the Roles")
    @PostMapping("/private/saveAllRoles")
    public List<Role> saveRoles(@RequestBody List<Role> role) {return adminService.saveRoles(role);}

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Updated role successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.ROLE_UPDATED_200_SUCCESSFULL)})}),  @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.ROLE_UPDATED_400_FAILURE)})})})
    @Operation(summary = "Update the role")
    @PutMapping("/private/update/role")
    public ResponseEntity<Object> updateRole(@RequestBody Role role) throws Exception {
        adminService.updateRole(role);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK,("updated the role successfully")));
    }





    //user Role Ref
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User Role refernce successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_ROLE_REF_200_SUCCESSFULL)})}), @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_ROLE_REF_400_FAILURE)})})})
    @Operation(summary = "Saving User Role refernce")
    @PostMapping("/private/userXref")
    public String saveOrUpdateUserRoleXref(@RequestBody UserRoleRefDto userRoleRefDto) throws Exception {
        adminService.saveOrUpdateUserRoleXref(userRoleRefDto);
        return "UserRoleXref Data Saved Successfully";
    }
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User Role Ref fetched with id successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_ROLE_REF_FETCHED_WITH_ID_200_SUCCESSFULL)})}),  @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.FETCHING_USER_ROLE_REF_ID_400_FAILURE)})})})
    @Operation(summary = "fetching UserRoleRef by its id")
    @GetMapping("/private/fetchUserRoleRef/{id}")
    public Optional<UserRoleRef> fetchUserRoleRefById(@PathVariable("id") Integer id) {
        return adminService.fetchUserRoleRefId(id);
    }



    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Users Roles Ref fetched  successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_ROLE_REF_FETCHED_WITH_ID_200_SUCCESSFULL)})}),  @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.FETCHING_USER_ROLE_REF_ID_400_FAILURE)})})})
    @Operation(summary = "fetch all the UserRoleRef")
    @GetMapping("/private/fetchAllUserRoleRef")
    public List<UserRoleRef> fetchAllUserRole() {return adminService.fetchAllUserRoleRef();}


    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User Role Ref deleted", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_ROLE_REF_DELETED_200_SUCCESSFULL)})}),  @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_ROLE_REF_DELETED_400_FAILURE)})})})
    @Operation(summary = "Delete UserRoleRef by its id")
    @DeleteMapping("/private/deleteUserRole/{id}")
    public String deleteUserRole(@PathVariable("id") Integer id) throws Exception {
        adminService.deleteUserRoleById(id);
        return "UserRoleRef id is deleted";
    }


    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User Role Ref Updated  successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_ROLE_REF_UPDATED_200_SUCCESSFULL)})}), @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_ROLE_REF_UPDATED_400_FAILURE)})})})
    @Operation(summary = "updating the UserRoleRef")
    @PutMapping("/private/update/userRoleRef")
    public String updateUserRoleXref(@RequestBody UserRoleRefDto userRoleRefDto) throws Exception {
        adminService.updateUserRoleRef(userRoleRefDto);
        return "UserRoleXref Data updated Successfully";
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "created Model successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.CREATED_MODEL_200_SUCCESSFULL)})}), @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.MODEL_CREATING_400_FAILURE)})})})
    @Operation(summary = "Creating a Model")
    @PostMapping("/private/model")
    public Model addModel(@RequestBody Model model) {return adminService.saveModel(model);}


    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Model fetched with id successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.MODEL_FETCHED_WITH_ID_200_SUCCESSFULL)})}), @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.FETCHING_MODEL_WITH_ID_400_FAILURE)})})})
    @Operation(summary = "Fetching model by its id")
    @GetMapping("/private/models/{id}")
    public Optional<Model> findModelById(@PathVariable("id") Integer id) {return adminService.findModelById(id);}


    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Models fetched successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.MODEL_FETCHED_WITH_ID_200_SUCCESSFULL)})}), @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.FETCHING_MODEL_WITH_ID_400_FAILURE)})})})
    @Operation(summary = "Fetching all the Models")
    @GetMapping("/private/allModel")
    public List<Model> findAllModel() {return adminService.findAllModel();}


    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Model deleted", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.MODEL_DELETED_200_SUCCESSFULL)})}),  @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.MODEL_DELETED_400_FAILURE)})})})
    @Operation(summary = "Deleting a Model by its id")
    @DeleteMapping("/private/deleteModel/{id}")
    public ResponseEntity<String> deleteModel(@PathVariable("id") Integer id) {
        adminService.deleteModel(id);
        return new ResponseEntity<String>("user with '" + id + "' has been deleted ", HttpStatus.OK);
    }



    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Models saved", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.MODEL_DELETED_200_SUCCESSFULL)})}),  @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.MODEL_DELETED_400_FAILURE)})})})
    @Operation(summary = "Saving all the Models")
    @PostMapping("/private/saveAllModels")
    public List<Model> saveModels(@RequestBody List<Model> model) {return adminService.saveModels(model);}


    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Updated Model successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.MODEL_UPDATED_200_SUCCESSFULL)})}),  @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.MODEL_UPDATED_400_FAILURE)})})})
    @Operation(summary = "Update the model")
    @PutMapping("/private/update/Model")
    public ResponseEntity<Object> updateModel(@RequestBody Model model) throws Exception {
        adminService.updateModel(model);
        return ResponseEntity.ok("Data Saved successfully");

    }


    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "created Permission successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.CREATED_PERMISSION_200_SUCCESSFULL)})}),  @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.PERMISSION_CREATING_400_FAILURE)})})})
    @Operation(summary = "Creating a Permission ")
    @PostMapping("/private/permission")
    public ResponseEntity<Object> addPermission(@RequestBody PermissionDto permissionDto) throws Exception {
        adminService.addOrUpdatePermission(permissionDto);
        return ResponseEntity.ok("Data Saved ");
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Permission fetched with id successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.PERMISSION_FETCHED_WITH_ID_200_SUCCESSFULL)})}),  @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.FETCHING_PERMISSION_ID_400_FAILURE)})})})
    @Operation(summary = "Fetching a Permission  by its id")
    @GetMapping("/private/fetchPermissionId/{id}")
    public Optional<Permission> fetchPermissionId(@PathVariable("id") Integer id) {return adminService.fetchByPermissionId(id);}

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "All Permission fetched  successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.PERMISSION_FETCHED_WITH_ID_200_SUCCESSFULL)})}),  @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.FETCHING_PERMISSION_ID_400_FAILURE)})})})
    @Operation(summary = "Fetching all the Permission")
    @GetMapping("/private/fetchAllPermission")
    public List<Permission> fetchAllPermission() {return adminService.fetchAllPermission();}


    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Permission deleted", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.PERMISSION_DELETED_200_SUCCESSFULL)})}), @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.PERMISSION_DELETED_400_FAILURE)})})})
    @Operation(summary = "Deleting a Permission by its id")
    @DeleteMapping("/private/deletePermission/{id}")
    public String deletePermissionById(@PathVariable("id") Integer id) throws Exception {
        adminService.deletePermissionById(id);
        return "PermissionId " + id + "is deleted";
    }


    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Updated permission successfully", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.PERMISSION_UPDATED_200_SUCCESSFULL)})}),  @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.PERMISSION_UPDATED_400_FAILURE)})})})
    @Operation(summary = "Updating the Permission")
    @PutMapping("/private/updatePermission")
    public ResponseEntity<Object> updatePermission(@RequestBody PermissionDto permissionDto) throws Exception {
        adminService.updatePermission(permissionDto);
        return ResponseEntity.ok("Data Saved ");
    }

}







