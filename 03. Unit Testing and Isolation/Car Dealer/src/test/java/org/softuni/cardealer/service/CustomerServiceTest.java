package org.softuni.cardealer.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.entities.Customer;
import org.softuni.cardealer.domain.models.service.CustomerServiceModel;
import org.softuni.cardealer.repository.CustomerRepository;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CustomerServiceImpl service;

    @Before
    public void setUp() {
        when(modelMapper.map(eq(null), any())).thenThrow(new IllegalArgumentException());
    }

    @Test
    public void saveCustomer() {
        Customer customer = mock(Customer.class);
        CustomerServiceModel model = mock(CustomerServiceModel.class);
        when(modelMapper.map(model, Customer.class)).thenReturn(customer);
        when(repository.saveAndFlush(customer)).thenReturn(customer);
        when(modelMapper.map(customer, CustomerServiceModel.class)).thenReturn(model);

        CustomerServiceModel result = service.saveCustomer(model);

        verify(modelMapper).map(model, Customer.class);
        verify(repository).saveAndFlush(customer);
        verify(modelMapper).map(customer, CustomerServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveCustomer_withNullParameter_throwsIllegalArgumentException() {
        service.saveCustomer(null);
    }

    @Test
    public void editCustomer() {
        Customer customer = mock(Customer.class);
        CustomerServiceModel model = mock(CustomerServiceModel.class);
        when(model.getId()).thenReturn("id");
        when(model.getName()).thenReturn("name");
        when(model.getBirthDate()).thenReturn(LocalDate.of(2000, 1, 1));
        when(model.isYoungDriver()).thenReturn(true);
        when(repository.findById("id")).thenReturn(Optional.of(customer));
        when(repository.saveAndFlush(customer)).thenReturn(customer);
        when(modelMapper.map(customer, CustomerServiceModel.class)).thenReturn(model);

        CustomerServiceModel result = service.editCustomer(model);

        verify(repository).findById("id");
        verify(customer).setName("name");
        verify(customer).setBirthDate(LocalDate.of(2000, 1, 1));
        verify(customer).setYoungDriver(true);
        verify(repository).saveAndFlush(customer);
        verify(modelMapper).map(customer, CustomerServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = NullPointerException.class)
    public void editCustomer_withInvalidId_throwsNullPointerException() {
        CustomerServiceModel model = mock(CustomerServiceModel.class);
        when(model.getId()).thenReturn("id");
        when(repository.findById("id")).thenReturn(Optional.empty());

        service.editCustomer(model);
    }

    @Test(expected = NullPointerException.class)
    public void editCustomer_withNullParameter_throwNullPointerException() {
        service.editCustomer(null);
    }

    @Test
    public void deleteCustomer() {
        Customer customer = mock(Customer.class);
        CustomerServiceModel model = mock(CustomerServiceModel.class);
        when(repository.findById("id")).thenReturn(Optional.of(customer));
        when(modelMapper.map(customer, CustomerServiceModel.class)).thenReturn(model);

        CustomerServiceModel result = service.deleteCustomer("id");

        verify(repository).findById("id");
        verify(repository).delete(customer);
        verify(modelMapper).map(customer, CustomerServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteCustomer_withInvalidId_throwsIllegalArgumentException() {
        when(repository.findById("id")).thenReturn(Optional.empty());

        this.service.deleteCustomer("id");
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteCustomer_withNullParameter_throwsIllegalArgumentException() {
        when(repository.findById(null)).thenThrow(IllegalArgumentException.class);

        this.service.deleteCustomer(null);
    }

    @Test
    public void findCustomerById() {
        Customer customer = mock(Customer.class);
        CustomerServiceModel model = mock(CustomerServiceModel.class);
        when(repository.findById("id")).thenReturn(Optional.of(customer));
        when(modelMapper.map(customer, CustomerServiceModel.class)).thenReturn(model);

        CustomerServiceModel result = service.findCustomerById("id");

        verify(repository).findById("id");
        verify(modelMapper).map(customer, CustomerServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findCustomerBy_withNonExistingId_throwsIllegalArgumentException() {
        when(repository.findById("id")).thenReturn(Optional.empty());

        service.findCustomerById("id");
    }

    @Test(expected = IllegalArgumentException.class)
    public void findCustomerBy_withNullParameter_throwsIllegalArgumentException() {
        when(repository.findById(null)).thenThrow(IllegalArgumentException.class);

        service.findCustomerById(null);
    }
}