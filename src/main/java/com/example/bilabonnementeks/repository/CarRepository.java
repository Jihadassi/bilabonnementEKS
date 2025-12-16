package com.example.bilabonnementeks.repository;

import com.example.bilabonnementeks.model.Car;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


//Fortæller spring at denne klasse snakker med databasen
@Repository


// Her ligger database metoderne
public class CarRepository {
    // en variabel som bruges til sql commands
    private final JdbcTemplate jdbc;




    public CarRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // denne metode tager en database række og laver den om til en objekt
    private Car mapCar(java.sql.ResultSet rs) throws java.sql.SQLException {
        Car c = new Car(); // creates a new empty car
        c.setCarId(rs.getInt("car_id"));
        c.setVinNumber(rs.getString("vin_number"));
        c.setCarBrand(rs.getString("car_brand"));
        c.setCarModel(rs.getString("car_model"));
        c.setCurrentKm(rs.getInt("current_km"));
        c.setTrimLevel(rs.getInt("trim_level"));
        c.setCarStatus(rs.getString("car_status"));
        c.setCarRentPrice(rs.getInt("car_rent_price"));
        c.setCarTotalPrice(rs.getInt("car_total_price"));
        c.setRegTax(rs.getInt("reg_tax"));
        c.setCo2Emission(rs.getInt("co2_emission"));
        c.setActiveStatus(rs.getBoolean("active_status"));
        return c; // returnere den udfyldte car objekt
    }

    //Metoden kører en sql query for at finde og udskrive alle biler,
    public List<Car> findAll() {
        String sql = "SELECT * FROM car ORDER BY car_id DESC";
        return jdbc.query(sql, (rs, row) -> mapCar(rs));
    }


    // metoden finder biler ud far status
    public List<Car> findByActiveStatus(boolean active) {
        String sql = "SELECT * FROM car WHERE active_status = ? ORDER BY car_id DESC";
        return jdbc.query(sql, new Object[]{active}, (rs, row) -> mapCar(rs));
    }


    //metoden finder en bil ud fra id
    public Car findById(int id) {
        String sql = "SELECT * FROM car WHERE car_id = ?";
        return jdbc.queryForObject(sql, new Object[]{id}, (rs, row) -> mapCar(rs));
    }

    //tilføjer en ny bil til databasen
    public void create(Car car) {
        String sql = """
            INSERT INTO car 
            (vin_number, car_brand, car_model, current_km, trim_level, car_status,
             car_rent_price, car_total_price, reg_tax, co2_emission, active_status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbc.update(sql,
                car.getVinNumber(),
                car.getCarBrand(),
                car.getCarModel(),
                car.getCurrentKm(),
                car.getTrimLevel(),
                car.getCarStatus(),
                car.getCarRentPrice(),
                car.getCarTotalPrice(),
                car.getRegTax(),
                car.getCo2Emission(),
                car.isActiveStatus()
        );
    }
    //sletter en bil ud fra given ID
    public void delete(int id) {
        jdbc.update("DELETE FROM car WHERE car_id = ?", id);
    }


    //sætter en bils status til aktiv
    public void setActiveStatus(int carId, boolean status) {
        jdbc.update("UPDATE car SET active_status = ? WHERE car_id = ?", status, carId);
    }
}