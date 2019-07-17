package productshop.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import productshop.domain.entities.Role;
import productshop.domain.entities.User;
import productshop.domain.models.binding.EditUserProfileBindingModel;
import productshop.domain.models.binding.RegisterUserBindingModel;
import productshop.repositories.RoleRepository;
import productshop.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service("UserServiceImpl")
@Transactional
public class UserServiceImpl implements UserService {

    private static final List<String> ROLES = new ArrayList<>() {{
        add("ROOT");
        add("ADMIN");
        add("MODERATOR");
        add("USER");
    }};

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper mapper;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository,
                           UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           ModelMapper mapper) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mapper = mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameEager(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with such username doesn't exist."));
    }

    @Override
    public boolean register(RegisterUserBindingModel model) {
        if (userRepository.findByUsernameEager(model.getUsername()).isPresent()) {
            return false;
        }

        User user = mapper.map(model, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (userRepository.count() == 0) {
            this.setUserRoles("ROOT", user);
        } else {
            this.setUserRoles("USER", user);
        }

        userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public <T> T getByUsername(String username, Class<T> targetClass) {
        return mapper.map(userRepository.findByUsername(username), targetClass);
    }

    @Override
    public boolean edit(String username, EditUserProfileBindingModel model) {
        User user = userRepository.findByUsername(username);
        if (!bCryptPasswordEncoder.matches(model.getOldPassword(), user.getPassword())) {
            return false;
        }

        user.setPassword(bCryptPasswordEncoder.encode(model.getNewPassword()));
        user.setEmail(model.getEmail());

        userRepository.saveAndFlush(user);
        return true;
    }

    private void setUserRoles(String role, User user) {
        for (int i = ROLES.indexOf(role); i < ROLES.size(); i++) {
            Role byName = roleRepository.getOne(i + 1);
            user.addRole(byName);
        }
    }
}
