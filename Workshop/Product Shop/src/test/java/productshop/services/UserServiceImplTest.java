package productshop.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import productshop.domain.entities.User;
import productshop.repositories.RoleRepository;
import productshop.repositories.UserRepository;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;


    @Test
    public void loadUserByUsername_withValidInput_returnsUserDetails() {
        User user = new User();
        when(userRepository.findByUsernameEager(any(String.class))).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("test");
        assertNotNull(result);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_withInvalidInput_throwsUsernameNotFoundException() {
        userService.loadUserByUsername(null);
    }
}