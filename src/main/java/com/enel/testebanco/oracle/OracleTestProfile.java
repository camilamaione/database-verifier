package com.enel.testebanco.oracle;

public class OracleTestProfile {

    private final String sourceUsername;
    private final String sourcePassword;
    private final String sourceHostname;
    private final String sourceHostnameSufix;
    private final OracleServiceName sourceServiceName;

    private final String destUsername;
    private final String destPassword;
    private final String destHostname;
    private final String destHostnameSufix;
    private final OracleSID destSID;

    public OracleTestProfile(String sourceUsername, String sourcePassword, String sourceHostname, String sourceHostnameSufix, 
            OracleServiceName sourceServiceName, String destUsername, String destPassword, String destHostname, 
            String destHostnameSufix, OracleSID destSID) {
        this.sourceUsername = sourceUsername;
        this.sourcePassword = sourcePassword;
        this.sourceHostname = sourceHostname;
        this.sourceHostnameSufix = sourceHostnameSufix;
        this.sourceServiceName = sourceServiceName;
        this.destUsername = destUsername;
        this.destPassword = destPassword;
        this.destHostname = destHostname;
        this.destHostnameSufix = destHostnameSufix;
        this.destSID = destSID;
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

    public OracleServiceName getSourceServiceName() {
        return sourceServiceName;
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

    public OracleSID getDestSID() {
        return destSID;
    }
}
