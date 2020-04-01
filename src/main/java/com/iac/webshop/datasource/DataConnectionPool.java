package com.iac.webshop.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class DataConnectionPool {
    private static final DataConnectionPool INSTANCE = new DataConnectionPool();
    private static List<Connection> openConnections;
    private static List<Connection> usedConnections = new ArrayList<>();
    private int INITIAL_POOL_SIZE = 10;
    private String dbuserName = "iac";
    private String dbpassword = "I@c202020";
    private String url = "jdbc:mysql://localhost:3306/prouductService";
    private String driverName = "com.mysql.cj.jdbc.Driver";
   private  Connection conn = null;

    private DataConnectionPool(){
        if(INSTANCE == null) {
            openConnections = createConnection(url, dbuserName, dbpassword);
        }
    }

    private List<Connection> createConnection(String url, String user, String password) {
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        try {
            //mysql database connectivity
            Class.forName(driverName);
            for(int i = 0; i< INITIAL_POOL_SIZE; i++) {
                conn = DriverManager.getConnection(url, user, password);
                System.out.println("Database connection established (id = "+i+")");
                System.out.println("[DONE]");
                pool.add(conn);
            }
            return pool;
        } catch (Exception e) {
            System.out.println("Couldn't make a connection!");
            e.printStackTrace();
            return null;
        }
    }

    public Connection getConnection() {
        try {
            Connection connection = openConnections.get(openConnections.size() - 1);
            usedConnections.add(connection);
            openConnections.remove(connection);
            return connection;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean releaseConnection(Connection connection) {
        try {
            openConnections.add(connection);
            usedConnections.remove(connection);
            return true;
        } catch (Exception e){
            System.out.println("An error has occurred while trying to release a connection!");
            return false;
        }
    }

    public static DataConnectionPool getInstance(){
        return INSTANCE;
    }
}


