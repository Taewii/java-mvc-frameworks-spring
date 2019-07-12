package org.softuni.cardealer.web.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Car;
import org.softuni.cardealer.domain.entities.Part;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.repository.CarRepository;
import org.softuni.cardealer.repository.PartRepository;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CarsControllerTest {

    private static final String REDIRECT_PATTERN_USERS_LOGIN = "**/users/login";
    private static final String ALL_CARS_VIEW = "all-cars";

    private static final String URL_CARS_BASE = "/cars";
    private static final String URL_CARS_ADD = URL_CARS_BASE + "/add";
    private static final String URL_CARS_ALL = URL_CARS_BASE + "/all";
    private static final String URL_CARS_EDIT = URL_CARS_BASE + "/edit";
    private static final String URL_CARS_DELETE = URL_CARS_BASE + "/delete";

    private static final String CARS_ATTRIBUTE = "cars";

    private static final String PARAM_MAKE = "make";
    private static final String PARAM_MODEL = "model";
    private static final String PARAM_TRAVELLED_DISTANCE = "travelledDistance";
    private static final String PARAM_PARTS = "parts";

    private static final String MAKE = "make";
    private static final String MODEL = "model";
    private static final String TRAVELLED_DISTANCE = "100";
    private static final String SUPPLIER = "supplier";
    private static final String PART = "part";
    private static final String EMPTY = "";

    private static final String EDITED_MAKE = "editedMake";
    private static final String EDITED_MODEL = "editedModel";
    private static final String EDITED_TRAVELLED_DISTANCE = "999";

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private MockMvc mockMvc;

    private Supplier supplier;
    private Part part;
    private MockHttpServletRequestBuilder postCarValid;

    @Before
    public void setUp() {
        carRepository.deleteAll();
        partRepository.deleteAll();

        supplier = createSupplier();
        part = createPart();

        postCarValid = post(URL_CARS_ADD)
                .param(PARAM_MAKE, MAKE)
                .param(PARAM_MODEL, MODEL)
                .param(PARAM_TRAVELLED_DISTANCE, TRAVELLED_DISTANCE)
                .param(PARAM_PARTS, part.getId());
    }

    private Supplier createSupplier() {
        Supplier supplier = new Supplier();
        supplier.setName(SUPPLIER);

        return supplierRepository.save(supplier);
    }

    private Part createPart() {
        Part part = new Part();
        part.setName(PART);
        part.setPrice(BigDecimal.ONE);
        part.setSupplier(supplier);

        return partRepository.save(part);
    }

    private Car createCar() {
        Car car = new Car();
        car.setMake(MAKE);
        car.setModel(MODEL);
        car.setTravelledDistance(Long.parseLong(TRAVELLED_DISTANCE));
        car.setParts(List.of(part));

        return carRepository.save(car);
    }

    @Test
    @WithAnonymousUser
    public void add_post_anonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(postCarValid)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_PATTERN_USERS_LOGIN));
    }

    @Test
    @WithMockUser
    public void add_post_authenticatedUserValidData_returnsCorrectStatusCodeAndRedirectsCorrectly() throws Exception {
        // should be /cars/all instead of just /all
        mockMvc.perform(postCarValid)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_CARS_ALL));
    }

    @Test
    @WithMockUser
    public void add_post_authenticatedUserValidData_addsCarToDatabase() throws Exception {
        mockMvc.perform(postCarValid);
        Car car = carRepository.findAll().get(0);

        assertEquals(1, carRepository.count());
        assertEquals(MAKE, car.getMake());
        assertEquals(MODEL, car.getModel());
        assertEquals(Long.valueOf(100L), car.getTravelledDistance());
        assertEquals(1, car.getParts().size());
        assertEquals(part.getId(), car.getParts().get(0).getId());
    }

    @Test
    @WithMockUser
    public void add_post_authenticatedUserDuplicateCars_createsCarsSuccessfully() throws Exception {
        mockMvc.perform(postCarValid);
        mockMvc.perform(postCarValid);

        assertEquals(2, carRepository.count());
    }

    @Test(expected = Exception.class)
    @WithMockUser
    public void add_post_authenticatedUserEmptyFields_throwsException() throws Exception {
        mockMvc.perform(post(URL_CARS_ADD)
                .param(PARAM_MAKE, EMPTY)
                .param(PARAM_MODEL, EMPTY)
                .param(PARAM_TRAVELLED_DISTANCE, EMPTY)
                .param(PARAM_PARTS, EMPTY));
    }

    @Test
    @WithAnonymousUser
    public void edit_post_anonymousUser_redirectsToLoginPage() throws Exception {
        Car car = createCar();
        mockMvc.perform(post(URL_CARS_EDIT + "/" + car.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_PATTERN_USERS_LOGIN));
    }

    @Test(expected = Exception.class)
    @WithMockUser
    public void edit_post_authenticatedUserInvalidId_throwsException() throws Exception {
        mockMvc.perform(post(URL_CARS_EDIT + "/" + "invalid"));
    }

    @Test
    @WithMockUser
    public void edit_post_authenticatedUserValidData_editsCorrectly() throws Exception {
        Car car = createCar();

        mockMvc.perform(post(URL_CARS_EDIT + "/" + car.getId())
                .param(PARAM_MAKE, EDITED_MAKE)
                .param(PARAM_MODEL, EDITED_MODEL)
                .param(PARAM_TRAVELLED_DISTANCE, EDITED_TRAVELLED_DISTANCE)
                .param(PARAM_PARTS, part.getId()));

        Car editedCar = carRepository.findById(car.getId()).orElseThrow();

        assertEquals(1, carRepository.count());
        assertEquals(EDITED_MAKE, editedCar.getMake());
        assertEquals(EDITED_MODEL, editedCar.getModel());
        assertEquals(Long.valueOf(EDITED_TRAVELLED_DISTANCE), editedCar.getTravelledDistance());
        assertEquals(1, editedCar.getParts().size());
        assertEquals(part.getId(), editedCar.getParts().get(0).getId());
    }

    @Test
    @WithMockUser
    public void edit_post_authenticatedUserValidData_returnsCorrectStatusCodeAndRedirectsCorrectly() throws Exception {
        Car car = createCar();

        mockMvc.perform(post(URL_CARS_EDIT + "/" + car.getId())
                .param(PARAM_MAKE, EDITED_MAKE)
                .param(PARAM_MODEL, EDITED_MODEL)
                .param(PARAM_TRAVELLED_DISTANCE, EDITED_TRAVELLED_DISTANCE)
                .param(PARAM_PARTS, part.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_CARS_ALL));

    }

    @Test
    @WithAnonymousUser
    public void delete_post_anonymousUser_redirectsToLoginPage() throws Exception {
        Car car = createCar();
        mockMvc.perform(post(URL_CARS_DELETE + "/" + car.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_PATTERN_USERS_LOGIN));
    }

    @Test(expected = Exception.class)
    @WithMockUser
    public void delete_post_authenticatedUserInvalidId_throwsException() throws Exception {
        mockMvc.perform(post(URL_CARS_DELETE + "/" + "invalid"));
    }

    @Test
    @WithMockUser
    public void delete_post_authenticatedUserValidData_deletesCorrectly() throws Exception {
        Car car = createCar();

        mockMvc.perform(post(URL_CARS_DELETE + "/" + car.getId()));
        assertFalse(carRepository.findById(car.getId()).isPresent());
        assertEquals(0, carRepository.count());
    }

    @Test
    @WithMockUser
    public void delete_post_authenticatedUserValidData_returnsCorrectStatusCodeAndRedirectsCorrectly() throws Exception {
        Car car = createCar();

        mockMvc.perform(post(URL_CARS_DELETE + "/" + car.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(URL_CARS_ALL));
    }

    @Test
    @WithAnonymousUser
    public void all_get_anonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get(URL_CARS_ALL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(REDIRECT_PATTERN_USERS_LOGIN));
    }

    @Test
    @WithMockUser
    public void all_get_authenticatedUser_returnsCorrectAttributesViewAndStatusCode() throws Exception {
        createCar();

        mockMvc.perform(get(URL_CARS_ALL))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name(ALL_CARS_VIEW))
                .andExpect(model().attributeExists(CARS_ATTRIBUTE))
                .andExpect(model().attribute(CARS_ATTRIBUTE, hasSize(1)));
    }
}