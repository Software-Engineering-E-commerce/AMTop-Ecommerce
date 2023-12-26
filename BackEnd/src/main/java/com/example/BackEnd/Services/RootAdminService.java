package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.Model.Admin;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RootAdminService {
    private final JwtService jwtService;
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final EmailService emailService;

    public void addEmployee(String token, String newAdminEmail) throws Exception {
        String email = jwtService.extractUsername(token);
        verifyRootAdmin(email);
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(newAdminEmail);
        if(optionalCustomer.isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This email already used by a customer!");

        Optional<Admin> optionalAdmin = adminRepository.findByEmail(newAdminEmail);
        if(optionalAdmin.isPresent()){
            handleFoundAdmin(optionalAdmin.get());
        }else{
            handleNewAdmin(newAdminEmail);
        }
    }

    void verifyRootAdmin(String email) {
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);
        if(optionalAdmin.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin Not Found");
        if(optionalAdmin.get().getId() != 1)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only root admin is allowed to add new admin");
    }

    void handleFoundAdmin(Admin admin) throws Exception {
        if (!admin.getIsVerified())
            newAdminVerification(admin);
        else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This email already used by a verified admin!");
        }
    }
    void handleNewAdmin(String newAdminEmail) throws Exception {
        Admin admin = new Admin();
        admin.setEmail(newAdminEmail);
        admin.setIsGmail(false);
        admin.setIsVerified(false);
        adminRepository.save(admin);
        newAdminVerification(admin);
    }

    void newAdminVerification(Admin admin) throws Exception {
        var jwtToken = jwtService.generateToken(admin);
        try {
            String verificationLink = "http://localhost:3000/verificationSignup?token=" + jwtToken + "&email=" + admin.getEmail();
            emailService.sendEmail(admin.getEmail(), "Email Verification",
                    "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; padding: 20px;\">\n" +
                            "\n" +
                            "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">\n" +
                            "\n" +
                            "        <h2 style=\"color: #333333;\">Email Verification</h2>\n" +
                            "\n" +
                            "        <p style=\"color: #666666;\">Please click on the button below to complete register and verify your account:</p>\n" +
                            "\n" +
                            "        <a href=\"" + verificationLink + "\"style=\"display: inline-block; background-color: #4caf50; color: #ffffff; padding: 10px 20px; text-decoration: none; border-radius: 5px; margin-top: 20px;\">Verify</a>\n" +
                            "\n" +
                            "    </div>\n" +
                            "</body>");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
