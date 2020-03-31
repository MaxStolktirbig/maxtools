package com.iac.webshop.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataConnectionPool {
    private static final DataConnectionPool INSTANCE = new DataConnectionPool();
    private static List<Connection> openConnections;
    private static List<Connection> usedConnections = new ArrayList<>();
    private int INITIAL_POOL_SIZE = 10;
    private String url;
    private String user;
    private String password;


    private DataConnectionPool(){
        if(INSTANCE == null) {
            List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
            long time = System.currentTimeMillis();
            for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
                pool.add(createConnection(url, user, password));
                System.out.println("Made connection (id = "+i+") [time elapsed: "+(System.currentTimeMillis()-time)+"ms]");
            }
            openConnections = pool;
        }
    }

    private Connection createConnection(String url, String user, String password){
        try {

            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e){
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
