package com.kec.academic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/kec_academics";

    private static final String USER = "root";

    public static Connection getConnection() throws SQLException {

        String password = System.getenv("KEC_DB_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new SQLException(
                "Database password not found. Set KEC_DB_PASSWORD first."
            );
        }

        return DriverManager.getConnection(URL, USER, password);
    }
}