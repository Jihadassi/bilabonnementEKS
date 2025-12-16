package com.example.bilabonnementeks.repository;
import com.example.bilabonnementeks.model.DamageReport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DamageReportRepository {

    private final JdbcTemplate jdbcTemplate;


    public DamageReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    //henter alle skaderapporter fra databasen
    public List<DamageReport> findAll() {
        String sql = "SELECT * FROM damageReport ORDER BY damage_id DESC";
        return jdbcTemplate.query(sql, new DamageReportMapper());
    }
    // finder en rapport ud fra ID
    public DamageReport findById(int id) {
        String sql = "SELECT * FROM damageReport WHERE damage_id = ?";
        return jdbcTemplate.queryForObject(sql, new DamageReportMapper(), id);
    }
    // opretter en ny rapport
    public void create(DamageReport report) {
        String sql = "INSERT INTO damageReport (text_description, damage_price, car_id, customer_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                report.getTextDescription(),
                report.getDamagePrice(),
                report.getCarId(),
                report.getCustomerId());
    }
    // sletter en rapport ud far ID
    public void delete(int id) {
        String sql = "DELETE FROM damageReport WHERE damage_id = ?";
        jdbcTemplate.update(sql, id);
    }
    // Mapper en række fra databasen til et DamageReport-objekt
    private static class DamageReportMapper implements RowMapper<DamageReport> {
        @Override
        public DamageReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            DamageReport dr = new DamageReport();
            dr.setDamageId(rs.getInt("damage_id"));
            dr.setTextDescription(rs.getString("text_description"));
            dr.setDamagePrice(rs.getInt("damage_price"));
            dr.setCarId(rs.getInt("car_id"));
            dr.setCustomerId(rs.getInt("customer_id"));
            return dr;
        }
    }
}