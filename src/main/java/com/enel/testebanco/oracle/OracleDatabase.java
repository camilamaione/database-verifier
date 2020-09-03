package com.enel.testebanco.oracle;

import com.enel.testebanco.IDatabase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OracleDatabase implements IDatabase {

    private Connection connection;
    private final String user;
    private final String password;
    private final String hostname;
    private final OracleSID sid;
    private final OracleServiceName serviceName;
    
    public OracleDatabase(String user, String password, String hostname, OracleSID sid) throws SQLException {        
        this.user = user;
        this.password = password;
        this.hostname = hostname;
        this.sid = sid;       
        this.serviceName = null;
    }

    public OracleDatabase(String user, String password, String hostname, OracleServiceName serviceName) throws SQLException {
        this.user = user;
        this.password = password;
        this.hostname = hostname;
        this.serviceName = serviceName;
        this.sid = null;
    }
    
    @Override
    public void initConnection() throws SQLException {
        String dbUrl = null;
        if (this.sid != null) {
            dbUrl = "jdbc:oracle:thin:@(description=(address_list=(address=(protocol=tcp)(port=1521)(host="
                + hostname + " )))(connect_data=(SID=" + sid.getName() + ")))";
        } else if (this.serviceName != null) {
            dbUrl = "jdbc:oracle:thin:@(description=(address_list=(address=(protocol=tcp)(port=1521)(host="
                + hostname + " )))(connect_data=(SERVICE_NAME=" + serviceName.getName() + ")))";
        }
        this.connection = DriverManager.getConnection(dbUrl, user, password);
    }

    @Override
    public void closeConnection() throws SQLException {
        this.connection.close();
    }

    @Override
    public String getUser() {
        return this.user;
    }
    
    @Override
    public String getHostname() {
        return this.hostname;
    }
    
    @Override
    public String getDatabaseName() {
        return (this.sid != null) ? sid.getName() : serviceName.getName();
    }
    
    @Override
    public List<String> queryTables() throws SQLException {
        List<String> tables = new ArrayList<>();
        PreparedStatement st;
        ResultSet rs;
        
        st = connection.prepareStatement("SELECT table_name FROM user_tables ORDER BY table_name");
        rs = st.executeQuery();
        while (rs.next()) {
            tables.add(rs.getString("table_name"));
        }

        st.close();
        rs.close();
        return tables;
    }

    @Override
    public Map<String, Integer> queryTablesNumberOfRows() throws SQLException {
        Map<String, Integer> tablesNumberOfRows = new HashMap<>();
        PreparedStatement st = null;
        ResultSet rs = null;

        // Get list of tables
        List<String> tables = queryTables();

        // For each table, count the number of rows and store table name and number of rows in the hashmap
        for (String tableName : tables) {
            st = connection.prepareStatement("SELECT count(*) AS count FROM " + tableName);
            rs = st.executeQuery();
            while (rs.next()) {
                tablesNumberOfRows.put(tableName, rs.getInt("count"));
            }
        }

        if (rs!= null)
            rs.close();
        if(st != null)
            st.close();
        return tablesNumberOfRows;
    }

    @Override
    public List<String> queryIndexes() throws SQLException {
        List<String> indexes = new ArrayList<>();
        PreparedStatement st;
        ResultSet rs;

        st = connection.prepareStatement("SELECT TABLE_NAME "
                + "FROM all_indexes "
                + "WHERE table_owner = '" + getUser() + "' "
                + " ORDER BY TABLE_NAME");
        rs = st.executeQuery();
        while (rs.next()) {
            indexes.add(rs.getString("TABLE_NAME"));
        }

        st.close();
        rs.close();
        return indexes;
    }

    @Override
    public List<String> queryStoredProcedures() throws SQLException {
        List<String> procedures = new ArrayList<>();
        PreparedStatement st;
        ResultSet rs;
        
        st = connection.prepareStatement("SELECT OBJECT_NAME "
                + "FROM user_procedures "
                + "WHERE object_type = 'PROCEDURE' "
                + "ORDER BY OBJECT_NAME");
        rs = st.executeQuery();
        while (rs.next()) {
            procedures.add(rs.getString("OBJECT_NAME"));
        }

        st.close();
        rs.close();
        return procedures;
    }

    @Override
    public List<String> queryViews() throws SQLException {
        List<String> views = new ArrayList<>();
        PreparedStatement st;
        ResultSet rs;

        st = connection.prepareStatement("SELECT VIEW_NAME "
                + "FROM user_views "
                + "ORDER BY VIEW_NAME");
        rs = st.executeQuery();
        while (rs.next()) {
            views.add(rs.getString("VIEW_NAME"));
        }

        st.close();
        rs.close();
        return views;
    }

    @Override
    public List<String> queryFunctions() throws SQLException {
        List<String> functions = new ArrayList<>();
        PreparedStatement st;
        ResultSet rs;

        st = connection.prepareStatement("SELECT OBJECT_NAME "
                + "FROM user_procedures "
                + "WHERE object_type = 'FUNCTION' "
                + "ORDER BY OBJECT_NAME");
        rs = st.executeQuery();
        while (rs.next()) {
            functions.add(rs.getString("OBJECT_NAME"));
        }

        st.close();
        rs.close();
        return functions;
    }

    @Override
    public List<String> queryTriggers() throws SQLException {
        List<String> triggers = new ArrayList<>();
        PreparedStatement st;
        ResultSet rs;

        st = connection.prepareStatement("SELECT TRIGGER_NAME "
                + "FROM user_triggers "
                + "ORDER BY TRIGGER_NAME");
        rs = st.executeQuery();
        while (rs.next()) {
            triggers.add(rs.getString("TRIGGER_NAME"));
        }

        st.close();
        rs.close();
        return triggers;
    }
}
