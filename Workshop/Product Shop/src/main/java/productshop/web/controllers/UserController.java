package productshop.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import productshop.domain.entities.User;
import productshop.domain.models.binding.user.EditUserProfileBindingModel;
import productshop.domain.models.binding.user.RegisterUserBindingModel;
import productshop.domain.models.view.user.EditUserProfileViewModel;
import productshop.domain.models.view.user.UserProfileViewModel;
import productshop.domain.validation.UserValidator;
import productshop.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

import static productshop.config.Constants.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final String USER_VIEW_PATH = "user";
    private static final String LOGIN_VIEW = USER_VIEW_PATH + "/" + "login";
    private static final String ALL_VIEW = USER_VIEW_PATH + "/" + "all";
    private static final String REGISTER_VIEW = USER_VIEW_PATH + "/" + "register";
    private static final String PROFILE_VIEW = USER_VIEW_PATH + "/" + "profile";
    private static final String EDIT_PROFILE_VIEW = USER_VIEW_PATH + "/" + "profile-edit";

    private static final String ID_ATTRIBUTE = "id";
    private static final String ROLE_ATTRIBUTE = "role";
    private static final String USER_ATTRIBUTE = "user";
    private static final String USERS_ATTRIBUTE = "users";
    private static final String USERNAME_ATTRIBUTE = "username";
    private static final String PROFILE_ATTRIBUTE = "profile";
    private static final String OLD_PASSWORD_ATTRIBUTE = "oldPassword";

    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping("/login")
    public String login() {
        return LOGIN_VIEW;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute(USER_ATTRIBUTE, new RegisterUserBindingModel());
        return REGISTER_VIEW;
    }

    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute(USER_ATTRIBUTE) RegisterUserBindingModel user, Errors errors) {
        if (errors.hasErrors()) {
            return REGISTER_VIEW;
        }

        User entity = userService.register(user);
        if (entity == null) {
            errors.rejectValue(USERNAME_ATTRIBUTE, BAD_REQUEST_ERROR_CODE, USERNAME_ALREADY_IN_USE_MESSAGE);
            return REGISTER_VIEW;
        }

        return "redirect:/users/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        model.addAttribute(USER_ATTRIBUTE,
                userService.getByUsername(principal.getName(), UserProfileViewModel.class));
        return PROFILE_VIEW;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit")
    public String profileEdit(Model model, Principal principal) {
        model.addAttribute(PROFILE_ATTRIBUTE,
                userService.getByUsername(principal.getName(), EditUserProfileViewModel.class));
        return EDIT_PROFILE_VIEW;
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/edit")
    public String profileEditPut(@Valid @ModelAttribute(PROFILE_ATTRIBUTE) EditUserProfileBindingModel profile,
                                 Errors errors,
                                 HttpServletRequest request,
                                 Principal principal) throws ServletException {
        if (errors.hasErrors()) {
            return EDIT_PROFILE_VIEW;
        }

        User entity = userService.edit(principal.getName(), profile);
        if (entity == null) {
            errors.rejectValue(OLD_PASSWORD_ATTRIBUTE, BAD_REQUEST_ERROR_CODE, WRONG_PASSWORD_MESSAGE);
            return EDIT_PROFILE_VIEW;
        }

        request.logout(); // force re-logging
        return "redirect:/users/login";
    }

    @PreAuthorize(IS_ADMIN)
    @GetMapping("/all")
    public String all(Model model) {
        model.addAttribute(USERS_ATTRIBUTE, userService.findAll());
        return ALL_VIEW;
    }

    @PreAuthorize(IS_ADMIN)
    @PostMapping("/set-{role}/{id}")
    public String changeUserRole(@PathVariable(ID_ATTRIBUTE) String id, @PathVariable(ROLE_ATTRIBUTE) String role) {
        userService.changeRole(id, role);
        return "redirect:/users/all";
    }

    @InitBinder
    public void dataBinding(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }
}
