package com.example.bilabonnementeks.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.bilabonnementeks.model.Customer;
import com.example.bilabonnementeks.model.User;
import com.example.bilabonnementeks.repository.CarRepository;
import com.example.bilabonnementeks.service.CustomerService;
import com.example.bilabonnementeks.service.RentalContractService;
import com.example.bilabonnementeks.model.Car;
import com.example.bilabonnementeks.model.RentalContract;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public String listContracts(
            @RequestParam(defaultValue = "all") String status,
            Model model) {

        var contractInfo = rentalContractService.getContractsByStatus(status);
        var views = new ArrayList<RentalContract.ContractView>();

        for (RentalContract rc : contractInfo) {
            RentalContract.ContractView v = new RentalContract.ContractView();
            v.contractId = rc.getContractId();
            v.startDate = rc.getStartDate() != null ? rc.getStartDate().toString() : "";
            v.endDate = rc.getEndDate() != null ? rc.getEndDate().toString() : "";

            // Bil
            Car car = carRepository.findById(rc.getCarId());
            if (car != null) {
                v.carBrand = car.getCarBrand();
                v.carModel = car.getCarModel();
            } else {
                v.carBrand = "Ukendt";
                v.carModel = "";
            }

            // Kunde (aktiv / inaktiv)
            if (rc.getCustomerId() != null) {
                Customer customer = customerService.findById(rc.getCustomerId());
                v.customerName = customer != null ? customer.getFullName() : "Ukendt";
            } else {
                v.customerName = "— Afsluttet —";
            }

            views.add(v);
        }

        model.addAttribute("contracts", views);
        model.addAttribute("currentStatus", status);
        model.addAttribute("homeUrl", "/dr/menu");

        return "dr/DRContracts";
    }

    @GetMapping("/customers/new")
    public String showCreateCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("homeUrl", "/dr/customers");
        return "dr/DRNewCustomer";
    }

    @GetMapping("/new/contract")
    public String createContract(Model model) {
        model.addAttribute("cars", carRepository.findAll());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("rentalContract", new RentalContract());
        model.addAttribute("homeUrl", "/dr/menu");
        return "dr/DRNewContract";
    }

    @PostMapping("/contracts")
    public String saveContract(@ModelAttribute RentalContract rentalContract){

        Car selectedCar = carRepository.findById(rentalContract.getCarId());

        rentalContract.setCarRentPrice(selectedCar.getCarRentPrice());
        rentalContract.setCurrentKm(selectedCar.getCurrentKm());

        rentalContractService.createContract(rentalContract);

        carRepository.setActiveStatus(
                rentalContract.getCarId(),
                true
        );

        return "redirect:/dr/contracts";
    }

    @GetMapping("/contract/info/{id}")
    public String showContractInfo(@PathVariable int id, Model model) {
        RentalContract rc = rentalContractService.getContractById(id);
        if (rc == null) {
            return "redirect:/dr/contracts";
        }

        Car car = carRepository.findById(rc.getCarId());

        Customer customer = null;
        if (rc.getCustomerId() != null) {
            customer = customerService.findById(rc.getCustomerId());
        }

        model.addAttribute("contract", rc);
        model.addAttribute("car", car);
        model.addAttribute("customer", customer);
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

    @PostMapping("/customers")
    public String saveCustomer(@ModelAttribute Customer customer) {
        customerService.createCustomer(customer);
        return "redirect:/dr/customers";
    }

    @GetMapping("/customers")
    public String listCustomers(
            @RequestParam(defaultValue = "name_asc") String sort,
            Model model) {

        List<Customer> customers;

        switch (sort) {
            case "name_desc":
                customers = customerService.getAllCustomersOrderBy("full_name DESC");
                break;
            case "newest":
                customers = customerService.getAllCustomersOrderBy("customer_id DESC");
                break;
            case "oldest":
                customers = customerService.getAllCustomersOrderBy("customer_id ASC");
                break;
            default:
                customers = customerService.getAllCustomersOrderBy("full_name ASC");
        }

        model.addAttribute("customers", customers);
        model.addAttribute("currentSort", sort);
        model.addAttribute("homeUrl", "/dr/menu");
        return "dr/DRCustomers";
    }


    @GetMapping("/cars/delete/{id}")
    public String deleteCar(@PathVariable int id) {
        carRepository.delete(id);
        return "redirect:/dr/cars";
    }

    @PostMapping("/contracts/close/{id}")
    public String closeContract(@PathVariable int id) {

        RentalContract contract = rentalContractService.getContractById(id);
        if (contract == null) {
            return "redirect:/dr/contracts";
        }

        int carId = contract.getCarId();

        rentalContractService.closeContract(id);

        carRepository.setActiveStatus(carId, false);

        return "redirect:/dr/contracts";
    }


    @PostMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable int id,
                                 RedirectAttributes redirectAttributes) {

        if (rentalContractService.customerHasActiveContracts(id)) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Kunden kan ikke slettes, da der findes en aktiv kontrakt."
            );
            return "redirect:/dr/customers";
        }

        customerService.deleteCustomer(id);
        return "redirect:/dr/customers";
    }
}




