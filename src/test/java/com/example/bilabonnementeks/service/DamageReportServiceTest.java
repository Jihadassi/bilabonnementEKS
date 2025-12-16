package com.example.bilabonnementeks.service;

import com.example.bilabonnementeks.model.DamageReport;
import com.example.bilabonnementeks.repository.DamageReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class DamageReportServiceTest {

    private DamageReportRepository damageReportRepository;
    private DamageReportService damageReportService;

    @BeforeEach
    void setUp() {
        // Mock repository
        damageReportRepository = mock(DamageReportRepository.class);

        // Injektér mock til service
        damageReportService = new DamageReportService(damageReportRepository);
    }

    @Test
    void createReport_shouldCallRepositoryWithCorrectDamageReport() {
        // Arrange: opret testdata
        DamageReport report = new DamageReport();
        report.setTextDescription("Rids på højre dør");
        report.setCarId(5);
        report.setDamagePrice(null); // vi tester kun description og carId her
        report.setCustomerId(null);  // ikke nødvendigt for dette test-case
        // damageId genereres af databasen, så vi sætter den ikke

        // Act: kald service-metoden
        damageReportService.createReport(report);

        // Assert: verificer, at repository.create() blev kaldt med det korrekte objekt
        verify(damageReportRepository).create(report);
    }
}