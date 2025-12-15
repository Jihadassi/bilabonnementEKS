package com.example.bilabonnementeks.controller;


import com.example.bilabonnementeks.model.Customer;
import com.example.bilabonnementeks.repository.CarRepository;
import com.example.bilabonnementeks.service.CustomerService;
import com.example.bilabonnementeks.service.RentalContractService;
import com.example.bilabonnementeks.model.Car;
import com.example.bilabonnementeks.model.RentalContract;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/dr")


public class DRController {

    private final RentalContractService rentalContractService;
    private final CarRepository carRepository;
    private final CustomerService customerService;

    public DRController(RentalContractService rentalContractService, CarRepository carRepository, CustomerService customerService) {
        this.rentalContractService = rentalContractService;
        this.carRepository = carRepository;
        this.customerService = customerService;
    }



    @GetMapping("/menu")
    public String showMenu(){
        return "dr/DRMenu";
    }

    @GetMapping("/contracts")
    public String listContracts(Model model) {
        var contractInfo = rentalContractService.getAllContracts();
        var views = new java.util.ArrayList<RentalContract.ContractView>();

        for (RentalContract rc : contractInfo) {
            RentalContract.ContractView v = new RentalContract.ContractView();
            v.contractId = rc.getContractId();
            v.startDate = rc.getStartDate() != null ? rc.getStartDate().toString() : "";
            v.endDate = rc.getEndDate() != null ? rc.getEndDate().toString() : "";

            // Hent bilen
            Car car = carRepository.findById(rc.getCarId());
            if (car != null) {
                v.carBrand = car.getCarBrand();
                v.carModel = car.getCarModel();
            } else {
                v.carBrand = "Ukendt";
                v.carModel = "";
            }

            views.add(v);
        }

        model.addAttribute("contracts", views);
        model.addAttribute("homeUrl", "/dr/menu");
        return "dr/DRContracts";
    }

    @GetMapping("/new/contract")
    public String createContract(Model model) {
        model.addAttribute("cars", carRepository.findAll());
        model.addAttribute("rentalContract", new RentalContract());
        model.addAttribute("homeUrl", "/dr/menu");
        return "dr/DRNewContract";
    }

    @PostMapping("/contracts")
    public String saveContract(@ModelAttribute RentalContract rentalContract){
        Car selectedCar = carRepository.findById(rentalContract.getCarId());
        rentalContract.setCurrentKm(selectedCar.getCurrentKm());

        rentalContractService.createContract(rentalContract);

        return "redirect:/dr/contracts";
    }

    @GetMapping("/contract/info/{id}")
    public String showContractInfo(@PathVariable int id, Model model) {
        RentalContract rc = rentalContractService.getContractById(id);
        Car car = carRepository.findById(rc.getCarId());

        model.addAttribute("contract", rc);
        model.addAttribute("car", car);
        model.addAttribute("homeUrl", "/dr/contracts");
        return "dr/DRContractInfo";
    }


    @GetMapping("/cars")
    public String showCars(Model model){
        model.addAttribute("cars", carRepository.findAll());
        model.addAttribute("homeUrl", "/dr/menu");
        return "dr/DRCars";
    }

    @GetMapping("/cars/new")
    public String addCar(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("homeUrl", "/dr/menu");
        return "dr/DRAddCar";
    }

    @PostMapping("/cars")
    public String saveCar(@ModelAttribute Car car) {
        carRepository.create(car);
        return "redirect:/dr/cars";
    }

    @GetMapping("/customers")
    public String listCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();

        model.addAttribute("customers",customers);
        model.addAttribute("homeUrl", "/dr/menu");
        return "dr/DRCustomers";
    }

    @GetMapping("/cars/delete/{id}")
    public String deleteCar(@PathVariable int id) {
        carRepository.delete(id);
        return "redirect:/dr/cars";
    }
}




