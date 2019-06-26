package exodia.services;

import exodia.domain.entities.User;
import exodia.domain.models.binding.UserLoginBindingModel;
import exodia.domain.models.binding.UserRegisterBindingModel;
import exodia.domain.models.service.UserServiceModel;
import exodia.repositories.UserRepository;
import exodia.util.PasswordHash;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean register(UserRegisterBindingModel model, BindingResult result) {
        if (result.hasErrors() || !model.getPassword().equals(model.getConfirmPassword())) {
            return false;
        }

        try {
            model.setPassword(PasswordHash.createHash(model.getPassword()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }

        this.userRepository.saveAndFlush(this.mapper.map(model, User.class));
        return true;
    }

    @Override
    public UserServiceModel login(UserLoginBindingModel model, BindingResult result) {
        if (result.hasErrors()) return null;

        User user = this.userRepository.findByUsername(model.getUsername());
        try {
            if (user != null && PasswordHash.validatePassword(model.getPassword(), user.getPassword())) {
                return this.mapper.map(user, UserServiceModel.class);
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }
}
