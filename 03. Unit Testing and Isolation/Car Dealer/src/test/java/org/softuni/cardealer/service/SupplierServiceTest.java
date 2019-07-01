package org.softuni.cardealer.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.domain.models.service.SupplierServiceModel;
import org.softuni.cardealer.domain.models.service.SupplierServiceModel;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class SupplierServiceTest {

    @Mock
    private SupplierRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SupplierServiceImpl service;

    @Before
    public void setUp() {
        when(modelMapper.map(eq(null), any())).thenThrow(new IllegalArgumentException());
    }

    @Test
    public void saveSupplier() {
        Supplier supplier = mock(Supplier.class);
        SupplierServiceModel model = mock(SupplierServiceModel.class);
        when(modelMapper.map(model, Supplier.class)).thenReturn(supplier);
        when(repository.saveAndFlush(supplier)).thenReturn(supplier);
        when(modelMapper.map(supplier, SupplierServiceModel.class)).thenReturn(model);

        SupplierServiceModel result = service.saveSupplier(model);

        verify(modelMapper).map(model, Supplier.class);
        verify(repository).saveAndFlush(supplier);
        verify(modelMapper).map(supplier, SupplierServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveSupplier_withNullParameter_throwsIllegalArgumentException() {
        service.saveSupplier(null);
    }

    @Test
    public void editSupplier() {
        Supplier supplier = mock(Supplier.class);
        SupplierServiceModel model = mock(SupplierServiceModel.class);
        when(model.getId()).thenReturn("id");
        when(model.getName()).thenReturn("name");
        when(model.isImporter()).thenReturn(true);
        when(repository.findById("id")).thenReturn(Optional.of(supplier));
        when(repository.saveAndFlush(supplier)).thenReturn(supplier);
        when(modelMapper.map(supplier, SupplierServiceModel.class)).thenReturn(model);

        SupplierServiceModel result = service.editSupplier(model);

        verify(repository).findById("id");
        verify(supplier).setName("name");
        verify(supplier).setImporter(true);
        verify(repository).saveAndFlush(supplier);
        verify(modelMapper).map(supplier, SupplierServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = NullPointerException.class)
    public void editSupplier_withInvalidId_throwsNullPointerException() {
        SupplierServiceModel model = mock(SupplierServiceModel.class);
        when(model.getId()).thenReturn("id");
        when(repository.findById("id")).thenReturn(Optional.empty());

        service.editSupplier(model);
    }

    @Test(expected = NullPointerException.class)
    public void editSupplier_withNullParameter_throwNullPointerException() {
        service.editSupplier(null);
    }

    @Test
    public void deleteSupplier() {
        Supplier supplier = mock(Supplier.class);
        SupplierServiceModel model = mock(SupplierServiceModel.class);
        when(repository.findById("id")).thenReturn(Optional.of(supplier));
        when(modelMapper.map(supplier, SupplierServiceModel.class)).thenReturn(model);

        SupplierServiceModel result = service.deleteSupplier("id");

        verify(repository).findById("id");
        verify(repository).delete(supplier);
        verify(modelMapper).map(supplier, SupplierServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteSupplier_withInvalidId_throwsIllegalArgumentException() {
        when(repository.findById("id")).thenReturn(Optional.empty());

        this.service.deleteSupplier("id");
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteSupplier_withNullParameter_throwsIllegalArgumentException() {
        when(repository.findById(null)).thenThrow(IllegalArgumentException.class);

        this.service.deleteSupplier(null);
    }

    @Test
    public void findSupplierById() {
        Supplier supplier = mock(Supplier.class);
        SupplierServiceModel model = mock(SupplierServiceModel.class);
        when(repository.findById("id")).thenReturn(Optional.of(supplier));
        when(modelMapper.map(supplier, SupplierServiceModel.class)).thenReturn(model);

        SupplierServiceModel result = service.findSupplierById("id");

        verify(repository).findById("id");
        verify(modelMapper).map(supplier, SupplierServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findSupplierBy_withNonExistingId_throwsIllegalArgumentException() {
        when(repository.findById("id")).thenReturn(Optional.empty());

        service.findSupplierById("id");
    }

    @Test(expected = IllegalArgumentException.class)
    public void findSupplierBy_withNullParameter_throwsIllegalArgumentException() {
        when(repository.findById(null)).thenThrow(IllegalArgumentException.class);

        service.findSupplierById(null);
    }
}