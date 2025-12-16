package com.example.bilabonnementeks.repository;

import com.example.bilabonnementeks.model.RentalContract;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RentalContractRepository {


    private final JdbcTemplate jdbcTemplate;


    public RentalContractRepository(JdbcTemplate jdbc, JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    // Mapper en række fra databasen til et RentalContract-objekt
    private RentalContract mapRow(java.sql.ResultSet rs) throws java.sql.SQLException {
        RentalContract rc = new RentalContract();
        rc.setContractId(rs.getInt("contract_id"));
        rc.setStartDate(rs.getDate("start_date"));
        rc.setEndDate(rs.getDate("end_date"));
        rc.setCurrentKm(rs.getInt("current_km"));
        rc.setIncludedKm(rs.getInt("included_km"));
        rc.setCarRentPrice(rs.getInt("car_rent_price"));
        rc.setPickupLocation(rs.getString("pickup_location"));
        rc.setDropoffLocation(rs.getString("dropoff_location"));
        rc.setCarId(rs.getInt("car_id"));
        rc.setUserId(rs.getInt("user_id"));
        rc.setCustomerId(rs.getInt("customer_id"));
        return rc;
    }
    // finder alle kontrakter efter ID
    public List<RentalContract> findAll() {
        String sql = "SELECT * FROM rentalContracts ORDER BY contract_id DESC";
        return jdbcTemplate.query(sql, (rs, row) -> mapRow(rs));
    }
    //finder en kontrakt ud fra ID
    public RentalContract findById(int id) {
        String sql = "SELECT * FROM rentalContracts WHERE contract_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, row) -> mapRow(rs));
    }
    // finder en kunde ud far ID
    public List<RentalContract> findCustomerId(int customerId) {
        String sql = "SELECT * FROM rentalContracts WHERE customer_id = ?";
        return jdbcTemplate.query(sql, new Object[]{customerId}, (rs, row) -> mapRow(rs));
    }
    // henter alle aktive kontrakter
    public List<RentalContract> findActiveContracts() {
        String sql = """
        SELECT * FROM rentalContracts
        WHERE customer_id IS NOT NULL
        ORDER BY contract_id DESC
    """;
        return jdbcTemplate.query(sql, (rs, row) -> mapRow(rs));
    }
    // henter alle inaktive kontrakter
    public List<RentalContract> findInactiveContracts() {
        String sql = """
        SELECT * FROM rentalContracts
        WHERE customer_id IS NULL
        ORDER BY contract_id DESC
    """;
        return jdbcTemplate.query(sql, (rs, row) -> mapRow(rs));
    }

   // opretter en lejekontrakt
    public void create(RentalContract rc) {
        String sql = """
            INSERT INTO rentalContracts
            (start_date, end_date, current_km, included_km, car_rent_price,
             pickup_location, dropoff_location, car_id, user_id, customer_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql,
                rc.getStartDate(),
                rc.getEndDate(),
                rc.getCurrentKm(),
                rc.getIncludedKm(),
                rc.getCarRentPrice(),
                rc.getPickupLocation(),
                rc.getDropoffLocation(),
                rc.getCarId(),
                rc.getUserId(),
                rc.getCustomerId()
        );
    }
    // lukker en kontrakt ved at fjerne tilknytningen til kunden
    public void closeContract(int contractId) {
        String sql = """
        UPDATE rentalContracts
        SET customer_id = NULL
        WHERE contract_id = ?
    """;
        jdbcTemplate.update(sql, contractId);
    }
    // tjekker om en kunde har aktive kontrakter
    public boolean customerHasActiveContracts(int customerId) {
        String sql = """
        SELECT COUNT(*) 
        FROM rentalContracts 
        WHERE customer_id = ?
    """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, customerId);
        return count != null && count > 0;
    }


}
