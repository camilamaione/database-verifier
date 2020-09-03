package com.enel.testebanco;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IDatabase {
    
    public void initConnection() throws SQLException;
    
    public void closeConnection() throws SQLException;

    public String getUser();
    
    public String getHostname();
    
    public String getDatabaseName();
    
    public List<String> queryTables() throws SQLException;

    public Map<String, Integer> queryTablesNumberOfRows() throws SQLException;

    public List<String> queryIndexes() throws SQLException;

    public List<String> queryStoredProcedures() throws SQLException;

    public List<String> queryViews() throws SQLException;

    public List<String> queryFunctions() throws SQLException;

    public List<String> queryTriggers() throws SQLException;
    
}
