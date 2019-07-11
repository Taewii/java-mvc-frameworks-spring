package org.softuni.cardealer.web.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.User;
import org.softuni.cardealer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UsersControllerTest {

    private static final String URL_USERS_BASE = "/users";
    private static final String URL_USERS_LOGIN = URL_USERS_BASE + "/login";
    private static final String URL_USERS_REGISTER = URL_USERS_BASE + "/register";

    private static final String VIEW_LOGIN = "login";
    private static final String VIEW_REGISTER = "register";

    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_CONFIRM_PASSWORD = "confirmPassword";
    private static final String PARAM_EMAIL = "email";

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String CONFIRM_PASSWORD = "password";
    private static final String EMAIL = "email@gmail.com";
    private static final String NOT_THE_SAME_PASSWORD = "randomPassword";

    private static final String EMPTY_VALUE = "";
    private MockHttpServletRequestBuilder POST_WITH_VALID_PARAMS =
            post(URL_USERS_REGISTER)
                    .param(PARAM_USERNAME, USERNAME)
                    .param(PARAM_PASSWORD, PASSWORD)
                    .param(PARAM_CONFIRM_PASSWORD, CONFIRM_PASSWORD)
                    .param(PARAM_EMAIL, EMAIL);

    private MockHttpServletRequestBuilder POST_WITH_DIFFERENT_PASSWORDS_INVALID =
            post(URL_USERS_REGISTER)
                    .param(PARAM_USERNAME, USERNAME)
                    .param(PARAM_PASSWORD, PASSWORD)
                    .param(PARAM_CONFIRM_PASSWORD, NOT_THE_SAME_PASSWORD)
                    .param(PARAM_EMAIL, EMAIL);

    private MockHttpServletRequestBuilder POST_WITH_EMPTY_FIELDS_INVALID =
            post(URL_USERS_REGISTER)
                    .param(PARAM_USERNAME, EMPTY_VALUE)
                    .param(PARAM_PASSWORD, EMPTY_VALUE)
                    .param(PARAM_CONFIRM_PASSWORD, EMPTY_VALUE)
                    .param(PARAM_EMAIL, EMPTY_VALUE);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void setUp() {
        this.userRepository.deleteAll();
    }

    @Test
    @WithAnonymousUser
    public void login_get_anonymousUser_returnsCorrectView() throws Exception {
        mockMvc.perform(get(URL_USERS_LOGIN))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name(VIEW_LOGIN));
    }

    @Test
    @WithMockUser
    public void login_get_authenticatedUser_isForbidden() throws Exception {
        mockMvc.perform(get(URL_USERS_LOGIN))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void register_get_anonymousUser_returnsCorrectView() throws Exception {
        mockMvc.perform(get(URL_USERS_REGISTER))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name(VIEW_REGISTER));
    }

    @Test
    @WithMockUser
    public void register_get_authenticatedUser_isForbidden() throws Exception {
        mockMvc.perform(get(URL_USERS_REGISTER))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void register_post_authenticatedUser_isForbidden() throws Exception {
        mockMvc.perform(post(URL_USERS_REGISTER))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void register_post_validDataAndAnonymousUser_registersNewUser() throws Exception {
        mockMvc.perform(POST_WITH_VALID_PARAMS);

        User user = userRepository.findByUsername(USERNAME).orElseThrow();

        assertEquals(USERNAME, user.getUsername());
        assertEquals(EMAIL, user.getEmail());
        assertTrue(bCryptPasswordEncoder.matches(PASSWORD, user.getPassword()));
    }

    @Test
    @WithAnonymousUser
    public void register_post_validDataAndAnonymousUser_returnsCorrectCodeAndRedirectView() throws Exception {
        // should be /users/login instead of just /login

        mockMvc.perform(POST_WITH_VALID_PARAMS)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_USERS_LOGIN));
    }

    @Test(expected = Exception.class)
    @WithAnonymousUser
    public void register_post_withDuplicateData_throwsException() throws Exception {
        // needs to throw a better exception
        mockMvc.perform(POST_WITH_VALID_PARAMS);
        mockMvc.perform(POST_WITH_VALID_PARAMS);
    }

    @Test(expected = Exception.class)
    @WithAnonymousUser
    public void register_post_notMatchingPasswordsAndAnonymousUser_throwsException() throws Exception {
        // passwords are never checked
        mockMvc.perform(POST_WITH_DIFFERENT_PASSWORDS_INVALID);
    }

    @Test(expected = Exception.class)
    @WithAnonymousUser
    public void register_post_emptyFieldsAndAnonymousUser_throwsException() throws Exception {
        mockMvc.perform(POST_WITH_EMPTY_FIELDS_INVALID);
    }
}