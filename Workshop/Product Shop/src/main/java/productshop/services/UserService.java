package productshop.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import productshop.domain.models.binding.user.EditUserProfileBindingModel;
import productshop.domain.models.binding.user.RegisterUserBindingModel;
import productshop.domain.models.view.user.ListUserWithRolesViewModel;

import java.util.List;

public interface UserService extends UserDetailsService {

    boolean register(RegisterUserBindingModel user);

    <T> T getByUsername(String username, Class<T> targetClass);

    boolean edit(String username, EditUserProfileBindingModel profile);

    List<ListUserWithRolesViewModel> findAll();

    void changeRole(String userId, String newRole);
}
