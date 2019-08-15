package productshop.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import productshop.domain.entities.Role;
import productshop.domain.entities.User;
import productshop.domain.enums.Authority;
import productshop.domain.models.binding.user.EditUserProfileBindingModel;
import productshop.domain.models.binding.user.RegisterUserBindingModel;
import productshop.domain.models.view.user.ListUserWithRolesViewModel;
import productshop.domain.models.view.user.UserProfileViewModel;
import productshop.repositories.RoleRepository;
import productshop.repositories.UserRepository;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder encoder;

    @Captor
    private ArgumentCaptor captor;

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

    @Test
    public void getByUsername_withValidInput_returnsCorrectlyMappedUser() {
        User user = new User();
        user.setEmail("email");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        UserProfileViewModel result = userService.getByUsername("test", UserProfileViewModel.class);

        assertEquals(UserProfileViewModel.class, result.getClass());
        assertEquals("email", result.getEmail());
    }

    @Test(expected = NoSuchElementException.class)
    public void getByUsername_withInvalidUsername_throwsNoSuchElementException() {
        userService.getByUsername("invalid", UserProfileViewModel.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getByUsername_withInvalidTargetClass_throwsIllegalArgumentException() {
        User user = mock(User.class);
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        userService.getByUsername("valid", null);
    }

    @Test
    public void findAll_with5ValidUsers_returnsCorrectlyMapped5Users() {
        List<User> users = createUsers(5);
        when(userRepository.findAll()).thenReturn(users);

        List<ListUserWithRolesViewModel> result = userService.findAll();

        assertEquals(5, result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(ListUserWithRolesViewModel.class, result.get(i).getClass());
            assertEquals("username " + i, result.get(i).getUsername());
            assertEquals("email " + i, result.get(i).getEmail());
            assertEquals(Authority.ROOT, result.get(i).getMainRole());
        }
    }

    @Test
    public void findAll_withNoUsers_returnsEmptyList() {
        List<ListUserWithRolesViewModel> result = userService.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    public void changeRole_withValidInput_changesRolesSuccessfully() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(new Role(Authority.ROOT) {{ setId(1L); }}));
        when(roleRepository.findById(2L)).thenReturn(Optional.of(new Role(Authority.ADMIN) {{ setId(2L); }}));
        when(roleRepository.findById(3L)).thenReturn(Optional.of(new Role(Authority.MODERATOR) {{ setId(3L); }}));
        when(roleRepository.findById(4L)).thenReturn(Optional.of(new Role(Authority.USER) {{ setId(4L); }}));

        User user = new User();
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        userService.changeRole(UUID.randomUUID().toString(), "admiN");

        assertEquals(3, user.getRoles().size());
        for (Role role : user.getRoles()) {
            assertNotEquals("ROLE_ROOT", role.getAuthority());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeRole_withRootAsNewRole_throwsIllegalArgumentException() {
        userService.changeRole("1", "rooT");
    }

    @Test(expected = NoSuchElementException.class)
    public void changeRole_withInvalidUserId_throwsNoSuchElementException() {
        userService.changeRole(UUID.randomUUID().toString(), "user");
    }

    @Test(expected = NoSuchElementException.class)
    public void changeRole_withInvalidNewRole_throwsNoSuchElementException() {
        User user = new User();
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        userService.changeRole(UUID.randomUUID().toString(), "invalid");
    }

    @Test
    public void register_withExistingUser_returnsNullAndStopsMethodExecution() {
        User user = mock(User.class);
        RegisterUserBindingModel model = mock(RegisterUserBindingModel.class);
        when(model.getUsername()).thenReturn("existing");
        when(userRepository.findByUsernameEager(any(String.class))).thenReturn(Optional.of(user));

        User result = userService.register(model);

        verify(userRepository, never()).saveAndFlush(any(User.class));
        assertNull(result);
    }

    @Test
    public void register_withValidInputAndNoOtherUsersInDb_registersUserSuccessfullyAndSetsHimRootRole() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(new Role(Authority.ROOT) {{ setId(1L); }}));
        when(roleRepository.findById(2L)).thenReturn(Optional.of(new Role(Authority.ADMIN) {{ setId(2L); }}));
        when(roleRepository.findById(3L)).thenReturn(Optional.of(new Role(Authority.MODERATOR) {{ setId(3L); }}));
        when(roleRepository.findById(4L)).thenReturn(Optional.of(new Role(Authority.USER) {{ setId(4L); }}));

        RegisterUserBindingModel model = new RegisterUserBindingModel();
        model.setUsername("username");
        model.setPassword("password");
        model.setEmail("email");

        User result = userService.register(model);

        // TODO: 30.7.2019 г. not sure how to do the testing here without in-memory db
        verify(userRepository).saveAndFlush(any(User.class));
    }

    @Test
    public void register_withValidInput_registersUserSuccessfullyAndSetsHimUserRole() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(new Role(Authority.ROOT) {{ setId(1L); }}));
        when(roleRepository.findById(2L)).thenReturn(Optional.of(new Role(Authority.ADMIN) {{ setId(2L); }}));
        when(roleRepository.findById(3L)).thenReturn(Optional.of(new Role(Authority.MODERATOR) {{ setId(3L); }}));
        when(roleRepository.findById(4L)).thenReturn(Optional.of(new Role(Authority.USER) {{ setId(4L); }}));

        when(userRepository.count()).thenReturn(1L);

        RegisterUserBindingModel model = new RegisterUserBindingModel();
        model.setUsername("username");
        model.setPassword("password");
        model.setEmail("email");

        User result = userService.register(model);

        // TODO: 30.7.2019 г. not sure how to do the testing here without in-memory db
        verify(userRepository).saveAndFlush(any(User.class));
    }

    @Test
    public void edit_withValidInput_successfullyEditsUser() {
        when(encoder.matches(any(String.class), any(String.class))).thenReturn(true);
        when(encoder.encode(any(String.class))).thenReturn("new password");

        EditUserProfileBindingModel model = mock(EditUserProfileBindingModel.class);
        when(model.getOldPassword()).thenReturn("old password");
        when(model.getNewPassword()).thenReturn("new password");
        when(model.getEmail()).thenReturn("new email");

        User user = mock(User.class);
        when(user.getPassword()).thenReturn("old password");

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        userService.edit("username", model);

        verify(user).setPassword(String.valueOf(captor.capture()));
        verify(user).setEmail(String.valueOf(captor.capture()));

        List allValues = captor.getAllValues();
        assertTrue(allValues.contains("new password"));
        assertTrue(allValues.contains("new email"));

        verify(userRepository).saveAndFlush(any(User.class));
    }

    @Test
    public void edit_withPasswordsNotMatching_returnsNullAndStopsMethodExecution() {
        EditUserProfileBindingModel model = mock(EditUserProfileBindingModel.class);
        when(model.getOldPassword()).thenReturn("invalid password");

        User user = mock(User.class);
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn("old password");

        User username = userService.edit("username", model);

        assertNull(username);
        verify(userRepository, never()).saveAndFlush(user);
    }

    @Test(expected = NoSuchElementException.class)
    public void edit_withInvalidUsername_throwsNoSuchElementFoundException() {
        EditUserProfileBindingModel model = mock(EditUserProfileBindingModel.class);
        userService.edit("invalid", model);
    }

    private List<User> createUsers(int count) {
        List<User> users = new ArrayList<>();

        Set<Role> roles = new HashSet<>();

        for (int i = 0; i < Authority.values().length; i++) {
            Role role = new Role();
            role.setAuthority(Authority.values()[i]);
            role.setId(i + 1L);
            roles.add(role);
        }

        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setUsername("username " + i);
            user.setEmail("email " + i);
            user.setRoles(roles);
            users.add(user);
        }

        return users;
    }
}