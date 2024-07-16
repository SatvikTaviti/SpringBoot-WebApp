package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class AppController {

    @Autowired
    private UserRepository repo;


    @Autowired
    private JSONFileService jsonFileService; // Autowire JSONFileService

    @Autowired
    private PdfExportService pdfExportService;

    @Autowired
    private ExcelExportService excelExportService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/register")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());
        return "signup_form";
    }

    @PostMapping("/process-register")
    public String processRegistration(@ModelAttribute("user") User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        repo.save(user);
        return "register_success";
    }

    @GetMapping("/list_users")
    public String viewUsersList(Model model) {
        List<User> listUsers = repo.findAll();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam("email") String email, Model model) {
        List<User> listUsers = repo.findByEmailContaining(email);
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<Resource> exportToPdf() throws IOException {
        File pdfFile = pdfExportService.exportUsersToPdf();
        FileSystemResource resource = new FileSystemResource(pdfFile);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfFile.length())
                .body(resource);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<Resource> exportToExcel() throws IOException {
        File excelFile = excelExportService.exportUsersToExcel();
        FileSystemResource resource = new FileSystemResource(excelFile);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(excelFile.length())
                .body(resource);
    }

    @PostMapping("/send-email")
    public String sendEmail(@RequestParam("emails") List<String> emails, Model model) {
        for (String email : emails) {
            try {
                emailService.sendSimpleEmail(email, "Subject: Test Email", "This is a test email.");
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("message", "Failed to send emails");
                return "email_failure";
            }
        }
        model.addAttribute("message", "Emails sent successfully");
        return "email_success";
    }

    @PostMapping("/send-email-with-attachment")
    public String sendEmailWithAttachment(@RequestParam(name = "emails", required = false) List<String> selectedUsers,
                                          @RequestParam("attachment") MultipartFile attachment,
                                          Model model) {
        if (selectedUsers == null || selectedUsers.isEmpty()) {
            model.addAttribute("message", "Please select at least one user");
            return "email_failure";
        }

        if (!attachment.isEmpty()) {
            String attachmentPath = "/Users/satviktaviti/Desktop/Attachments/" + attachment.getOriginalFilename();
            try {
                attachment.transferTo(new File(attachmentPath));
                for (String userEmail : selectedUsers) {
                    // Send email with attachment to each selected user
                    emailService.sendEmailWithAttachment(userEmail, "Subject: Test Email with Attachment", "This is a test email with attachment.", attachmentPath);
                }
                model.addAttribute("message", "Emails sent successfully with attachment");
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("message", "Failed to save attachment or send email");
                return "email_failure";
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("message", "Failed to send email");
                return "email_failure";
            }
        } else {
            model.addAttribute("message", "Attachment is required");
            return "email_failure";
        }
        return "email_success";
    }

    

    // Create JSON file endpoint
    @PostMapping("/create-json")
    public String createJSONFile(@ModelAttribute("user") User user, Model model) {
        try {
            File outputFile = new File("user_" + user.getId() + ".json");
            jsonFileService.createJSONFile(user, outputFile);
            model.addAttribute("message", "JSON File created successfully");
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Failed to create JSON file");
        }
        return "json_creation_result";
    }
}