package com.example.bilabonnementeks.repository;


import com.example.bilabonnementeks.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private  JdbcTemplate jdbc = new JdbcTemplate();

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public User validateLogin(String username, String password) {
        String sql = "SELECT * FROM USER WHERE username = ? AND user_password = ?";
        return (User)this.jdbc.query(sql, new Object[]{username, password}, (rs) -> {
            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setUserPassword(rs.getString("user_password"));
                u.setUserRole(rs.getString("user_role"));
                return u;
            } else {
                return null;
            }
        });
    }
}
