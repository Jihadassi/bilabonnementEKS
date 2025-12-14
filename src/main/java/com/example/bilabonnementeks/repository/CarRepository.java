package com.example.bilabonnementeks.repository;

import com.example.bilabonnementeks.model.Car;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


// den fortæller spring at denne klasse snakker med databasen
@Repository

// this class is a bean that organizes persistence logic
public class CarRepository { // i denne klasse ligger database metoderne for Car
    private final JdbcTemplate jdbc; // en variabel som bruges til sql commands



// This runs when the object is created.
//Spring gives us a JdbcTemplate, and we store it in our own jdbc variable.
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

    // den metode kører en sql query for at finde og udskrive alle biler,
    // jdbc.query runs the SQL and calls mapCar for each row.
    public List<Car> findAll() {
        String sql = "SELECT * FROM CAR ORDER BY car_id DESC";
        return jdbc.query(sql, (rs, row) -> mapCar(rs));
    }


    // denne metode finder biler ud far status
    // vi får en liste af biler
    public List<Car> findByActiveStatus(boolean active) {
        String sql = "SELECT * FROM CAR WHERE active_status = ? ORDER BY car_id DESC";
        return jdbc.query(sql, new Object[]{active}, (rs, row) -> mapCar(rs));
    }


    // denne metode finder en bil ud fra specifikt id
    // queryForObject() returner kun 1 resultat
    public Car findById(int id) {
        String sql = "SELECT * FROM CAR WHERE car_id = ?";
        return jdbc.queryForObject(sql, new Object[]{id}, (rs, row) -> mapCar(rs));
    }

    // denne metode tilføjer en ny bil til databasen
    // jdbs.templace kører INSERT komandoen
    public void create(Car car) {
        String sql = """
            INSERT INTO CAR 
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
    // denne metode sletter en bil ud fra givem ID
    public void delete(int id) {
        jdbc.update("DELETE FROM CAR WHERE car_id = ?", id);
    }


    // denne metode sætter en bils status til aktiv
    public void setActiveStatus(int carId, boolean status) {
        jdbc.update("UPDATE CAR SET active_status = ? WHERE car_id = ?", status, carId);
    }
}