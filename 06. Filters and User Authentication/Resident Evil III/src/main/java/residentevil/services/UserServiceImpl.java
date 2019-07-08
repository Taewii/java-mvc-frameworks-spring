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
import residentevil.domain.models.view.UserViewModel;
import residentevil.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

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

        boolean isRoot = userRepository.isRoot() == 0;
        user.setAuthority(isRoot ? Authority.ROOT : Authority.USER);
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
}
