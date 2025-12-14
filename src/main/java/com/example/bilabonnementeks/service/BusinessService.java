package com.example.bilabonnementeks.service;

import com.example.bilabonnementeks.model.Car;
import com.example.bilabonnementeks.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessService {

private final CarRepository carRepository;

public BusinessService(CarRepository carRepository){
    this.carRepository=carRepository;
}

public int countActiveCars(){
    return carRepository.findByActiveStatus(true).size();
}

public int countInactiveCars(){
        return carRepository.findByActiveStatus(false).size();
    }

public int totalActiveRevenue(){
    return carRepository.findByActiveStatus(true)
            .stream()
            .mapToInt(Car::getCarRentPrice)
            .sum();
}

public List<Car> getAllCars() {
    return carRepository.findAll();
}

}
