package com.example.bilabonnementeks.controller;

import org.springframework.ui.Model;
import com.example.bilabonnementeks.service.CarService;
import com.example.bilabonnementeks.model.Car;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;

@Controller
public class FUController {

    private final CarService carService;

    public FUController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping ("/fu/menu")
    public String fuMenu() {
        return "Forretningsudvikler/FUMenu";
    }

    @GetMapping("/fu/cars")
    public String fuCars(Model model) {
        List<Car> cars = carService.findAll();
        model.addAttribute("cars", cars);

        //Filtrerer aktive og inaktive biler
        long activeCount = cars.stream().filter(Car::isActiveStatus).count();
        long inactiveCount = cars.size() - activeCount;

        // sammenlægger betalinger og giver en samlet betaling
        int activePayment = cars.stream()
                .filter(Car::isActiveStatus)
                .mapToInt(Car::getCarRentPrice)
                .sum();

        int totalPayment = cars.stream()
                .mapToInt(Car::getCarRentPrice)
                .sum();

        model.addAttribute("activeCount", activeCount);
        model.addAttribute("inactiveCount", inactiveCount);
        model.addAttribute("activePayment", activePayment);
        model.addAttribute("totalPayment", totalPayment);

        return "Forretningsudvikler/FUCars";

    }

    @GetMapping("/fu/statistics")
    public String fuStatistics(Model model) {
        List<Car> cars = carService.findAll();
        model.addAttribute("cars", cars);


        //Her bliver der set på månedlig betaling de sidste 36 måneder, og det dannes til en graf
        int[] monthlyActivePayment = new int[36];
        int[] monthlyTotalPayment = new int[36];

        for (int i = 0; i < 36; i++) {
            // For nu: simuler tallene ud fra aktuelle priser
            monthlyActivePayment[i] = cars.stream()
                    .filter(Car::isActiveStatus)
                    .mapToInt(Car::getCarRentPrice)
                    .sum();

            monthlyTotalPayment[i] = cars.stream()
                    .mapToInt(Car::getCarRentPrice)
                    .sum();
        }

        model.addAttribute("monthlyActivePayment", monthlyActivePayment);
        model.addAttribute("monthlyTotalPayment", monthlyTotalPayment);

        return "Forretningsudvikler/FUStatistics";

    }

}
