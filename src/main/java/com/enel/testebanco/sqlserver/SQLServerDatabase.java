package com.enel.testebanco.sqlserver;

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

public class SQLServerDatabase implements IDatabase {

    private Connection connection;
    private final String user;
    private final String password;
    private final String hostname;
    private final String port;
    private final String databaseName;    
    
    public SQLServerDatabase(String user, String password, String hostname, String port, String databaseName) {
        this.user = user;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
        this.databaseName = databaseName;
    }

    @Override
    public void initConnection() throws SQLException {
        String dbUrl = "jdbc:sqlserver://" + getHostname() + ":" + this.port + ";databaseName=" + this.databaseName;
        this.connection = DriverManager.getConnection(dbUrl, getUser(), this.password);
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
        return this.databaseName;
    }

    @Override
    public List<String> queryTables() throws SQLException {
        List<String> tables = new ArrayList<>();
        PreparedStatement st;
        ResultSet rs;
        
        st = connection.prepareStatement("SELECT OBJECT_SCHEMA_NAME(t.object_id) schema_name, t.name table_name FROM sys.tables as t");
        rs = st.executeQuery();
        while (rs.next()) {
            String[] t = new String[2];
            t[0] = rs.getString("schema_name");
            t[1] = rs.getString("table_name");            
            tables.add(t[0] + "." + t[1]);
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
        for (String table : tables) {
            st = connection.prepareStatement("SELECT count(*) AS count FROM " + table);
            rs = st.executeQuery();
            while (rs.next()) {
                tablesNumberOfRows.put(table, rs.getInt("count"));
            }
        }

        rs.close();
        st.close();
        return tablesNumberOfRows;
    }

    @Override
    public List<String> queryIndexes() throws SQLException {
        List<String> indexes = new ArrayList<>();
        PreparedStatement st;
        ResultSet rs;

        st = connection.prepareStatement("SELECT OBJECT_SCHEMA_NAME(i.object_id) schema_name, i.name index_name FROM sys.indexes as i");
        rs = st.executeQuery();
        while (rs.next()) {
            indexes.add(rs.getString("schema_name") + "." + rs.getString("index_name"));
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

        st = connection.prepareStatement("SELECT OBJECT_SCHEMA_NAME(p.object_id) schema_name, p.name procedure_name FROM sys.procedures as p;");
        rs = st.executeQuery();
        while (rs.next()) {
            procedures.add(rs.getString("schema_name") + "." + rs.getString("procedure_name"));
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

        st = connection.prepareStatement("SELECT OBJECT_SCHEMA_NAME(v.object_id) schema_name, v.name view_name FROM sys.views as v");
        rs = st.executeQuery();
        while (rs.next()) {
            views.add(rs.getString("schema_name") + "." + rs.getString("view_name"));
        }

        st.close();
        rs.close();
        return views;
    }

    @Override
    public List<String> queryFunctions() throws SQLException {
        return new ArrayList<>();
    }

    @Override
    public List<String> queryTriggers() throws SQLException {
        List<String> triggers = new ArrayList<>();
        PreparedStatement st;
        ResultSet rs;

        st = connection.prepareStatement("SELECT OBJECT_SCHEMA_NAME(t.object_id) schema_name, t.name trigger_name FROM sys.triggers as t;");
        rs = st.executeQuery();
        while (rs.next()) {
            triggers.add(rs.getString("schema_name") + "." + rs.getString("trigger_name"));
        }

        st.close();
        rs.close();
        return triggers;
    }

}
