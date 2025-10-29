package com.proyecto.work.controller;

import com.proyecto.work.model.User;
import com.proyecto.work.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdditionalController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/colors")
    public String colorsPage(Model model) {
        // Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        model.addAttribute("user", user);
        return "colors";
    }

    @PostMapping("/colors/update")
    public String updateColorTheme(@RequestParam String colorTheme) {
        // Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        user.setColorTheme(colorTheme);
        userService.updateUser(user);
        
        return "redirect:/dashboard";
    }

    @GetMapping("/terms")
    public String termsPage(Model model) {
        // Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        model.addAttribute("user", user);
        return "terms";
    }
    
    @GetMapping("/profile")
    public String profilePage(Model model) {
        // Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        model.addAttribute("user", user);
        return "profile";
    }
    
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String email) {
        // Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = auth.getName();
        
        User user = userService.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Verificar si el nuevo email ya existe (y no es el mismo usuario)
        if (!currentEmail.equals(email) && userService.emailExists(email)) {
            return "redirect:/profile?error=email_exists";
        }
        
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        userService.updateUser(user);
        
        return "redirect:/dashboard";
    }
}