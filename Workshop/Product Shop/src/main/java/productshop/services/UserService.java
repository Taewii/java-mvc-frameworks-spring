package productshop.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import productshop.domain.models.binding.RegisterUserBindingModel;
import productshop.domain.models.view.UserProfileViewModel;

public interface UserService extends UserDetailsService {

    boolean register(RegisterUserBindingModel user);

    UserProfileViewModel getByUsername(String name);
}
