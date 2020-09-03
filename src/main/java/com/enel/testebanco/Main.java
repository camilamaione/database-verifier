package com.enel.testebanco;

import com.enel.testebanco.oracle.OracleSID;
import com.enel.testebanco.oracle.OracleServiceName;
import com.enel.testebanco.oracle.OracleTestProfile;
import com.enel.testebanco.sqlserver.SQLServerTestProfile;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String args[]) {
		/*
        OracleTestProfile p = new OracleTestProfile(
			sourceUsername, sourcePassword, sourceHostname, sourceHostnameSufix, sourceServiceName, 
			destUsername, destPassword, destHostname, destHostnameSufix, destSID);      
        SQLServerTestProfile p = new SQLServerTestProfile(
			sourceUsername, sourcePassword, sourceHostname, sourceHostnameSufix, sourcePort, sourceDatabaseName, 
			destUsername, destPassword, destHostname, destHostnameSufix, destPort, destDatabaseName);   
		*/
        try {        
        //    MigrationVerifier migrationVerifier = new MigrationVerifier(p);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
