package com.example.bilabonnementeks.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.bilabonnementeks.model.Customer;
import com.example.bilabonnementeks.repository.CarRepository;
import com.example.bilabonnementeks.service.CustomerService;
import com.example.bilabonnementeks.service.RentalContractService;
import com.example.bilabonnementeks.model.Car;
import com.example.bilabonnementeks.model.RentalContract;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;



//Controller for dataregistrator (DR)
@RequestMapping("/dr")
@Controller
public class DRController {

    private final RentalContractService rentalContractService;
    private final CarRepository carRepository;
    private final CustomerService customerService;

    public DRController(RentalContractService rentalContractService, CarRepository carRepository, CustomerService customerService) {
        this.rentalContractService = rentalContractService;
        this.carRepository = carRepository;
        this.customerService = customerService;
    }


    //Navigation til menuen for en dataregistrator
    @GetMapping("/menu")
    public String showMenu(){
        return "dr/DRMenu";
    }

    //Navigation til listen med kontrakter
    @GetMapping("/contracts")
    public String listContracts(
            @RequestParam(defaultValue = "all") String status, Model model) {

        //Kalder service klassen for alle lejeaftaler med den valgte status
        var contractInfo = rentalContractService.getContractsByStatus(status);

        //Liste til ContractView objekter
        var views = new ArrayList<RentalContract.ContractView>();

        //Gennemgår hver lejekontrakt og opretter et view objekt med værdier
        for (RentalContract rc : contractInfo) {
            RentalContract.ContractView v = new RentalContract.ContractView();
            v.contractId = rc.getContractId();
            v.startDate = rc.getStartDate() != null ? rc.getStartDate().toString() : "";
            v.endDate = rc.getEndDate() != null ? rc.getEndDate().toString() : "";

            //Henter bil fra repository
            Car car = carRepository.findById(rc.getCarId());
            if (car != null) {
                v.carBrand = car.getCarBrand();
                v.carModel = car.getCarModel();
            } else {
                v.carBrand = "Ukendt";
                v.carModel = "";
            }

            //Henter kunde og gemmer det til listen af view- objekter
            if (rc.getCustomerId() != null) {
                Customer customer = customerService.findById(rc.getCustomerId());
                v.customerName = customer != null ? customer.getFullName() : "Ukendt";
            } else {
                v.customerName = "— Afsluttet —";
            }

            views.add(v);
        }

        //Sender data til view, hvor Thymeleaf kan vise listen af lejeaftaler mm.
        model.addAttribute("contracts", views);
        model.addAttribute("currentStatus", status);
        model.addAttribute("homeUrl", "/dr/menu");

        return "dr/DRContracts";
    }
//Navigation til siden for oprettelse af nye kunde
    @GetMapping("/customers/new")
    public String showCreateCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("homeUrl", "/dr/customers");
        return "dr/DRNewCustomer";
    }
// Navigation til siden for oprettelsen af nye lejekontrakt
    @GetMapping("/new/contract")
    public String createContract(Model model) {
        model.addAttribute("cars", carRepository.findAll());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("rentalContract", new RentalContract());
        model.addAttribute("homeUrl", "/dr/menu");
        return "dr/DRNewContract";
    }
// Gemmer af nye lejekontrakt
    @PostMapping("/contracts")
    public String saveContract(@ModelAttribute RentalContract rentalContract){

        //Finder bilen i databasen med carId, og selectedCar bruges til at hente bilens aktuelle data
        Car selectedCar = carRepository.findById(rentalContract.getCarId());

        //Sætter lejeprisen + bilens kilometertal, som bilen er oprettet med, på lejeaftalen
        rentalContract.setCarRentPrice(selectedCar.getCarRentPrice());
        rentalContract.setCurrentKm(selectedCar.getCurrentKm());

        rentalContractService.createContract(rentalContract);

        // opdater bilens status
        carRepository.setActiveStatus(rentalContract.getCarId(), true);

        return "redirect:/dr/contracts";
    }
    //henter kontrakt ID, bil ID og kunde ID og sender data til visning
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

    // henter alle biler og sender dem til visning
    @GetMapping("/cars")
    public String showCars(Model model){
        model.addAttribute("cars", carRepository.findAll());
        model.addAttribute("homeUrl", "/dr/menu");
        return "dr/DRCars";
    }
    //Opretter nye bil og viser formular til tilføjelse af bil
    @GetMapping("/cars/new")
    public String addCar(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("homeUrl", "/dr/menu");
        return "dr/DRAddCar";
    }
    //Gemmer en ny bil og videresender til biloversigt
    @PostMapping("/cars")
    public String saveCar(@ModelAttribute Car car) {
        carRepository.create(car);
        return "redirect:/dr/cars";
    }
    //Gemmer en ny kunde og videresender til kundeoversigt
    @PostMapping("/customers")
    public String saveCustomer(@ModelAttribute Customer customer) {
        customerService.createCustomer(customer);
        return "redirect:/dr/customers";
    }
    //Henter og viser listen af kunder med mulighed for sortering
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
        //kunder der opfylder kriterier sendes til visning
        model.addAttribute("customers", customers);
        model.addAttribute("currentSort", sort);
        model.addAttribute("homeUrl", "/dr/menu");
        return "dr/DRCustomers";
    }

    //sletter en bil ud fra id fra repo
    @GetMapping("/cars/delete/{id}")
    public String deleteCar(@PathVariable int id) {
        carRepository.delete(id);
        return "redirect:/dr/cars";
    }
    //afslutter en kontrakt
    @PostMapping("/contracts/close/{id}")
    public String closeContract(@PathVariable int id) {

        RentalContract contract = rentalContractService.getContractById(id);
        if (contract == null) {
            return "redirect:/dr/contracts";
        }

        int carId = contract.getCarId();

        rentalContractService.closeContract(id);
        //marker bilen som inaktiv
        carRepository.setActiveStatus(carId, false);

        return "redirect:/dr/contracts";
    }

    // Sletter en kunde hvis den ikke har aktive kontrakter
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




