package com.example.bilabonnementeks.service;


import com.example.bilabonnementeks.repository.DamageReportRepository;
import com.example.bilabonnementeks.model.DamageReport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DamageReportService {
    private final DamageReportRepository reportRepository;

    public DamageReportService(DamageReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
// henter alle rapporter
    public List<DamageReport> getAllReports() {
        return reportRepository.findAll();
    }
//opretter en ny rapport
    public void createReport(DamageReport report) {
        reportRepository.create(report);
    }
//henter en rapport ud fra ID
    public DamageReport getReportById(int id) {
        return reportRepository.findById(id);
    }
// sletter en rapport
    public void deleteReport(int id) {
        reportRepository.delete(id);
    }
}
