package org.softuni.cardealer.web.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Customer;
import org.softuni.cardealer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomersControllerTest {

    private static final String REDIRECT_PATTERN_USERS_LOGIN = "**/users/login";

    private static final String ALL_CUSTOMERS_VIEW = "all-customers";

    private static final String URL_CUSTOMERS_BASE = "/customers";
    private static final String URL_CUSTOMERS_ALL = URL_CUSTOMERS_BASE + "/all";
    private static final String URL_CUSTOMERS_ADD = URL_CUSTOMERS_BASE + "/add";

    private static final String PARAM_NAME = "name";
    private static final String PARAM_BIRTH_DATE = "birthDate";
    private static final LocalDate PARAM_BIRTH_DATE_AS_LOCALDATE = LocalDate.of(2000, 1, 2);

    private static final String CUSTOMERS_ATTRIBUTE = "customers";

    private static final String NAME = "name";
    private static final String BIRTH_DATE = "2000-01-02";
    private static final String EMPTY = "";

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    @WithAnonymousUser
    public void add_post_anonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(post(URL_CUSTOMERS_ADD))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_PATTERN_USERS_LOGIN));
    }

    @Test
    @WithMockUser
    public void add_post_authenticatedUserAndValidData_returnsCorrectViewAndStatusCode() throws Exception {
        // should redirect to /customers/all instead of just /all
        mockMvc.perform(post(URL_CUSTOMERS_ADD)
                .param(PARAM_NAME, NAME)
                .param(PARAM_BIRTH_DATE, BIRTH_DATE))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_CUSTOMERS_ALL));
    }

    @Test
    @WithMockUser
    public void add_post_authenticatedUserAndValidData_addsCustomer() throws Exception {
        mockMvc.perform(post(URL_CUSTOMERS_ADD)
                .param(PARAM_NAME, NAME)
                .param(PARAM_BIRTH_DATE, BIRTH_DATE));

        Customer customer = customerRepository.findAll().get(0);

        assertEquals(1, customerRepository.count());
        assertEquals(NAME, customer.getName());
        assertEquals(PARAM_BIRTH_DATE_AS_LOCALDATE, customer.getBirthDate());
    }

    @Test(expected = Exception.class)
    @WithMockUser
    public void add_post_authenticatedUserAndEmptyFields_throwsException() throws Exception {
        mockMvc.perform(post(URL_CUSTOMERS_ADD)
                .param(PARAM_NAME, EMPTY)
                .param(PARAM_BIRTH_DATE, EMPTY));
    }

    @Test
    @WithAnonymousUser
    public void all_get_anonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get(URL_CUSTOMERS_ALL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_PATTERN_USERS_LOGIN));
    }

    @Test
    @WithMockUser
    public void all_get_authenticatedUser_returnsCorrectAttributesViewAndStatusCode() throws Exception {
        Customer customer = new Customer();
        customer.setName(NAME);
        customer.setBirthDate(PARAM_BIRTH_DATE_AS_LOCALDATE);
        customerRepository.save(customer);

        mockMvc.perform(get(URL_CUSTOMERS_ALL))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name(ALL_CUSTOMERS_VIEW))
                .andExpect(model().attributeExists(CUSTOMERS_ATTRIBUTE))
                .andExpect(model().attribute(CUSTOMERS_ATTRIBUTE, hasSize(1)));
    }
}