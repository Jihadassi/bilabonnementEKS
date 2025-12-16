package com.example.bilabonnementeks.controller;

import org.springframework.ui.Model;
import com.example.bilabonnementeks.service.CarService;
import com.example.bilabonnementeks.model.Car;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
public class FUController {

    private final CarService carService;

    public FUController(CarService carService) {
        this.carService = carService;
    }
    // navigation til Forretningsudvikler menuen
    @GetMapping ("/fu/menu")
    public String fuMenu() {
        return "Forretningsudvikler/FUMenu";
    }
    //Henter alle biler til visning
    @GetMapping("/fu/cars")
    public String fuCars(Model model) {
        List<Car> cars = carService.findAll();
        model.addAttribute("cars", cars);

        //Filtrerer aktive og inaktive biler
        long activeCount = cars.stream().filter(Car::isActiveStatus).count();
        long inactiveCount = cars.size() - activeCount;

        // sammenlægger betalinger og giver en samlet betaling
        int activePayment = cars.stream().filter(Car::isActiveStatus).mapToInt(Car::getCarRentPrice).sum();
        //Beregning af samlet pris
        int totalPayment = cars.stream().mapToInt(Car::getCarRentPrice).sum();

        // tilføjer bilstatistikker og viser biloversigten for FU
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("inactiveCount", inactiveCount);
        model.addAttribute("activePayment", activePayment);
        model.addAttribute("totalPayment", totalPayment);
        model.addAttribute("homeUrl", "/fu/menu");


        return "Forretningsudvikler/FUCars";

    }

//    @GetMapping("/fu/statistics")
//    public String fuStatistics(Model model) {
//        List<Car> cars = carService.findAll();
//        model.addAttribute("cars", cars);
//
//
//        //Her bliver der set på månedlig betaling de sidste 36 måneder, og det dannes til en graf
//        int[] monthlyActivePayment = new int[36];
//        int[] monthlyTotalPayment = new int[36];
//
//        for (int i = 0; i < 36; i++) {
//            // For nu: simuler tallene ud fra aktuelle priser
//            monthlyActivePayment[i] = cars.stream()
//                    .filter(Car::isActiveStatus)
//                    .mapToInt(Car::getCarRentPrice)
//                    .sum();
//
//            monthlyTotalPayment[i] = cars.stream()
//                    .mapToInt(Car::getCarRentPrice)
//                    .sum();
//        }
//
//        model.addAttribute("monthlyActivePayment", monthlyActivePayment);
//        model.addAttribute("monthlyTotalPayment", monthlyTotalPayment);
//
//        return "Forretningsudvikler/FUStatistics";
//
//    }
    //Navigation til siden med statistik for FU
    @GetMapping("/fu/statistics")
    public String showStatistics(Model model, @RequestParam(required = false, defaultValue = "2025") int year) {
        int[] rentalsPerMonth = {5, 7, 3, 8, 6, 4, 9, 2, 7, 5, 6, 4};
        model.addAttribute("year", year);
        model.addAttribute("rentalsPerMonth", rentalsPerMonth);
        model.addAttribute("months", new String[]{"Jan","Feb","Mar","Apr","Maj","Jun","Jul","Aug","Sep","Okt","Nov","Dec"});
        model.addAttribute("years", new int[]{2023,2024,2025});
        model.addAttribute("homeUrl", "/fu/menu");

        return "Forretningsudvikler/FUStatistics";
    }




}
