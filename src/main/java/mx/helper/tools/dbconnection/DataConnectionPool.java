package mx.helper.tools.dbconnection;

import mx.helper.tools.SystemMessage;

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
        openConnections = createConnection(urlNoPrefix, dbuserName, dbpassword, connectionType);
    }
    public DataConnectionPool(String urlNoPrefix, String dbuserName, String dbpassword, ConnectionType connectionType){
        openConnections = createConnection(urlNoPrefix, dbuserName, dbpassword, connectionType);
    }

    private List<Connection> createConnection(String urlNoPrefix, String user, String password, ConnectionType connectionType) {
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        try {
            String baseUrl = urlNoPrefix;
            String url = setDriverName(connectionType)+ baseUrl;
            //mysql database connectivity
            Class.forName(driverName);
            long totaltime = System.currentTimeMillis();
            SystemMessage.infoMessage("Starting connection pool for: ");
            SystemMessage.infoMessage("database="+urlNoPrefix.split("\\?")[0].split("/")[1]);
            for(int i = 0; i< INITIAL_POOL_SIZE; i++) {
                long time = System.currentTimeMillis();
                conn = DriverManager.getConnection(url, user, password);
                pool.add(conn);
                SystemMessage.connectionMessage((i+1)+" of "+INITIAL_POOL_SIZE +
                        " ["+(System.currentTimeMillis()-time)+"ms]");
            }
            SystemMessage.finishedTaskMessage("----------------------------------------");
            SystemMessage.finishedTaskMessage("Total time:"+((System.currentTimeMillis()-totaltime)/1000)+"s");
            SystemMessage.finishedTaskMessage("----------------------------------------");
            return pool;
        } catch (Exception e) {
            SystemMessage.errorMessage("Couldn't make a connection");
            SystemMessage.exceptionMessage(e);
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
            SystemMessage.errorMessage("An error has occurred while trying to get a connection");
            SystemMessage.exceptionMessage(e);
            return null;
        }
    }

    private void releaseConnection(Connection connection) {
        try {
            if(usedConnections.contains(connection)) {
                openConnections.add(connection);
                usedConnections.remove(connection);
            } else if(openConnections.contains(connection)) {
                SystemMessage.warningMessage("Connection not used!");
            } else{
                SystemMessage.warningMessage("Connection not from this pool");
                SystemMessage.infoMessage("Use dataConnectionPool.getConnection()");
            }
        } catch (Exception e){
            SystemMessage.errorMessage("An error has occurred while trying to release a connection");
            SystemMessage.exceptionMessage(e);
        }
    }

    public ResultSet executeQuery(PreparedStatement preparedStatement, Connection connection){
        ResultSet resultSet = null;
        try {
            resultSet = preparedStatement.executeQuery();
            releaseConnection(connection);
            return resultSet;
        }
        catch (NullPointerException | SQLException e){
            e.printStackTrace();
            return resultSet;
        }
    }

    public void execute(PreparedStatement preparedStatement, Connection connection){
        try {
            preparedStatement.execute();
            releaseConnection(connection);
        } catch (NullPointerException | SQLException e){
            e.printStackTrace();
        }
    }

    public List<Integer> getGeneratedKeys(PreparedStatement preparedStatement, Connection connection){
        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            List<Integer> keys = new ArrayList<>();
            while(resultSet.next()) {
                keys.add(resultSet.getInt(1));
            }
            return keys;
        }catch (NullPointerException|SQLException e){
            e.printStackTrace();
            return null;
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


