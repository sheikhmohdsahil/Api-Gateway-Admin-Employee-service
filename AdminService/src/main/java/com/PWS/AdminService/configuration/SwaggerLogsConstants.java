package com.PWS.AdminService.configuration;

public class SwaggerLogsConstants {
   public static final String USER_SAVED_200_SUCCESSFULL ="{\n" +
           "    \"id\": 10,\n" +
           "    \"firstName\": \"abc123\",\n" +
           "    \"lastName\": \"abc123\",\n" +
           "    \"dateOfBirth\": \"2004-04-06T00:00:00.000+00:00\",\n" +
           "    \"email\": \"abc123@gmail.com\",\n" +
           "    \"phoneNumber\": 99887799,\n" +
           "    \"password\": \"$2a$08$A/0rFuq79d0f//9kvOoJWuhQHrstvbHBCL4L/kUoOl1Mn7pG8ct16\",\n" +
           "    \"resetToken\": null,\n" +
           "    \"isActive\": true,\n" +
           "    \"username\": \"abc123@gmail.com\",\n" +
           "    \"authorities\": null\n" +
           "}";
    public static final String USER_SAVE_500_FAILURE="{\n" +
            "    \"timestamp\": \"2023-03-14T17:37:51.196+00:00\",\n" +
            "    \"status\": 500,\n" +
            "    \"error\": \"Internal Server Error\",\n" +
            "    \"path\": \"/public/user\"\n" +
            "}";


    public static final String  USER_SAVE_400_FAILURE="{\n" +
            "    \"timestamp\": \"2023-03-15T04:42:37.796+00:00\",\n" +
            "    \"status\": 400,\n" +
            "    \"error\": \"Bad Request\",\n" +
            "    \"path\": \"/public/user\"\n" +
            "}";


    public static final String USERS_SAVED_200_SUCCESSFULL="[\n" +
            "    {\n" +
            "        \"id\": 2,\n" +
            "        \"firstName\": \"mohd\",\n" +
            "        \"lastName\": \"sahi\",\n" +
            "        \"dateOfBirth\": null,\n" +
            "        \"email\": \"sheikhmohdsahil456@gmail.com\",\n" +
            "        \"phoneNumber\": 99887799,\n" +
            "        \"password\": \"S@hil06052000\",\n" +
            "        \"resetToken\": null,\n" +
            "        \"isActive\": true,\n" +
            "        \"authorities\": null,\n" +
            "        \"username\": \"sheikhmohdsahil456@gmail.com\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 3,\n" +
            "        \"firstName\": \"mohd\",\n" +
            "        \"lastName\": \"sahil\",\n" +
            "        \"dateOfBirth\": null,\n" +
            "        \"email\": \"sheikhmohdsahil@gmail.com\",\n" +
            "        \"phoneNumber\": 99887799,\n" +
            "        \"password\": \"S@hil0605200\",\n" +
            "        \"resetToken\": null,\n" +
            "        \"isActive\": true,\n" +
            "        \"authorities\": null,\n" +
            "        \"username\": \"sheikhmohdsahil@gmail.com\"\n" +
            "    }\n" +
            "]";

 public static final String USER_FETCHED_WITH_ID_200_SUCCESSFULL ="{\n" +
         "    \"id\": 4,\n" +
         "    \"firstName\": \"mohd\",\n" +
         "    \"lastName\": \"sahi\",\n" +
         "    \"dateOfBirth\": null,\n" +
         "    \"email\": \"sheikhmohdsahil456@gmail.com\",\n" +
         "    \"phoneNumber\": 99887799,\n" +
         "    \"password\": \"S@hil06052000\",\n" +
         "    \"resetToken\": null,\n" +
         "    \"isActive\": true,\n" +
         "    \"authorities\": null,\n" +
         "    \"username\": \"sheikhmohdsahil456@gmail.com\"\n" +
         "}" ;
 public static final String FETCHING_USERDETAIL_WITH_ID_400_FAILURE = "";
 public static final String FETCHING_USERDETAIL_WITH_ID_500_FAILURE = "{\n" +
         "    \"timestamp\": \"2023-03-21T07:50:43.955+00:00\",\n" +
         "    \"status\": 500,\n" +
         "    \"error\": \"Internal Server Error\",\n" +
         "    \"path\": \"/user/4\"\n" +
         "}";
 public static final String PERMISSION_UPDATED_200_SUCCESSFULL ="Data Saved " ;
 public static final String PERMISSION_UPDATED_400_FAILURE = "";


 public static final String PERMISSION_DELETED_200_SUCCESSFULL = "PermissionId 1is deleted";
 public static final String PERMISSION_DELETED_400_FAILURE ="" ;

