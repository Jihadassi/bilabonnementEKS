package com.example.bilabonnementeks.service;

import com.example.bilabonnementeks.repository.CarRepository;
import com.example.bilabonnementeks.repository.RentalContractRepository;
import com.example.bilabonnementeks.model.Car;
import com.example.bilabonnementeks.model.RentalContract;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalContractService {

    private final RentalContractRepository rentalContractRepository;
    private final CarRepository carRepository;

    public RentalContractService(RentalContractRepository rentalContractRepository, CarRepository carRepository){
        this.rentalContractRepository = rentalContractRepository;
        this.carRepository = carRepository;
    }

    public List<RentalContract> getAllContracts() {
        return rentalContractRepository.findAll();
    }

    public RentalContract getContractById(int id) {
        return rentalContractRepository.findById(id);
    }

    public void createContract(RentalContract rc) {

        Car selectedCar = carRepository.findById(rc.getCarId());
        rc.setCurrentKm(selectedCar.getCurrentKm());

        rentalContractRepository.create(rc);
        carRepository.setActiveStatus(rc.getCarId(), true);
    }




}
