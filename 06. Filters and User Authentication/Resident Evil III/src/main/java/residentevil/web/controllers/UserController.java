package residentevil.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import residentevil.domain.entities.User;
import residentevil.domain.models.binding.RegisterUserBindingModel;
import residentevil.domain.models.binding.UserRoleBindingModel;
import residentevil.services.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final String VIEW_REGISTER = "user/register";
    private static final String VIEW_LOGIN = "user/login";
    private static final String VIEW_ALL = "user/all";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new RegisterUserBindingModel());
        return VIEW_REGISTER;
    }

    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute("user") RegisterUserBindingModel bindingModel, Errors errors) {
        if (errors.hasErrors()) {
            return VIEW_REGISTER;
        }

        if (!bindingModel.getPassword().equals(bindingModel.getConfirmPassword())) {
            errors.rejectValue("password", "Passwords don't match.");
            errors.rejectValue("confirmPassword", "Passwords don't match.");
            return VIEW_REGISTER;
        }

        boolean saved = this.userService.save(bindingModel);
        if (!saved) {
            errors.rejectValue("username", "Username already exists.");
            return VIEW_REGISTER;
        }

        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String login() {
        return VIEW_LOGIN;
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return VIEW_ALL;
    }

    @PatchMapping("/edit")
    public String edit(@ModelAttribute UserRoleBindingModel model, Authentication authentication) {
        userService.updateRole(model, (User) authentication.getPrincipal());
        return "redirect:/user/users";
    }
}
