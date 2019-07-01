package org.softuni.cardealer.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.entities.Car;
import org.softuni.cardealer.domain.entities.CarSale;
import org.softuni.cardealer.domain.entities.PartSale;
import org.softuni.cardealer.domain.models.service.CarSaleServiceModel;
import org.softuni.cardealer.domain.models.service.CarServiceModel;
import org.softuni.cardealer.domain.models.service.PartSaleServiceModel;
import org.softuni.cardealer.repository.CarRepository;
import org.softuni.cardealer.repository.CarSaleRepository;
import org.softuni.cardealer.repository.PartSaleRepository;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class SaleServiceTest {

    @Mock
    private CarSaleRepository carSaleRepository;

    @Mock
    private PartSaleRepository partSaleRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SaleServiceImpl service;

    @Before
    public void setUp() {
        when(modelMapper.map(eq(null), any())).thenThrow(IllegalArgumentException.class);
    }

    @Test
    public void saleCar() {
        CarSale car = mock(CarSale.class);
        CarSaleServiceModel model = mock(CarSaleServiceModel.class);
        when(modelMapper.map(model, CarSale.class)).thenReturn(car);
        when(carSaleRepository.saveAndFlush(car)).thenReturn(car);
        when(modelMapper.map(car, CarSaleServiceModel.class)).thenReturn(model);

        CarSaleServiceModel result = service.saleCar(model);

        verify(modelMapper).map(model, CarSale.class);
        verify(carSaleRepository).saveAndFlush(car);
        verify(modelMapper).map(car, CarSaleServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saleCar_withNullParameter_throwsIllegalArgumentException() {
        service.saleCar(null);
    }

    @Test
    public void salePart() {
        PartSale car = mock(PartSale.class);
        PartSaleServiceModel model = mock(PartSaleServiceModel.class);
        when(modelMapper.map(model, PartSale.class)).thenReturn(car);
        when(partSaleRepository.saveAndFlush(car)).thenReturn(car);
        when(modelMapper.map(car, PartSaleServiceModel.class)).thenReturn(model);

        PartSaleServiceModel result = service.salePart(model);

        verify(modelMapper).map(model, PartSale.class);
        verify(partSaleRepository).saveAndFlush(car);
        verify(modelMapper).map(car, PartSaleServiceModel.class);
        assertEquals(model, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void partCar_withNullParameter_throwsIllegalArgumentException() {
        service.salePart(null);
    }
}