package residentevil.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import residentevil.domain.models.binding.RegisterUserBindingModel;
import residentevil.services.UserService;

import javax.validation.Valid;

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
    public String users() {
        return "users";
    }
}
