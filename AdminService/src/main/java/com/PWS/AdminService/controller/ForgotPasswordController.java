package com.PWS.AdminService.controller;

import com.PWS.AdminService.dto.ResetUpdatepassword;
import com.PWS.AdminService.entity.User;
import com.PWS.AdminService.jwtconfig.JwtUtil;
import com.PWS.AdminService.sevice.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/admin")
public class ForgotPasswordController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/public/forgot_password")
    public <email> String processForgotPassword(@RequestParam String email, Model model) throws Exception, MessagingException, UnsupportedEncodingException {

       String token= new DecimalFormat("000000").format(new Random().nextInt(999999));

     if(email==null) {
         return "error email not found";

     }else {
//            model.addAttribute("error", ex.getMessage());
//            System.out.println(ex);
         adminService.updateResetPasswordToken(token,email);
         String resetPasswordLink = token;
         sendEmail(email, resetPasswordLink);
         //  return "We have sent a reset password link to your email. Please check.";
         return String.valueOf(new ResponseEntity<>("We have sent a reset password link to your email. Please check.", HttpStatus.OK));
        }

    }


    public void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("contact@PWS.com", "PWS Support");
        String recipientEmail = email;
        helper.setTo(email);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + resetPasswordLink
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);

    }


    @GetMapping("/public/reset_password")
    public String showResetPasswordForm(@RequestParam String token, Model model) throws Exception {
        Optional<User> user = adminService.getByResetPasswordToken(token);
     //   model.addAttribute("token", token);

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "invalid Token";
        }

        return "reset_password_form";
    }

//    @ApiResponse(description = "Updating password")
//    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful updated", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_SAVED_200_SUCCESSFULL)})}), @ApiResponse(responseCode = "500", description = "Internal Server Exception", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_SAVE_500_FAILURE)})}), @ApiResponse(responseCode = "400", description = "Improper Syntax", content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.USER_SAVE_400_FAILURE)})}), @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
   @Operation(summary = "Updating password")
    @PostMapping("/public/reset_password")
    public String processResetPassword(@RequestBody ResetUpdatepassword resetUpdatepassword, Model model) throws Exception {
        Optional<User> user = adminService.getByResetPasswordToken(resetUpdatepassword.getToken());
       // model.addAttribute("title", "Reset your password");

        if (user == null) {
         //   model.addAttribute("message", "Invalid Token");
            return "Invalid token";

        } else {
            adminService.updatePassword(resetUpdatepassword);

        //    model.addAttribute("message", "You have successfully changed your password.");
            return "You have successfully changed your password";
        }

    }
}