package residentevil.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import residentevil.domain.models.binding.RegisterUserBindingModel;
import residentevil.domain.models.view.UserViewModel;

import java.util.List;

public interface UserService extends UserDetailsService {

    void save(RegisterUserBindingModel user);

    List<UserViewModel> findAll();
}
