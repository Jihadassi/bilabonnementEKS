package com.example.bilabonnementeks.controller;

import com.example.bilabonnementeks.repository.CarRepository;
import com.example.bilabonnementeks.service.DamageReportService;
import com.example.bilabonnementeks.model.Car;
import com.example.bilabonnementeks.model.DamageReport;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SBController {

    private final DamageReportService damageReportService;
    private final CarRepository carRepository;

    public SBController(DamageReportService damageReportService, CarRepository carRepository) {
        this.damageReportService = damageReportService;
        this.carRepository = carRepository;
    }
    // Navigation til skaderapport siden
    @GetMapping("/sb/reports")
    public String listReports(Model model) {
        model.addAttribute("reports", damageReportService.getAllReports());
        model.addAttribute("homeUrl", "/sb/reports");
        return "Skadebehandler/SBReports";
    }
    // Navigation til siden med formular til ny skaderapport med aktive biler
    @GetMapping("/sb/new/report")
    public String createReport(Model model) {
        System.out.println("Viser SB new report form");
        List<Car> activeCars = carRepository.findAll().stream()
                .filter(Car::isActiveStatus)
                .collect(Collectors.toList());

        model.addAttribute("cars", activeCars);
        model.addAttribute("damageReport", new DamageReport());
        model.addAttribute("homeUrl", "/sb/reports");
        return "Skadebehandler/SBNewReport";
    }
    //Gemmer en y skaderapport og marker bilen som inaktiv
    @PostMapping("/sb/reports")
    public String saveReport(@ModelAttribute DamageReport dr) {
        damageReportService.createReport(dr);
        carRepository.setActiveStatus(dr.getCarId(), false);
        return "redirect:/sb/reports";
    }
    //Henter en rapport efter ID
    @GetMapping("/sb/report/{id}")
    public String showReportInfo(@PathVariable("id") int id, Model model) {
        DamageReport dr = damageReportService.getReportById(id);
        model.addAttribute("report", dr);
        model.addAttribute("homeUrl", "/sb/reports");
        return "Skadebehandler/SBReportInfo";
    }
    // Sletter en rapport med en bestemt ID
    @GetMapping("/sb/report/delete/{id}")
    public String deleteReport(@PathVariable int id) {
        damageReportService.deleteReport(id);
        return "redirect:/sb/reports";
    }
}
