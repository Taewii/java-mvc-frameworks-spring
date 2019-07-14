package org.softuni.cardealer.web.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.Charset;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SuppliersControllerTest {

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private static final String REDIRECT_PATTERN_USERS_LOGIN = "**/users/login";
    private static final String ALL_SUPPLIERS_VIEW = "all-suppliers";

    private static final String URL_SUPPLIERS_BASE = "/suppliers";
    private static final String URL_SUPPLIERS_ADD = URL_SUPPLIERS_BASE + "/add";
    private static final String URL_SUPPLIERS_ALL = URL_SUPPLIERS_BASE + "/all";
    private static final String URL_SUPPLIERS_EDIT = URL_SUPPLIERS_BASE + "/edit";
    private static final String URL_SUPPLIERS_DELETE = URL_SUPPLIERS_BASE + "/delete";
    private static final String URL_SUPPLIERS_FETCH = URL_SUPPLIERS_BASE + "/fetch";

    private static final String SUPPLIERS_ATTRIBUTE = "suppliers";

    private static final String PARAM_NAME = "name";
    private static final String PARAM_IS_IMPORTER = "isImporter";

    private static final String NAME = "name";
    private static final String IS_IMPORTER = "true";
    private static final String EMPTY = "";

    private static final String EDITED_NAME = "editedName";
    private static final String EDITED_IS_IMPORTER = "false";

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private MockMvc mockMvc;

    private MockHttpServletRequestBuilder postSupplierValid;

    @Before
    public void setUp() {
        supplierRepository.deleteAll();

        postSupplierValid = post(URL_SUPPLIERS_ADD)
                .param(PARAM_NAME, NAME)
                .param(PARAM_IS_IMPORTER, IS_IMPORTER);
    }

    private Supplier createSupplier() {
        Supplier supplier = new Supplier();
        supplier.setName(NAME);
        supplier.setIsImporter(Boolean.valueOf(IS_IMPORTER));

        return supplierRepository.save(supplier);
    }

    @Test
    @WithAnonymousUser
    public void add_post_anonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(postSupplierValid)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_PATTERN_USERS_LOGIN));
    }

    @Test
    @WithMockUser
    public void add_post_authenticatedUserValidData_returnsCorrectStatusCodeAndRedirectsCorrectly() throws Exception {
        // should be /suppliers/all instead of just /all
        mockMvc.perform(postSupplierValid)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_SUPPLIERS_ALL));
    }

    @Test
    @WithMockUser
    public void add_post_authenticatedUserValidData_addsSupplierToDatabase() throws Exception {
        mockMvc.perform(postSupplierValid);
        Supplier supplier = supplierRepository.findAll().get(0);

        assertEquals(1, supplierRepository.count());
        assertEquals(NAME, supplier.getName());
        assertEquals(Boolean.valueOf(IS_IMPORTER), supplier.getIsImporter());
    }

    @Test
    @WithMockUser
    public void add_post_authenticatedUserDuplicateSuppliers_createsSuppliersSuccessfully() throws Exception {
        mockMvc.perform(postSupplierValid);
        mockMvc.perform(postSupplierValid);

        assertEquals(2, supplierRepository.count());
    }

    @Test(expected = Exception.class)
    @WithMockUser
    public void add_post_authenticatedUserEmptyFields_throwsException() throws Exception {
        mockMvc.perform(post(URL_SUPPLIERS_ADD)
                .param(PARAM_NAME, EMPTY)
                .param(PARAM_IS_IMPORTER, EMPTY));
    }

    @Test
    @WithAnonymousUser
    public void edit_post_anonymousUser_redirectsToLoginPage() throws Exception {
        Supplier supplier = createSupplier();
        mockMvc.perform(post(URL_SUPPLIERS_EDIT + "/" + supplier.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_PATTERN_USERS_LOGIN));
    }

    @Test(expected = Exception.class)
    @WithMockUser
    public void edit_post_authenticatedUserInvalidId_throwsException() throws Exception {
        mockMvc.perform(post(URL_SUPPLIERS_EDIT + "/" + "invalid"));
    }

    @Test
    @WithMockUser
    public void edit_post_authenticatedUserValidData_editsCorrectly() throws Exception {
        Supplier supplier = createSupplier();

        mockMvc.perform(post(URL_SUPPLIERS_EDIT + "/" + supplier.getId())
                .param(PARAM_NAME, EDITED_NAME)
                .param(PARAM_IS_IMPORTER, EDITED_IS_IMPORTER));

        Supplier editedSupplier = supplierRepository.findById(supplier.getId()).orElseThrow();

        assertEquals(1, supplierRepository.count());
        assertEquals(EDITED_NAME, editedSupplier.getName());
        assertEquals(Boolean.valueOf(EDITED_IS_IMPORTER), editedSupplier.getIsImporter());
    }

    @Test
    @WithMockUser
    public void edit_post_authenticatedUserValidData_returnsCorrectStatusCodeAndRedirectsCorrectly() throws Exception {
        Supplier supplier = createSupplier();

        mockMvc.perform(post(URL_SUPPLIERS_EDIT + "/" + supplier.getId())
                .param(PARAM_NAME, EDITED_NAME)
                .param(PARAM_IS_IMPORTER, EDITED_IS_IMPORTER))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_SUPPLIERS_ALL));
    }

    @Test
    @WithAnonymousUser
    public void delete_post_anonymousUser_redirectsToLoginPage() throws Exception {
        Supplier supplier = createSupplier();

        mockMvc.perform(post(URL_SUPPLIERS_DELETE + "/" + supplier.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_PATTERN_USERS_LOGIN));
    }

    @Test(expected = Exception.class)
    @WithMockUser
    public void delete_post_authenticatedUserInvalidId_throwsException() throws Exception {
        mockMvc.perform(post(URL_SUPPLIERS_DELETE + "/" + "invalid"));
    }

    @Test
    @WithMockUser
    public void delete_post_authenticatedUserValidData_deletesCorrectly() throws Exception {
        Supplier supplier = createSupplier();

        mockMvc.perform(post(URL_SUPPLIERS_DELETE + "/" + supplier.getId()));
        assertFalse(supplierRepository.findById(supplier.getId()).isPresent());
        assertEquals(0, supplierRepository.count());
    }

    @Test
    @WithMockUser
    public void delete_post_authenticatedUserValidData_returnsCorrectStatusCodeAndRedirectsCorrectly() throws Exception {
        Supplier supplier = createSupplier();

        mockMvc.perform(post(URL_SUPPLIERS_DELETE + "/" + supplier.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_SUPPLIERS_ALL));
    }

    @Test
    @WithAnonymousUser
    public void all_get_anonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get(URL_SUPPLIERS_ALL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_PATTERN_USERS_LOGIN));
    }

    @Test
    @WithMockUser
    public void all_get_authenticatedUser_returnsCorrectAttributesViewAndStatusCode() throws Exception {
        createSupplier();

        mockMvc.perform(get(URL_SUPPLIERS_ALL))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name(ALL_SUPPLIERS_VIEW))
                .andExpect(model().attributeExists(SUPPLIERS_ATTRIBUTE))
                .andExpect(model().attribute(SUPPLIERS_ATTRIBUTE, hasSize(1)));
    }

    @Test
    @WithAnonymousUser
    public void fetch_get_anonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get(URL_SUPPLIERS_FETCH))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_PATTERN_USERS_LOGIN));
    }

    @Test
    @WithMockUser
    public void fetch_get_authenticatedUser_returnsCorrectStatusCode() throws Exception {
        createSupplier();

        mockMvc.perform(get(URL_SUPPLIERS_FETCH))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    @Test
    @WithMockUser
    public void fetch_get_authenticatedUser_returnsCorrectData() throws Exception {
        createSupplier();

        List<Supplier> suppliers = supplierRepository.findAll();

        mockMvc.perform(get(URL_SUPPLIERS_FETCH))
                .andExpect(jsonPath("$", hasSize(suppliers.size())))
                .andExpect(jsonPath("$[0].id", is(suppliers.get(0).getId())))
                .andExpect(jsonPath("$[0].name", is(suppliers.get(0).getName())))
                .andExpect(jsonPath("$[0].isImporter", is(suppliers.get(0).getIsImporter())));
    }
}