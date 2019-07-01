package org.softuni.cardealer.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.entities.Part;
import org.softuni.cardealer.domain.models.service.PartServiceModel;
import org.softuni.cardealer.domain.models.service.PartServiceModel;
import org.softuni.cardealer.repository.PartRepository;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class PartServiceTest {

    @Mock
    private PartRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PartServiceImpl service;

    @Before
    public void setUp() {
        when(modelMapper.map(eq(null), any())).thenThrow(new IllegalArgumentException());
    }

    @Test
    public void savePart() {
        Part part = mock(Part.class);
        PartServiceModel model = mock(PartServiceModel.class);
        when(modelMapper.map(model, Part.class)).thenReturn(part);
        when(repository.saveAndFlush(part)).thenReturn(part);
        when(modelMapper.map(part, PartServiceModel.class)).thenReturn(model);

        PartServiceModel result = service.savePart(model);

        verify(modelMapper).map(model, Part.class);
        verify(repository).saveAndFlush(part);
        verify(modelMapper).map(part, PartServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void savePart_withNullParameter_throwsIllegalArgumentException() {
        service.savePart(null);
    }

    @Test
    public void editPart() {
        Part part = mock(Part.class);
        PartServiceModel model = mock(PartServiceModel.class);
        when(model.getId()).thenReturn("id");
        when(model.getName()).thenReturn("name");
        when(model.getPrice()).thenReturn(BigDecimal.TEN);
        when(repository.findById("id")).thenReturn(Optional.of(part));
        when(repository.saveAndFlush(part)).thenReturn(part);
        when(modelMapper.map(part, PartServiceModel.class)).thenReturn(model);

        PartServiceModel result = service.editPart(model);

        verify(repository).findById("id");
        verify(part).setName("name");
        verify(part).setPrice(BigDecimal.TEN);
        verify(repository).saveAndFlush(part);
        verify(modelMapper).map(part, PartServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = NullPointerException.class)
    public void editPart_withInvalidId_throwsNullPointerException() {
        PartServiceModel model = mock(PartServiceModel.class);
        when(model.getId()).thenReturn("id");
        when(repository.findById("id")).thenReturn(Optional.empty());

        service.editPart(model);
    }

    @Test(expected = NullPointerException.class)
    public void editPart_withNullParameter_throwNullPointerException() {
        service.editPart(null);
    }

    @Test
    public void deletePart() {
        Part part = mock(Part.class);
        PartServiceModel model = mock(PartServiceModel.class);
        when(repository.findById("id")).thenReturn(Optional.of(part));
        when(modelMapper.map(part, PartServiceModel.class)).thenReturn(model);

        PartServiceModel result = service.deletePart("id");

        verify(repository).findById("id");
        verify(repository).delete(part);
        verify(modelMapper).map(part, PartServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = NoSuchElementException.class)
    public void deletePart_withInvalidId_throwsNoSuchElementException() {
        when(repository.findById("id")).thenReturn(Optional.empty());

        this.service.deletePart("id");
    }

    @Test(expected = IllegalArgumentException.class)
    public void deletePart_withNullParameter_throwsIllegalArgumentException() {
        when(repository.findById(null)).thenThrow(IllegalArgumentException.class);

        this.service.deletePart(null);
    }

    @Test
    public void findPartById() {
        Part part = mock(Part.class);
        PartServiceModel model = mock(PartServiceModel.class);
        when(repository.findById("id")).thenReturn(Optional.of(part));
        when(modelMapper.map(part, PartServiceModel.class)).thenReturn(model);

        PartServiceModel result = service.findPartById("id");

        verify(repository).findById("id");
        verify(modelMapper).map(part, PartServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findPartBy_withNonExistingId_throwsIllegalArgumentException() {
        when(repository.findById("id")).thenReturn(Optional.empty());

        service.findPartById("id");
    }

    @Test(expected = IllegalArgumentException.class)
    public void findPartBy_withNullParameter_throwsIllegalArgumentException() {
        when(repository.findById(null)).thenThrow(IllegalArgumentException.class);

        service.findPartById(null);
    }
}