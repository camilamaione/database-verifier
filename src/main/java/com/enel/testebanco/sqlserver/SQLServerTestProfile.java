package com.enel.testebanco.sqlserver;

public class SQLServerTestProfile {
    
    private final String sourceUsername;
    private final String sourcePassword;
    private final String sourceHostname;
    private final String sourceHostnameSufix;
    private final String sourcePort;
    private final String sourceDatabaseName;

    private final String destUsername;
    private final String destPassword;
    private final String destHostname;
    private final String destHostnameSufix;
    private final String destPort;
    private final String destDatabaseName;

    public SQLServerTestProfile(String sourceUsername, String sourcePassword, String sourceHostname, String sourceHostnameSufix, String sourcePort, String sourceDatabaseName,
            String destUsername, String destPassword, String destHostname, String destHostnameSufix, String destPort, String destDatabaseName) {
        this.sourceUsername = sourceUsername;
        this.sourcePassword = sourcePassword;
        this.sourceHostname = sourceHostname;
        this.sourceHostnameSufix = sourceHostnameSufix;
        this.sourcePort = sourcePort;
        this.sourceDatabaseName = sourceDatabaseName;
        this.destUsername = destUsername;
        this.destPassword = destPassword;
        this.destHostname = destHostname;
        this.destHostnameSufix = destHostnameSufix;
        this.destPort = destPort;
        this.destDatabaseName = destDatabaseName;
    }

    public String getSourceUsername() {
        return sourceUsername;
    }

    public String getSourcePassword() {
        return sourcePassword;
    }

    public String getSourceHostname() {
        return sourceHostname;
    }

    public String getSourceHostnameSufix() {
        return sourceHostnameSufix;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public String getSourceDatabaseName() {
        return sourceDatabaseName;
    }

    public String getDestUsername() {
        return destUsername;
    }

    public String getDestPassword() {
        return destPassword;
    }

    public String getDestHostname() {
        return destHostname;
    }

    public String getDestHostnameSufix() {
        return destHostnameSufix;
    }

    public String getDestPort() {
        return destPort;
    }

    public String getDestDatabaseName() {
        return destDatabaseName;
    }   
}
