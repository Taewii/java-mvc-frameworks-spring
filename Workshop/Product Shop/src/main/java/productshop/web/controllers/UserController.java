package productshop.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import productshop.domain.models.binding.EditUserProfileBindingModel;
import productshop.domain.models.binding.RegisterUserBindingModel;
import productshop.domain.models.view.EditUserProfileViewModel;
import productshop.domain.models.view.UserProfileViewModel;
import productshop.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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

        boolean isSuccessful = userService.register(user);
        if (!isSuccessful) {
            errors.rejectValue("username", "400", "Username is already in use.");
            return "user/register";
        }

        return "redirect:/users/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        model.addAttribute("user",
                userService.getByUsername(principal.getName(), UserProfileViewModel.class));
        return "user/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit")
    public String profileEdit(Model model, Principal principal) {
        model.addAttribute("profile",
                userService.getByUsername(principal.getName(), EditUserProfileViewModel.class));
        return "user/profile-edit";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/edit")
    public String profileEditPut(@Valid @ModelAttribute("profile") EditUserProfileBindingModel profile,
                                 HttpServletRequest request,
                                 Principal principal,
                                 Errors errors) throws ServletException {
        boolean passwordsMatch = profile.getNewPassword().equals(profile.getNewPasswordConfirm());
        if (errors.hasErrors() || !passwordsMatch) {
            if (!passwordsMatch) {
                errors.rejectValue("newPassword", "400", "Passwords don't match.");
                errors.rejectValue("newPasswordConfirm", "400", "Passwords don't match.");
            }
            return "user/profile-edit";
        }

        boolean isSuccessful = userService.edit(principal.getName(), profile);
        if (!isSuccessful) {
            errors.rejectValue("oldPassword", "400", "Wrong password.");
            return "user/profile-edit";
        }

        request.logout(); // force re-logging
        return "redirect:/users/login";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public String all(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/all";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/set-{role}/{id}")
    public String changeUserRole(@PathVariable("id") String id, @PathVariable("role") String role) {
        userService.changeRole(id, role);
        return "redirect:/users/all";
    }
}
