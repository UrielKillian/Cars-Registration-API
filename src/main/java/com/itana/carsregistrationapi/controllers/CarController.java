package com.itana.carsregistrationapi.controllers;

import com.itana.carsregistrationapi.domain.models.Car;
import com.itana.carsregistrationapi.domain.services.CarService;
import com.itana.carsregistrationapi.resources.CarResource;
import com.itana.carsregistrationapi.resources.SaveCarResource;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Transactional
@Tag(name = "cars", description = "Cars API")
public class CarController {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CarService carService;

    @GetMapping("/cars")
    public Page<CarResource>getAllCars(@Parameter(description = "Pageable Parameter") Pageable pageable){
        Page<Car> carPage = carService.getAllCars(pageable);
        List<CarResource> resources = carPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<CarResource>(resources, pageable, resources.size());
    }

    @PostMapping("/cars")
    public CarResource createCar(@PathVariable(name = "carId") Long carId, @Valid @RequestBody SaveCarResource resource) {
        return convertToResource(carService.createCar(carId, convertToEntity(resource)));
    }

    private Car convertToEntity(SaveCarResource resource) { return mapper.map(resource, Car.class);}
    private CarResource convertToResource(Car entity) { return mapper.map(entity, CarResource.class);}
}