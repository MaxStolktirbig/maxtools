package maxtool.dbhelper.tools.dbconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataConnectionPool {
    private static List<Connection> openConnections;
    private static List<Connection> usedConnections = new ArrayList<>();
    private int INITIAL_POOL_SIZE = 10;
    private String driverName;
    private  Connection conn = null;

    public DataConnectionPool(String urlNoPrefix, String dbuserName, String dbpassword, ConnectionType connectionType, int INITIAL_POOL_SIZE){
        this.INITIAL_POOL_SIZE = INITIAL_POOL_SIZE;
        String url = setDriverName(connectionType)+ urlNoPrefix;
        openConnections = createConnection(url, dbuserName, dbpassword);
    }
    public DataConnectionPool(String urlNoPrefix, String dbuserName, String dbpassword, ConnectionType connectionType){
        String url = setDriverName(connectionType)+urlNoPrefix;
        openConnections = createConnection(url, dbuserName, dbpassword);
    }

    private List<Connection> createConnection(String url, String user, String password) {
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        try {
            //mysql database connectivity
            Class.forName(driverName);
            long totaltime = System.currentTimeMillis();
            System.out.println("[INFO] Starting connection pool for: " +
                    "\n[INFO] url="+url+
                    "\n[INFO] uname="+user+
                    "\n[INFO] password="+password);
            for(int i = 0; i< INITIAL_POOL_SIZE; i++) {
                long time = System.currentTimeMillis();
                conn = DriverManager.getConnection(url, user, password);
                pool.add(conn);
                System.out.println("[CONNECTION] " +
                        (i+1)+" of "+INITIAL_POOL_SIZE +
                        " ["+(System.currentTimeMillis()-time)+"ms]");
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

    private Connection getConnection() {
        try {
            Connection connection = openConnections.get(openConnections.size() - 1);
            usedConnections.add(connection);
            openConnections.remove(connection);
            return connection;
        }catch (Exception e){
            System.out.println("[ERROR] An error has occurred while trying to get a connection!");
            System.out.println("[STACKTRACE]");
            e.printStackTrace();
            System.out.println("[STACKTRACE]");
            return null;
        }
    }

    private void releaseConnection(Connection connection) {
        try {
            openConnections.add(connection);
            usedConnections.remove(connection);
        } catch (Exception e){
            System.out.println("[ERROR] An error has occurred while trying to release a connection!");
            System.out.println("[STACKTRACE]");
            e.printStackTrace();
            System.out.println("[STACKTRACE]");
        }
    }

    public ResultSet executeQuery(PreparedStatement preparedStatement){
        ResultSet resultSet = null;
        try {
            Connection connection = getConnection();
            resultSet = preparedStatement.executeQuery();
            releaseConnection(connection);
            return resultSet;
        } catch (NullPointerException | SQLException e){
            e.printStackTrace();
            return resultSet;
        }
    }
    private String setDriverName(ConnectionType connectionType){
        String driverPrefix =  null;
        switch (connectionType) {
            case H2:
                this.driverName = "org.h2.Driver";
                driverPrefix = "h2";
                break;
            case DB2:
                this.driverName = "com.ibm.db2.jcc.DB2Driver";
                driverPrefix =  "db2";
                break;
            case DERBY:
                this.driverName = "org.apache.derby.jdbc.EmbeddedDriver";
                driverPrefix = "derby";
                break;
            case MYSQL:
                this.driverName = "com.mysql.cj.jdbc.Driver";
                driverPrefix = "mysql";
                break;
            case HSQLDB:
                this.driverName = "org.hsqldb.jdbcDriver";
                driverPrefix = "hsqldb";
                break;
            case ORACLE:
                this.driverName = "oracle.jdbc.driver.OracleDriver";
                driverPrefix = "oracle";
                break;
            case MARIADB:
                this.driverName = "org.mariadb.jdbc.Driver";
                driverPrefix = "mariadb";
                break;
            case SAPHANA:
                this.driverName = "com.sap.db.jdbc.Driver";
                driverPrefix = "sap";
                break;
            case FIREBIRD:
                this.driverName = "org.firebirdsql.jdbc.FBDriver";
                driverPrefix = "firebirdsql";
                break;
            case INFORMIX:
                this.driverName = "com.informix.jdbc.IfxDriver";
                driverPrefix = "informix-sqli";
                break;
            case SQLSERVER:
                this.driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                driverPrefix = "sqlserver";
                break;
            case POSTGRESQL:
                this.driverName = "org.postgresql.Driver";
                driverPrefix = "postgresql";
                break;
        }
        return "jdbc:"+driverPrefix+"://";
    }
}


