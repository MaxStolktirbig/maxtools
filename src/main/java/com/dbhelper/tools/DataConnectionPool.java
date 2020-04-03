package com.dbhelper.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class DataConnectionPool {
    private static List<Connection> openConnections;
    private static List<Connection> usedConnections = new ArrayList<>();
    private int INITIAL_POOL_SIZE = 10;
    private String driverName;
    private  Connection conn = null;

    public DataConnectionPool(String driverName, String url, String dbuserName, String dbpassword, int INITIAL_POOL_SIZE){
        this.driverName = driverName;
        this.INITIAL_POOL_SIZE = INITIAL_POOL_SIZE;
        openConnections = createConnection(url, dbuserName, dbpassword);
    }
    public DataConnectionPool(String driverName, String url, String dbuserName, String dbpassword){
        this.driverName = driverName;
        openConnections = createConnection(url, dbuserName, dbpassword);
    }

    private List<Connection> createConnection(String url, String user, String password) {
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        try {
            //mysql database connectivity
            Class.forName(driverName);
            long totaltime = System.currentTimeMillis();
            System.out.println("[INFO] Starting connection pool");
            for(int i = 0; i< INITIAL_POOL_SIZE; i++) {
                long time = System.currentTimeMillis();
                conn = DriverManager.getConnection(url, user, password);
                System.out.println("[CONNECTION] " +
                        (i+1)+" of "+INITIAL_POOL_SIZE +
                        " ["+(System.currentTimeMillis()-time)+"ms]");
                pool.add(conn);
            }
            System.out.println("[DONE] "+(System.currentTimeMillis()-totaltime)+"ms");
            return pool;
        } catch (Exception e) {
            System.out.println("[ERROR] Couldn't make a connection!");
            System.out.println("[STACKTRACE]");
            e.printStackTrace();
            System.out.println("[STACKTRACE]");
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
            System.out.println("[ERROR] An error has occurred while trying to release a connection!");
            System.out.println("[STACKTRACE]");
            e.printStackTrace();
            System.out.println("[STACKTRACE]");
            return false;
        }
    }

}


