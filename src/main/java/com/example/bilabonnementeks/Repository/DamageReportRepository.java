package com.example.bilabonnementeks.Repository;
import com.example.bilabonnementeks.model.DamageReport;
import com.example.bilabonnementeks.model.DamageReportMapper;
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

    public List<DamageReport> findAll() {
        String sql = "SELECT * FROM DAMAGE_REPORT ORDER BY damage_id DESC";
        return jdbcTemplate.query(sql, new DamageReportMapper());
    }

    public DamageReport findById(int id) {
        String sql = "SELECT * FROM DAMAGE_REPORT WHERE damage_id = ?";
        return jdbcTemplate.queryForObject(sql, new DamageReportMapper(), id);
    }

    public void create(DamageReport report) {
        String sql = "INSERT INTO DAMAGE_REPORT (text_description, damage_price, car_id, customer_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                report.getTextDescription(),
                report.getDamagePrice(),
                report.getCarId(),
                report.getCustomerId());
    }

    public void delete(int id) {
        String sql = "DELETE FROM DAMAGE_REPORT WHERE damage_id = ?";
        jdbcTemplate.update(sql, id);
    }

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