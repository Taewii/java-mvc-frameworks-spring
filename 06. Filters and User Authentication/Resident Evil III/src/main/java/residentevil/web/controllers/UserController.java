package residentevil.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.SecurityContextProvider;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import residentevil.domain.entities.User;
import residentevil.domain.models.binding.RegisterUserBindingModel;
import residentevil.domain.models.binding.UserRoleBindingModel;
import residentevil.services.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute RegisterUserBindingModel bindingModel, Errors errors) {
        if (errors.hasErrors() || !bindingModel.getPassword().equals(bindingModel.getConfirmPassword())) {
            return "register";
        }

        this.userService.save(bindingModel);
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @PatchMapping("/edit")
    public String edit(@ModelAttribute UserRoleBindingModel model, Authentication authentication) {
        userService.updateRole(model, (User) authentication.getPrincipal());
        return "redirect:/user/users";
    }
}
