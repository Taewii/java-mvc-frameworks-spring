package residentevil.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import residentevil.domain.entities.User;
import residentevil.domain.enums.Authority;
import residentevil.domain.models.binding.RegisterUserBindingModel;
import residentevil.domain.models.binding.UserRoleBindingModel;
import residentevil.domain.models.view.UserViewModel;
import residentevil.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static residentevil.domain.enums.Authority.*;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           ModelMapper mapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
    }

    @Override
    public void save(RegisterUserBindingModel model) {
        User user = mapper.map(model, User.class);

        boolean isRoot = userRepository.rootCount() == 0;
        user.setAuthority(isRoot ? ROOT : USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Override
    public List<UserViewModel> findAll() {
        return userRepository
                .findAll()
                .stream()
                .map(u -> mapper.map(u, UserViewModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean updateRole(UserRoleBindingModel model, User enforcer) {
        User affectedUser = userRepository.findById(model.getId()).orElse(null);
        Authority newRole = model.getRole();
        Authority enforcerRole = enforcer.getAuthority();

        //if user doesnt exist
        if (affectedUser == null) {
            return false;
        }

        // trying to change role to ROOT is not valid
        if (newRole == ROOT) {
            return false;
        }

        // only ROOT and ADMIN roles can change other users's roles
        if (enforcerRole != ADMIN && enforcerRole != ROOT) {
            return false;
        }

        // ADMIN should not be able to change role to ADMIN or ROOT
//        if (enforcerRole == ADMIN && newRole == ADMIN) {
//            return false;
//        }

        affectedUser.setAuthority(newRole);
        userRepository.saveAndFlush(affectedUser);
        return true;
    }
}