 public static final String PERMISSION_FETCHED_WITH_ID_200_SUCCESSFULL = "{\n" +
         "    \"permId\": 4,\n" +
         "    \"isActive\": true,\n" +
         "    \"isView\": true,\n" +
         "    \"isAdd\": true,\n" +
         "    \"isUpdate\": true,\n" +
         "    \"isDelete\": true,\n" +
         "    \"model\": {\n" +
         "        \"id\": 1,\n" +
         "        \"name\": \"employee\",\n" +
         "        \"isActive\": true\n" +
         "    },\n" +
         "    \"role\": {\n" +
         "        \"id\": 1,\n" +
         "        \"name\": \"coFounder\",\n" +
         "        \"isActive\": true\n" +
         "    }\n" +
         "}";

 public static final String FETCHING_PERMISSION_ID_400_FAILURE = "";
 public static final String CREATED_PERMISSION_200_SUCCESSFULL = "Data Saved ";
 public static final String PERMISSION_CREATING_500_FAILURE = "{\n" +
         "    \"timestamp\": \"2023-03-21T07:58:07.500+00:00\",\n" +
         "    \"status\": 500,\n" +
         "    \"error\": \"Internal Server Error\",\n" +
         "    \"path\": \"/permission\"\n" +
         "}";
 public static final String PERMISSION_CREATING_400_FAILURE ="" ;
 //public static final String MODEL_UPDATED_500_FAILURE = "";
 public static final String MODEL_UPDATED_200_SUCCESSFULL = "";
 public static final String MODEL_UPDATED_400_FAILURE ="" ;
 public static final String MODEL_DELETED_200_SUCCESSFULL = "";
// public static final String MODEL_DELETED_500_FAILURE ="" ;
 public static final String MODEL_DELETED_400_FAILURE = "";
 public static final String MODEL_FETCHED_WITH_ID_200_SUCCESSFULL = "{\n" +
         "    \"id\": 1,\n" +
         "    \"name\": \"employee\",\n" +
         "    \"isActive\": true\n" +
         "}";

 public static final String FETCHING_MODEL_WITH_ID_400_FAILURE ="" ;
 public static final String CREATED_MODEL_200_SUCCESSFULL = "{\n" +
         "    \"id\": 2,\n" +
         "    \"name\": \"admin\",\n" +
         "    \"isActive\": true\n" +
         "}";

 public static final String MODEL_CREATING_400_FAILURE = "";





// public static final String USER_ROLE_REF_UPDATED_500_FAILURE ="" ;

 public static final String USER_ROLE_REF_DELETED_200_SUCCESSFULL = "";
 public static final String USER_ROLE_REF_DELETED_400_FAILURE ="" ;



 public static final String USER_ROLE_REF_UPDATED_400_FAILURE = "";
 public static final String USER_ROLE_REF_UPDATED_200_SUCCESSFULL = "";


 public static final String USER_ROLE_REF_FETCHED_WITH_ID_200_SUCCESSFULL = "";
 public static final String FETCHING_USER_ROLE_REF_ID_400_FAILURE = "";


 public static final String USER_ROLE_REF_200_SUCCESSFULL ="" ;
 public static final String USER_ROLE_REF_400_FAILURE = "";


 public static final String ROLE_UPDATED_200_SUCCESSFULL = "";
 public static final String ROLE_UPDATED_400_FAILURE ="" ;


 public static final String ROLE_DELETED_200_SUCCESSFULL = "";
 public static final String ROLE_DELETED_400_FAILURE ="" ;


 public static final String ROLE_FETCHED_WITH_ID_200_SUCCESSFULL ="{\n" +
         "    \"id\": 1,\n" +
         "    \"name\": \"coFounder\",\n" +
         "    \"isActive\": true\n" +
         "}" ;
 public static final String FETCHING_ROLE_WITH_ID_400_FAILURE ="" ;


 public static final String CREATED_ROLE_200_SUCCESSFULL ="{\n" +
         "    \"id\": 2,\n" +
         "    \"name\": \"tester\",\n" +
         "    \"isActive\": true\n" +
         "}" ;

 public static final String ROLE_CREATING_400_FAILURE = "{\n" +
         "    \"timestamp\": \"2023-03-21T08:19:24.213+00:00\",\n" +
         "    \"status\": 400,\n" +
         "    \"error\": \"Bad Request\",\n" +
         "    \"path\": \"/public/roles\"\n" +
         "}";



 public static final String USER_DELETED_200_SUCCESSFULL = "user with '5' has been deleted " ;
 public static final String DELETE_USER_400_FAILURE = "";
 public static final String DELETE_USER_500_FAILURE = "    \"timestamp\": \"2023-03-21T07:43:33.325+00:00\",\n" +
         "    \"status\": 500,\n" +
         "    \"error\": \"Internal Server Error\",\n" +
         "    \"path\": \"/deleteUser/5\"\n" +
         "}";

}