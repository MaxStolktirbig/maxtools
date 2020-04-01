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
    private String user ="root";
    private String password = "rKxfpK57";
    int lport = 5656;
    String rhost = "85.214.169.10";
    String host = "85.214.169.10";
    int rport = 3306;
    String dbuserName = "iac";
    String dbpassword = "I@c202020";
    String url = "jdbc:mysql://localhost:" + rport;
    String driverName = "com.mysql.cj.jdbc.Driver";
    Connection conn = null;
    Session session = null;

    private DataConnectionPool(){
        if(INSTANCE == null) {
            openConnections = createConnection(url, user, password);
        }
    }

    private List<Connection> createConnection(String url, String user, String password) {
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        try {
            //Set StrictHostKeyChecking property to no to avoid UnknownHostKey issue
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            session = jsch.getSession(user, host);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
            System.out.println("Connected to ssh");
            int assinged_port = session.setPortForwardingL(lport, rhost, rport);
            System.out.println("localhost:" + assinged_port + " -> " + rhost + ":" + rport);
            System.out.println("Port Forwarded");

            //mysql database connectivity
            Class.forName(driverName);
            for(int i = 0; i< INITIAL_POOL_SIZE; i++) {
                conn = DriverManager.getConnection(url, dbuserName, dbpassword);
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


