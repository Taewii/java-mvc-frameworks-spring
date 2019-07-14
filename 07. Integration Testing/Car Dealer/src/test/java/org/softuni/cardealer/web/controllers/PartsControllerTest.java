package org.softuni.cardealer.web.controllers;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Part;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.repository.PartRepository;
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

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PartsControllerTest {

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private static final String URL_PARTS_BASE = "/parts";
    private static final String URL_PARTS_ADD = URL_PARTS_BASE + "/add";
    private static final String URL_PARTS_EDIT = URL_PARTS_BASE + "/edit";
    private static final String URL_PARTS_EDIT_INVALID_ID = URL_PARTS_EDIT + "/invalid-id";
    private static final String URL_PARTS_DELETE = URL_PARTS_BASE + "/delete";
    private static final String URL_PARTS_DELETE_INVALID_ID = URL_PARTS_DELETE + "/invalid-id";
    private static final String URL_PARTS_ALL = URL_PARTS_BASE + "/all";
    private static final String URL_PARTS_FETCH = URL_PARTS_BASE + "/fetch";

    private static final String REDIRECT_URL_PATTERN_LOGIN = "**/users/login";

    private static final String VIEW_ALL_PARTS = "all-parts";

    private static final String ATTRIBUTE_PARTS = "parts";

    private static final String PARAM_NAME = "name";
    private static final String PARAM_PRICE = "price";
    private static final String PARAM_SUPPLIER = "supplier";

    private static final String PART_NAME = "part";
    private static final String PART_NAME_NEW = "New part";
    private static final String PART_PRICE = "1.00";
    private static final String PART_PRICE_NEW = "10.00";
    private static final String PART_SUPPLIER_NAME = "supplier";

    private static final MockHttpServletRequestBuilder POST_ADD_PART_VALID_DATA =
            post(URL_PARTS_ADD)
                    .param(PARAM_NAME, PART_NAME)
                    .param(PARAM_PRICE, PART_PRICE)
                    .param(PARAM_SUPPLIER, PART_SUPPLIER_NAME);


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Before
    public void setUp() {
        partRepository.deleteAll();
        supplierRepository.deleteAll();
    }

    private Supplier createSupplier(String name) {
        Supplier supplier = new Supplier();
        supplier.setName(name);
        return supplierRepository.save(supplier);
    }


    private Part createPart(Supplier supplier, String partName, BigDecimal price) {
        Part part = new Part();
        part.setName(partName);
        part.setPrice(price);
        part.setSupplier(supplier);

        part = partRepository.save(part);
        return part;
    }

    @Test
    @WithAnonymousUser
    public void add_post_anonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(POST_ADD_PART_VALID_DATA)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_URL_PATTERN_LOGIN));
    }

    @Test
    @WithMockUser
    public void add_post_authAuthenticatedUserValidDAta_returnsCorrectViewAndStatus() throws Exception {
        createSupplier(PART_SUPPLIER_NAME);

        mockMvc.perform(POST_ADD_PART_VALID_DATA)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_PARTS_ALL));
    }

    @Test
    @WithMockUser
    public void add_post_authenticatedUserValidData_createsNewPart() throws Exception {
        createSupplier(PART_SUPPLIER_NAME);

        mockMvc.perform(POST_ADD_PART_VALID_DATA);

        List<Part> parts = partRepository.findAll();

        assertFalse(parts.isEmpty());
        assertEquals(PART_NAME, parts.get(0).getName());
        assertEquals(new BigDecimal(PART_PRICE), parts.get(0).getPrice());
        assertEquals(PART_SUPPLIER_NAME, parts.get(0).getSupplier().getName());
    }

    @Test(expected = Exception.class)
    @WithMockUser
    public void add_post_authenticatedUserInvalidSupplier_throwsException() throws Exception {
        mockMvc.perform(POST_ADD_PART_VALID_DATA);
    }

    @Test
    @WithMockUser
    public void add_post_authenticatedUserDuplicatedData_bothPartsCreatedSuccessfully() throws Exception {
        createSupplier(PART_SUPPLIER_NAME);

        mockMvc.perform(POST_ADD_PART_VALID_DATA);
        mockMvc.perform(POST_ADD_PART_VALID_DATA);

        List<Part> parts = partRepository.findAll()
                .stream()
                .filter(supplier -> PART_NAME.equals(supplier.getName()))
                .collect(Collectors.toList());

        assertEquals(2, parts.size());
    }


    @Test
    @WithAnonymousUser
    public void edit_post_anonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(post(URL_PARTS_EDIT_INVALID_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_URL_PATTERN_LOGIN));
    }

    @Test(expected = Exception.class)
    @WithMockUser
    public void edit_post_authenticatedUserInvalidData_shouldThrowException() throws Exception {
        mockMvc.perform(post(URL_PARTS_EDIT_INVALID_ID));
    }

    @Test
    @WithMockUser
    public void edit_post_authenticatedUserValidData_returnsCorrectViewAndStatus() throws Exception {
        Supplier supplier = createSupplier(PART_SUPPLIER_NAME);

        Part part = createPart(supplier, PART_NAME, new BigDecimal(PART_PRICE));

        mockMvc.perform(post(URL_PARTS_EDIT + "/" + part.getId())
                .param(PARAM_NAME, PART_NAME_NEW)
                .param(PARAM_PRICE, "1.00")
                .param(PARAM_SUPPLIER, PART_SUPPLIER_NAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_PARTS_ALL));
    }

    @Test
    @WithMockUser
    public void edit_post_authenticatedUserValidData_updateSucceeds() throws Exception {
        Supplier supplier = createSupplier(PART_SUPPLIER_NAME);

        Part part = createPart(supplier, PART_NAME, new BigDecimal(PART_PRICE));

        mockMvc.perform(post(URL_PARTS_EDIT + "/" + part.getId())
                .param(PARAM_NAME, PART_NAME_NEW)
                .param(PARAM_PRICE, PART_PRICE_NEW))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_PARTS_ALL));

        Optional<Part> updated = partRepository.findById(part.getId());

        assertTrue(updated.isPresent());
        assertEquals(PART_NAME_NEW, updated.get().getName());
        assertEquals(1, updated.get().getPrice().compareTo(BigDecimal.ONE));
    }

    @Test
    @WithAnonymousUser
    public void delete_post_anonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(post(URL_PARTS_DELETE_INVALID_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_URL_PATTERN_LOGIN));
    }

    @Test(expected = Exception.class)
    @WithMockUser
    public void delete_post_authenticatedUserInvalidData_throwsException() throws Exception {
        mockMvc.perform(post(URL_PARTS_DELETE_INVALID_ID));
    }

    @Test
    @WithMockUser
    public void delete_post_authenticatedUserValidData_deleteSucceeds() throws Exception {
        Supplier supplier = createSupplier(PART_SUPPLIER_NAME);
        Part part = createPart(supplier, PART_NAME, new BigDecimal(PART_PRICE));

        mockMvc.perform(post(URL_PARTS_DELETE + "/" + part.getId()));

        Optional<Part> deleted = partRepository.findById(part.getId());
        assertFalse(deleted.isPresent());
    }

    @Test
    @WithMockUser
    public void delete_post_authenticatedUserValidData_returnsCorrectViewAndStatus() throws Exception {
        Supplier supplier = createSupplier(PART_SUPPLIER_NAME);
        Part part = createPart(supplier, PART_NAME, new BigDecimal(PART_PRICE));

        mockMvc.perform(post(URL_PARTS_DELETE + "/" + part.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_PARTS_ALL));
    }

    @Test
    @WithAnonymousUser
    public void all_get_anonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get(URL_PARTS_ALL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_URL_PATTERN_LOGIN));
    }

    @Test
    @WithMockUser
    public void all_get_authenticatedUser_returnsCorrectViewAndStatus() throws Exception {
        Supplier supplier = createSupplier(PART_SUPPLIER_NAME);
        createPart(supplier, PART_NAME, new BigDecimal(PART_PRICE));

        mockMvc.perform(get(URL_PARTS_ALL))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_ALL_PARTS))
                .andExpect(model().attributeExists(ATTRIBUTE_PARTS))
                .andExpect(model().attribute(ATTRIBUTE_PARTS, Matchers.hasSize(equalTo(1))));
    }

    @Test
    @WithAnonymousUser
    public void fetch_get_anonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get(URL_PARTS_FETCH))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_URL_PATTERN_LOGIN));
    }

    @Test
    @WithMockUser
    public void fetch_get_authenticatedUser_returnsCorrectTypeAndStatus() throws Exception {
        mockMvc.perform(get(URL_PARTS_FETCH))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    @Test
    @WithMockUser
    public void fetch_get_authenticatedUser_returnsCorrectData() throws Exception {
        Supplier supplier = createSupplier(PART_SUPPLIER_NAME);
        createPart(supplier, PART_NAME, new BigDecimal(PART_PRICE));

        List<Part> parts = partRepository.findAll();

        mockMvc.perform(get(URL_PARTS_FETCH))
                .andExpect(jsonPath("$", hasSize(parts.size())))
                .andExpect(jsonPath("$[0].id", is(parts.get(0).getId())))
                .andExpect(jsonPath("$[0].name", is(parts.get(0).getName())))
                .andExpect(jsonPath("$[0].price", is(parts.get(0).getPrice().doubleValue())))
                .andExpect(jsonPath("$[0].supplier.name", is(parts.get(0).getSupplier().getName())));
    }
}
