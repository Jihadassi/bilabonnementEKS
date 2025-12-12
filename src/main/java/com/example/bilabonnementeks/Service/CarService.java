package com.example.bilabonnementeks.Service;

import com.example.bilabonnementeks.model.Car;
import com.example.bilabonnementeks.Repository.CarRepository;
import org.springframework.stereotype.Service;
import java.util.List;


// her ligger logikken for hvordan man arbejder med cars, logikken skal bruges flere steder
@Service
public class CarService {

    private final CarRepository carRepository; // opretter en variabel der gemmer den repository

// Spring giver automatisk et CarRepository-objekt.
//this.carRepository betyder: gem det så vi kan bruge det i metoderne.
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

// gemmer en bil ved at sende den videre til repo
    public void save(Car car) {
        carRepository.create(car);
    }

// henter alle bile ri for af en liste
    public List<Car> findAll() {
        return carRepository.findAll();
    }

// finder 1 bil ud fra ID
    public Car findById(int id) {
        return carRepository.findById(id);
    }

// sletter en bil ud fra ID fra databasen
    public void delete(int id) {
        carRepository.delete(id);
    }

// ændre bilens status
    public void setActiveStatus(int carId, boolean status) {
        carRepository.setActiveStatus(carId, status);
    }


}

