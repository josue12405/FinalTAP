package com.example.proyectofinaltap.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {
    private static Connection con = null;
    private static final String URL="jdbc:mysql://localhost:3306/hotel_db";
    private static final String USER = "root";
    private static final String PASSWORD = "0101";

    private ConexionMySQL(){
    }
    public static Connection getInstancia(){
        try {
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(URL,USER,PASSWORD);
            }
        }catch (SQLException e){
            System.err.println("ERROR al conectar con la base de datos " + e.getMessage());
        }
        return con;
    }
}
