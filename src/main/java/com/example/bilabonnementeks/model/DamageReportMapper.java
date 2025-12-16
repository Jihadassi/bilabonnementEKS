//
//// denne klasse har opgaven til at konvertere rækker fra SQL resultatsæt til Java objekter af typen DamageReport
//// bruges til at olde SQL-laget adskilt fra Java-objekterne
//
//package com.example.bilabonnementeks.model;
//import org.springframework.jdbc.core.RowMapper;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//
//public class DamageReportMapper implements RowMapper<DamageReport> {
//
//    @Override
//    public DamageReport mapRow(ResultSet rs, int rowNum) throws SQLException { // denne metode bliver automatisk kaldt på hver række i en tabel
//        DamageReport report = new DamageReport();
//        report.setDamageId(rs.getInt("damageId"));
//        report.setTextDescription(rs.getString("textDescription"));
//        report.setDamagePrice(rs.getInt("damagePrice"));
//        report.setCarId(rs.getInt("carId"));
//        report.setCustomerId(rs.getInt("customer_Id"));
//        return report;
//    }
//}
