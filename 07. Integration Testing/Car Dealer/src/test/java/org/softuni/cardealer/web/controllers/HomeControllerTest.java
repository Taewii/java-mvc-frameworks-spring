package org.softuni.cardealer.web.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {

    private static final String URL_INDEX = "/";
    private static final String URL_HOME = "/home";
    private static final String REDIRECT_PATTERN_USERS_LOGIN = "**/users/login";

    private static final String INDEX_VIEW = "index";
    private static final String HOME_VIEW = "home";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    public void index_get_anonymousUser_returnsCorrectView() throws Exception {
        mockMvc.perform(get(URL_INDEX))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name(INDEX_VIEW));
    }

    @Test
    @WithMockUser
    public void index_get_authenticatedUser_isForbidden() throws Exception {
        mockMvc.perform(get(URL_INDEX))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void home_get_authenticatedUser_returnsCorrectView() throws Exception {
        mockMvc.perform(get(URL_HOME))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name(HOME_VIEW));
    }

    @Test
    @WithAnonymousUser
    public void home_get_anonymousUser_isForbidden() throws Exception {
        mockMvc.perform(get(URL_HOME))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_PATTERN_USERS_LOGIN));
    }
}