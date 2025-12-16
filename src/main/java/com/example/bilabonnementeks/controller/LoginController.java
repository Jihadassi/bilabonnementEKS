package com.example.bilabonnementeks.controller;

import com.example.bilabonnementeks.model.User;
import com.example.bilabonnementeks.service.LoginService;
import jakarta.servlet.http.HttpSession; // bruges til at arbejde med info lagring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional; // håndtere værdier der måske ikke findes



@Controller
//håndterer HTTP-requests.
public class LoginController {

    // værktøj til at sende SQL-queries til databasen.
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LoginController(JdbcTemplate jdbcTemplate, LoginService loginService) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // Navigation til LogIn siden
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // returnerer src/main/resources/templates/login.html (Thymeleaf)
    }

    /**
     * POST /login - behandler loginform
     * - Validerer brugernavn + kodeord mod USER-tabellen
     * - Lægger username og role i session
     * - Redirecter til relevant menu afhængig af role:
     *
     * Her bruges et parameteriseret query for at adskille SQL-kommandoen fra brugerens input,
     * så databasen ikke lader brugeren køre sin egen SQL-kode.
     */

    //Håndtere og validere login og rolle og videresende brugeren til den korrekte menu
    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {

        try {
            String sql = "SELECT user_role FROM users WHERE username = ? AND user_password = ? LIMIT 1";

            Optional<String> roleOpt = jdbcTemplate.query(sql, new Object[]{username, password}, rs -> {
                if (rs.next()) {
                    return Optional.ofNullable(rs.getString("user_role"));
                } else {
                    return Optional.empty();
                }
            });

            if (roleOpt.isPresent()) {
                String role = roleOpt.get();


                // Lægger brugernavn og rolle i sessionen, så resten af systemet kan se hvem der er logget ind.
                session.setAttribute("username", username);
                session.setAttribute("userRole", role);

                // Bestem redirect ud fra role (gør case-insensitive match)
                String roleLower = role.trim().toLowerCase();

                if (roleLower.equals("dataregistrator") || roleLower.equals("dataregistrator".toLowerCase())) {
                    return "redirect:/dr/menu";
                } else if (roleLower.equals("skadebehandler") || roleLower.equals("skadebehandler".toLowerCase())) {
                    return "redirect:/sb/reports";
                } else if (roleLower.equals("forretningsudvikler") || roleLower.equals("forretningsudvikler".toLowerCase())) {
                    return "redirect:/fu/menu";
                } else {
                    // Ukendt rolle — log ud og vis fejl (sikker fallback)
                    session.invalidate();
                    redirectAttributes.addAttribute("error", "true");
                    return "redirect:/login";
                }
            } else {
                // Forkert login
                redirectAttributes.addAttribute("error", "true");
                return "redirect:/login";
            }
        } catch (Exception e) {
            // Ved databasefejl — log evt. (her printer vi kun til stdout)
            System.err.println("Login error: " + e.getMessage());
            redirectAttributes.addAttribute("error", "true");
            return "redirect:/login";
        }
    }


    //stopper sessionen for den nuværende bruger
    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        session.invalidate();
        return "redirect:/login?logout";
    }
}