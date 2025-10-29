package com.proyecto.work.controller;

import com.proyecto.work.model.Task;
import com.proyecto.work.model.User;
import com.proyecto.work.model.Widget;
import com.proyecto.work.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private WidgetService widgetService;

    // Página de registro
    @GetMapping("/register")
    public String registerPage(Model model) {
        // Si ya está autenticado, redirigir al dashboard
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return "redirect:/dashboard";
        }
        
        model.addAttribute("user", new User());
        return "register";
    }

    // Procesar registro
    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("user") User user, 
                                     BindingResult result,
                                     @RequestParam String confirmPassword,
                                     Model model) {
        
        // Validaciones
        if (userService.emailExists(user.getEmail())) {
            result.rejectValue("email", "error.user", "El correo ya está registrado");
        }
        
        if (user.getFirstName().length() < 4) {
            result.rejectValue("firstName", "error.user", "El nombre debe tener al menos 4 caracteres");
        }
        
        if (user.getLastName().length() < 4) {
            result.rejectValue("lastName", "error.user", "El apellido debe tener al menos 4 caracteres");
        }
        
        if (user.getPassword().length() < 5) {
            result.rejectValue("password", "error.user", "La contraseña debe tener al menos 5 caracteres");
        }
        
        if (!user.getPassword().equals(confirmPassword)) {
            result.rejectValue("password", "error.user", "Las contraseñas no coinciden");
        }
        
        if (result.hasErrors()) {
            return "register";
        }
        
        // Registrar usuario
        User savedUser = userService.registerUser(user);
        
        // Crear widgets por defecto
        widgetService.createDefaultWidget(savedUser, "progress");
        widgetService.createDefaultWidget(savedUser, "pomodoro");
        
        // Redirigir al login para que el usuario inicie sesión
        return "redirect:/login?success=true";
    }

    // Dashboard principal
    @GetMapping("/dashboard")
    public String dashboardPage(@RequestParam(defaultValue = "estudios") String category, 
                               Model model) {
        // Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        model.addAttribute("user", user);
        model.addAttribute("category", category);
        
        List<Task> tasks = taskService.getUserTasksByCategory(user, category);
        List<Widget> widgets = widgetService.getUserWidgets(user);
        
        model.addAttribute("tasks", tasks);
        model.addAttribute("widgets", widgets);
        model.addAttribute("progress", taskService.calculateProgress(user, category));
        
        return "dashboard";
    }

    // Cambiar categoría
    @PostMapping("/dashboard/category")
    public String changeCategory(@RequestParam String category) {
        return "redirect:/dashboard?category=" + category;
    }

    // Agregar tarea
    @PostMapping("/task/add")
    public String addTask(@RequestParam String title,
                         @RequestParam String description,
                         @RequestParam String category) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Task task = new Task(title, description, category, user);
        taskService.saveTask(task);
        
        return "redirect:/dashboard?category=" + category;
    }

    // Toggle completar tarea
    @PostMapping("/task/toggle/{id}")
    public String toggleTask(@PathVariable Long id, @RequestParam String category) {
        taskService.toggleTaskCompletion(id);
        return "redirect:/dashboard?category=" + category;
    }

    // Eliminar tarea
    @PostMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable Long id, @RequestParam String category) {
        taskService.deleteTask(id);
        return "redirect:/dashboard?category=" + category;
    }
}