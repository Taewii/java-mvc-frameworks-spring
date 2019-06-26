package exodia.web.controllers;

import exodia.domain.models.binding.UserLoginBindingModel;
import exodia.domain.models.binding.UserRegisterBindingModel;
import exodia.domain.models.service.UserServiceModel;
import exodia.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
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
    public String registerPost(@Valid @ModelAttribute UserRegisterBindingModel model, BindingResult result) {
        if (this.userService.register(model, result)) {
            return "redirect:/login";
        }

        return "redirect:/register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@Valid @ModelAttribute UserLoginBindingModel model,
                            BindingResult result,
                            HttpSession session) {

        UserServiceModel userServiceModel = this.userService.login(model, result);
        if (userServiceModel != null) {
            session.setAttribute("userId", userServiceModel.getId());
            return "redirect:/";
//            return "redirect:/home";
        }

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
