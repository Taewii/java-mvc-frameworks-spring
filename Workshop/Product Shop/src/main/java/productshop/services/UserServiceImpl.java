package productshop.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import productshop.domain.entities.Role;
import productshop.domain.entities.User;
import productshop.domain.enums.Authority;
import productshop.domain.models.binding.EditUserProfileBindingModel;
import productshop.domain.models.binding.RegisterUserBindingModel;
import productshop.domain.models.view.ListUserWithRolesViewModel;
import productshop.repositories.RoleRepository;
import productshop.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

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
            user.setRoles(this.getInheritedRolesFromRole("ROOT"));
        } else {
            user.setRoles(this.getInheritedRolesFromRole("USER"));
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

    @Override
    public List<ListUserWithRolesViewModel> findAll() {
        return userRepository.findAll().stream()
                .map(u -> {
                    ListUserWithRolesViewModel model = mapper.map(u, ListUserWithRolesViewModel.class);
                    model.setRoles(u.getRoles()
                            .parallelStream()
                            .map(r -> r.authorityAsEnum().name())
                            .collect(Collectors.toList()));
                    model.setMainRole(this.getMainRole(u.getRoles()));
                    return model;
                }).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void changeRole(String userId, String newRole) {
        if (newRole.equalsIgnoreCase("root")) {
            throw new IllegalArgumentException("Cannot change role to ROOT.");
        }

        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow();
        user.setRoles(this.getInheritedRolesFromRole(newRole));
        userRepository.saveAndFlush(user);
    }

    private Authority getMainRole(Set<Role> roles) {
        List<Authority> authorities = Arrays.asList(Authority.values());

        int bestRoleIndex = Integer.MAX_VALUE;
        for (Role role : roles) {
            int roleIndex = authorities.indexOf(role.authorityAsEnum());
            if (roleIndex != -1 && roleIndex < bestRoleIndex) {
                bestRoleIndex = roleIndex;
            }
        }

        return authorities.get(bestRoleIndex);
    }

    private Set<Role> getInheritedRolesFromRole(String role) {
        Set<Role> roles = new HashSet<>();
        for (int i = ROLES.indexOf(role.toUpperCase()); i < ROLES.size(); i++) {
            roles.add(roleRepository.findById(i + 1L).orElseThrow());
        }

        return roles;
    }
}
