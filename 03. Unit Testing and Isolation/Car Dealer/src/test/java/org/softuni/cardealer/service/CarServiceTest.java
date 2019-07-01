package org.softuni.cardealer.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.entities.Car;
import org.softuni.cardealer.domain.models.service.CarServiceModel;
import org.softuni.cardealer.repository.CarRepository;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {

    @Mock
    private CarRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CarServiceImpl service;

    @Before
    public void setUp() {
        when(modelMapper.map(eq(null), any())).thenThrow(new IllegalArgumentException());
    }

    @Test
    public void saveCar_validInputData_correctMethodsAndArgumentsUsed() {
        Car car = mock(Car.class);
        CarServiceModel model = mock(CarServiceModel.class);
        when(modelMapper.map(model, Car.class)).thenReturn(car);
        when(repository.saveAndFlush(car)).thenReturn(car);
        when(modelMapper.map(car, CarServiceModel.class)).thenReturn(model);

        CarServiceModel result = service.saveCar(model);

        verify(modelMapper).map(model, Car.class);
        verify(repository).saveAndFlush(car);
        verify(modelMapper).map(car, CarServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveCar_withNullParameter_throwsIllegalArgumentException() {
        service.saveCar(null);
    }

    @Test
    public void editCar() {
        Car car = mock(Car.class);
        CarServiceModel model = mock(CarServiceModel.class);
        when(model.getId()).thenReturn("id");
        when(model.getMake()).thenReturn("make");
        when(model.getModel()).thenReturn("model");
        when(model.getTravelledDistance()).thenReturn(1L);
        when(repository.findById(eq("id"))).thenReturn(Optional.of(car));
        when(repository.saveAndFlush(car)).thenReturn(car);
        when(modelMapper.map(car, CarServiceModel.class)).thenReturn(model);

        CarServiceModel result = service.editCar(model);

        verify(repository).findById("id");
        verify(car).setMake("make");
        verify(car).setModel("model");
        verify(car).setTravelledDistance(1L);
        verify(repository).saveAndFlush(car);
        verify(modelMapper).map(car, CarServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = NullPointerException.class)
    public void editCar_withInvalidId_throwsNullPointerException() {
        CarServiceModel model = mock(CarServiceModel.class);
        when(model.getId()).thenReturn("id");
        when(repository.findById("id")).thenReturn(Optional.empty());

        service.editCar(model);
    }

    @Test(expected = NullPointerException.class)
    public void editCar_withNullParameter_throwNullPointerException() {
        service.editCar(null);
    }

    @Test
    public void deleteCar() {
        Car car = mock(Car.class);
        CarServiceModel model = mock(CarServiceModel.class);
        when(repository.findById("id")).thenReturn(Optional.of(car));
        when(modelMapper.map(car, CarServiceModel.class)).thenReturn(model);

        CarServiceModel result = service.deleteCar("id");

        verify(repository).findById("id");
        verify(repository).delete(car);
        verify(modelMapper).map(car, CarServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteCar_withInvalidId_throwsIllegalArgumentException() {
        when(repository.findById("id")).thenReturn(Optional.empty());

        this.service.deleteCar("id");
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteCar_withNullParameter_throwsIllegalArgumentException() {
        when(repository.findById(null)).thenThrow(IllegalArgumentException.class);

        this.service.deleteCar(null);
    }

    @Test
    public void findCarById() {
        Car car = mock(Car.class);
        CarServiceModel model = mock(CarServiceModel.class);
        when(repository.findById("id")).thenReturn(Optional.of(car));
        when(modelMapper.map(car, CarServiceModel.class)).thenReturn(model);

        CarServiceModel result = service.findCarById("id");

        verify(repository).findById("id");
        verify(modelMapper).map(car, CarServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findCarBy_withNonExistingId_throwsIllegalArgumentException() {
        when(repository.findById("id")).thenReturn(Optional.empty());

        service.findCarById("id");
    }

    @Test(expected = IllegalArgumentException.class)
    public void findCarBy_withNullParameter_throwsIllegalArgumentException() {
        when(repository.findById(null)).thenThrow(IllegalArgumentException.class);

        service.findCarById(null);
    }
}