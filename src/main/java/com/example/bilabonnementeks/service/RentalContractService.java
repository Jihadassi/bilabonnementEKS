package com.example.bilabonnementeks.service;

import com.example.bilabonnementeks.model.Car;
import com.example.bilabonnementeks.repository.CarRepository;
import com.example.bilabonnementeks.repository.RentalContractRepository;
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
// henter alle kontrakter
    public List<RentalContract> getAllContracts() {
        return rentalContractRepository.findAll();
    }
// henter alle kontrakter ud fra ID
    public RentalContract getContractById(int id) {
        return rentalContractRepository.findById(id);
    }

//opretter en ny kontrakt og merker bilen som aktiv
    public void createContract(RentalContract rc) {

        Car selectedCar = carRepository.findById(rc.getCarId());
        rc.setCurrentKm(selectedCar.getCurrentKm());

        rentalContractRepository.create(rc);
        carRepository.setActiveStatus(rc.getCarId(), true);
    }
// bruger repo ttil at tjekke om en kunde har aktive kontrakter
    public boolean customerHasActiveContracts(int customerId) {
        return rentalContractRepository.customerHasActiveContracts(customerId);
    }

//henter kontrakter baseret på status
    public List<RentalContract> getContractsByStatus(String status) {
        return switch (status) {
            case "active" -> rentalContractRepository.findActiveContracts();
            case "inactive" -> rentalContractRepository.findInactiveContracts();
            default -> rentalContractRepository.findAll();
        };
    }
// lukker en kontrakt via repo
    public void closeContract(int contractId) {
        rentalContractRepository.closeContract(contractId);
    }



}
