package residentevil.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import residentevil.domain.entities.User;
import residentevil.domain.models.binding.RegisterUserBindingModel;
import residentevil.domain.models.binding.UserRoleBindingModel;
import residentevil.domain.models.view.UserViewModel;

import java.util.List;

public interface UserService extends UserDetailsService {

    void save(RegisterUserBindingModel user);

    List<UserViewModel> findAll();

    boolean updateRole(UserRoleBindingModel model, User user);
}
