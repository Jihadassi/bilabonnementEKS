package com.example.bilabonnementeks.controller;


import com.example.bilabonnementeks.model.RentalContract;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dr")
public class DRController {

    @GetMapping("/menu")
    public String showMenu(){
        return "DR/DRMenu";
    }

    @GetMapping("/contracts")
    public String listContracts(Model model) {
        model.addAttribute("contracts");
        model.addAttribute("homeUrl", "/dr/menu");
        return "DR/DRContracts";
    }

    @GetMapping("/new/contract")
    public String createContract(Model model) {
        model.addAttribute("cars");
        model.addAttribute("rentalContract");
        model.addAttribute("homeUrl", "/dr/menu");
        return "DR/DRNewContract";
    }

    @PostMapping("/contracts")
    public String saveContract(){
        return "redirect:/dr/contracts";
    }

    @GetMapping("/contract/info")
    public String contractInfo(Model model){
        model.addAttribute("contract");
        model.addAttribute("customer");
        model.addAttribute("car");
        model.addAttribute("homeUrl", "/dr/contracts");
        return "DR/DRContractInfo";
    }


    @GetMapping("/cars")
    public String showCars(Model model){
        model.addAttribute("cars");
        model.addAttribute("homeUrl", "/dr/menu");
        return "DR/DRCars";
    }

    @GetMapping("/customers")
    public String listCustomers(Model model) {
        model.addAttribute("customers");
        model.addAttribute("homeUrl", "/dr/menu");
        return "DR/DR.customers";
    }

}




