package productshop.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import productshop.domain.models.binding.RegisterUserBindingModel;
import productshop.services.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new RegisterUserBindingModel());
        return "user/register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute("user") RegisterUserBindingModel user, Errors errors) {
        boolean passwordsMatch = user.getPassword().equals(user.getConfirmPassword());
        if (errors.hasErrors() || !passwordsMatch) {
            if (!passwordsMatch) {
                errors.rejectValue("password", "400", "Passwords don't match.");
                errors.rejectValue("confirmPassword", "400", "Passwords don't match.");
            }
            return "user/register";
        }

        if (!userService.register(user)) {
            errors.rejectValue("username", "400", "Username is already in use.");
            return "user/register";
        }

        return "redirect:/users/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        model.addAttribute("user", userService.getByUsername(principal.getName()));
        return "user/profile";
    }
}
