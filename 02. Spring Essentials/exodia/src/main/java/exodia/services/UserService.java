package exodia.services;

import exodia.domain.models.binding.UserLoginBindingModel;
import exodia.domain.models.binding.UserRegisterBindingModel;
import exodia.domain.models.service.UserServiceModel;
import org.springframework.validation.BindingResult;

public interface UserService {

    boolean register(UserRegisterBindingModel model, BindingResult result);

    UserServiceModel login(UserLoginBindingModel model, BindingResult result);
}
