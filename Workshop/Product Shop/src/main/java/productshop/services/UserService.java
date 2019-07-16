package productshop.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import productshop.domain.models.binding.RegisterUserBindingModel;

public interface UserService extends UserDetailsService {

    boolean register(RegisterUserBindingModel user);
}
