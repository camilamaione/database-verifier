package com.enel.testebanco;

import com.enel.testebanco.oracle.OracleTestProfile;
import com.enel.testebanco.oracle.OracleDatabase;
import com.enel.testebanco.sqlserver.SQLServerDatabase;
import com.enel.testebanco.sqlserver.SQLServerTestProfile;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MigrationVerifier {

    private final String outputDirectoryPath;
    private final IDatabase sourceDatabase;
    private final IDatabase destDatabase;

    private Report consolidatedResultsReport;

    public MigrationVerifier(OracleTestProfile p) throws SQLException, IOException {
        this.sourceDatabase = new OracleDatabase(p.getSourceUsername(), p.getSourcePassword(), p.getSourceHostname() + p.getSourceHostnameSufix(), p.getSourceServiceName());
        this.destDatabase = new OracleDatabase(p.getDestUsername(), p.getDestPassword(), p.getDestHostname() + p.getDestHostnameSufix(), p.getDestSID());
        outputDirectoryPath = ".\\Resultados\\" + p.getSourceUsername() + "_" + p.getSourceHostname() + "_" + p.getDestHostname() + "\\";
        init();
    }

    public MigrationVerifier(SQLServerTestProfile p) throws SQLException, IOException {
        this.sourceDatabase = new SQLServerDatabase(p.getSourceUsername(), p.getSourcePassword(), p.getSourceHostname() + p.getSourceHostnameSufix(), p.getSourcePort(), p.getDestDatabaseName());
        this.destDatabase = new SQLServerDatabase(p.getDestUsername(), p.getDestPassword(), p.getDestHostname() + p.getDestHostnameSufix(), p.getDestPort(), p.getDestDatabaseName());
        outputDirectoryPath = ".\\Resultados\\" + p.getSourceUsername() + "_" + p.getSourceHostname() + "_" + p.getDestHostname() + "\\";
        init();
    }

    private void init() throws SQLException, IOException {
        System.out.println("Iniciando validação para " + sourceDatabase.getUser().toUpperCase());
        connectDatabases();
        createReportsDirectory();
        this.consolidatedResultsReport = new Report(this.outputDirectoryPath + "RESULTADOS_CONSOLIDADOS.txt");       
        
        performVerification();
        disconnectDatabases();
        System.out.println("Teste finalizado.");
        this.consolidatedResultsReport.export();
        openResults();
    }

    private void createReportsDirectory() {
        File file = new File(outputDirectoryPath);
        if (file.exists()) {
            file.delete();
        }
        file.mkdirs();
    }

    private void performVerification() throws SQLException, IOException {
        System.out.println("Verificando views...");
        verifyViews();
        System.out.println("Verificando stored procedures...");
        verifyStoredProcedures();
        System.out.println("Verificando índices...");
        verifyIndexes();
        System.out.println("Verificando triggers...");
        verifyTriggers();
        System.out.println("Verificando funções...");
        verifyFunctions();
        System.out.println("Verificando tabelas...");
        verifyTables();
        System.out.println("Contando registros...");
        verifyTableRows();
    }

    private void openResults() throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.open(new File(outputDirectoryPath));
    }

    private void connectDatabases() throws SQLException {
        sourceDatabase.initConnection();
        System.out.println("Conectado no banco " + sourceDatabase.getDatabaseName() + " na origem " + sourceDatabase.getHostname());
        destDatabase.initConnection();
        System.out.println("Conectado no banco " + destDatabase.getDatabaseName() + " no destino " + destDatabase.getHostname());
    }

    private void disconnectDatabases() throws SQLException {
        this.sourceDatabase.closeConnection();
        this.destDatabase.closeConnection();
    }

    private void verifyViews() throws SQLException, IOException {
        List<String> viewsSource = this.sourceDatabase.queryViews();
        List<String> viewsDest = this.destDatabase.queryViews();

        if (!viewsSource.isEmpty()) {
            Report report = new Report(this.outputDirectoryPath + "views.txt");

            report.writeNewLineOnInfoSection(viewsSource.size() + " views encontradas na origem (" + sourceDatabase.getHostname() + "):");
            report.writeNewLineOnInfoSection("\n");
            viewsSource.forEach(i -> report.writeNewLineOnInfoSection(i));

            report.writeNewLineOnInfoSection("\n");
            report.writeNewLineOnInfoSection(viewsDest.size() + " views encontradas no destino (" + destDatabase.getHostname() + "):");
            report.writeNewLineOnInfoSection("\n");
            viewsDest.forEach(i -> report.writeNewLineOnInfoSection(i));

            int countErros = 0;
            for (String i : viewsSource) {
                if (!viewsDest.contains(i)) {
                    report.writeNewLineOnResultsSection("View " + i.toUpperCase() + " faltando");
                    countErros++;
                }
            }
            if (countErros == 0) {
                report.writeNewLineOnResultsSection("Nenhuma view faltando");
            }
            this.consolidatedResultsReport.writeNewLineOnResultsSection(report.readResultsSection());           
            report.export();
        }
    }

    private void verifyStoredProcedures() throws SQLException, IOException {
        List<String> proceduresSource = this.sourceDatabase.queryStoredProcedures();
        List<String> proceduresDest = this.destDatabase.queryStoredProcedures();

        if (!proceduresSource.isEmpty()) {
            Report report = new Report(this.outputDirectoryPath + "procedures.txt");

            report.writeNewLineOnInfoSection(proceduresSource.size() + " stored procedures encontradas na origem (" + sourceDatabase.getHostname() + "):");
            report.writeNewLineOnInfoSection("\n");
            proceduresSource.forEach(i -> report.writeNewLineOnInfoSection(i));

            report.writeNewLineOnInfoSection("\n");
            report.writeNewLineOnInfoSection(proceduresDest.size() + " stored procedures encontradas no destino (" + destDatabase.getHostname() + "):");
            report.writeNewLineOnInfoSection("\n");
            proceduresDest.forEach(i -> report.writeNewLineOnInfoSection(i));

            int countErros = 0;
            for (String i : proceduresSource) {
                if (!proceduresDest.contains(i)) {
                    report.writeNewLineOnResultsSection("Stored procedure " + i.toUpperCase() + " faltando");
                    countErros++;
                }
            }
            if (countErros == 0) {
                report.writeNewLineOnResultsSection("Nenhuma stored procedure faltando");
            }
            this.consolidatedResultsReport.writeNewLineOnResultsSection(report.readResultsSection());  
            report.export();
        }
    }

    private void verifyIndexes() throws SQLException, IOException {
        List<String> indexesSource = this.sourceDatabase.queryIndexes();
        List<String> indexesDest = this.destDatabase.queryIndexes();

        if (!indexesSource.isEmpty()) {
            Report report = new Report(this.outputDirectoryPath + "indices.txt");

            report.writeNewLineOnInfoSection(indexesSource.size() + " índices encontrados na origem (" + sourceDatabase.getHostname() + "):");
            report.writeNewLineOnInfoSection("\n");
            indexesSource.forEach(i -> report.writeNewLineOnInfoSection(i));

            report.writeNewLineOnInfoSection("\n");
            report.writeNewLineOnInfoSection(indexesDest.size() + " índices encontrados no destino (" + destDatabase.getHostname() + "):");
            report.writeNewLineOnInfoSection("\n");
            indexesDest.forEach(i -> report.writeNewLineOnInfoSection(i));

            int countErros = 0;
            for (String i : indexesSource) {
                if (!indexesDest.contains(i)) {
                    report.writeNewLineOnResultsSection("Índice da tabela " + i.toUpperCase() + " faltando");
                    countErros++;
                }
            }
            if (countErros == 0) {
                report.writeNewLineOnResultsSection("Nenhum indice faltando");
            }
            this.consolidatedResultsReport.writeNewLineOnResultsSection(report.readResultsSection());  
            report.export();
        }
    }

    private void verifyTriggers() throws SQLException, IOException {
        List<String> triggersSource = this.sourceDatabase.queryTriggers();
        List<String> triggersDest = this.destDatabase.queryTriggers();

        if (!triggersSource.isEmpty()) {
            Report report = new Report(this.outputDirectoryPath + "triggers.txt");

            report.writeNewLineOnInfoSection(triggersSource.size() + " triggers encontradas na origem (" + sourceDatabase.getHostname() + "):");
            report.writeNewLineOnInfoSection("\n");
            triggersSource.forEach(i -> report.writeNewLineOnInfoSection(i));

            report.writeNewLineOnInfoSection("\n");
            report.writeNewLineOnInfoSection(triggersDest.size() + " triggers encontradas no destino (" + destDatabase.getHostname() + "):");
            report.writeNewLineOnInfoSection("\n");
            triggersDest.forEach(i -> report.writeNewLineOnInfoSection(i));

            int countErros = 0;
            for (String i : triggersSource) {
                if (!triggersDest.contains(i)) {
                    report.writeNewLineOnResultsSection("Trigger " + i.toUpperCase() + " faltando");                    
                    countErros++;
                }
            }
            if (countErros == 0) {
                report.writeNewLineOnResultsSection("Nenhuma trigger faltando");
            }
            this.consolidatedResultsReport.writeNewLineOnResultsSection(report.readResultsSection());
            report.export();
        }
    }

    private void verifyFunctions() throws SQLException, IOException {
        List<String> functionsSource = this.sourceDatabase.queryFunctions();
        List<String> functionsDest = this.destDatabase.queryFunctions();

        if (!functionsSource.isEmpty()) {
            Report report = new Report(this.outputDirectoryPath + "functions.txt");            

            report.writeNewLineOnInfoSection(functionsSource.size() + " functions encontradas na origem (" + sourceDatabase.getHostname() + "):");
            report.writeNewLineOnInfoSection("\n");
            functionsSource.forEach(i -> report.writeNewLineOnInfoSection(i));

            report.writeNewLineOnInfoSection("\n");
            report.writeNewLineOnInfoSection(functionsDest.size() + " functions encontradas no destino (" + destDatabase.getHostname() + "):");
            report.writeNewLineOnInfoSection("\n");
            functionsDest.forEach(i -> report.writeNewLineOnInfoSection(i));

            int countErros = 0;
            for (String i : functionsSource) {
                if (!functionsDest.contains(i)) {
                    report.writeNewLineOnResultsSection("Function " + i.toUpperCase() + " faltando");
                    countErros++;
                }
            }
            if (countErros == 0) {
                report.writeNewLineOnResultsSection("Nenhuma function faltando");
            }
            this.consolidatedResultsReport.writeNewLineOnResultsSection(report.readResultsSection());
            report.export();
        }
    }

    private void verifyTables() throws SQLException, IOException {
        List<String> tablesSource = this.sourceDatabase.queryTables();
        List<String> tablesDest = this.destDatabase.queryTables();

        if (!tablesSource.isEmpty()) {
            Report report = new Report(this.outputDirectoryPath + "tables.txt");

            report.writeNewLineOnInfoSection(tablesSource.size() + " tabelas encontradas na origem (" + sourceDatabase.getHostname() + "):");
            report.writeNewLineOnInfoSection("\n");
            tablesSource.forEach(i -> report.writeNewLineOnInfoSection(i));

            report.writeNewLineOnInfoSection("\n");
            report.writeNewLineOnInfoSection(tablesDest.size() + " tabelas encontradas no destino (" + destDatabase.getHostname() + "):");
            report.writeNewLineOnInfoSection("\n");
            tablesDest.forEach(i -> report.writeNewLineOnInfoSection(i));

            int countErros = 0;
            for (String i : tablesSource) {
                if (!tablesDest.contains(i)) {
                    report.writeNewLineOnResultsSection("Tabela " + i.toUpperCase() + " faltando");
                    countErros++;
                }
            }
            if (countErros == 0) {
                report.writeNewLineOnResultsSection("Nenhuma tabela faltando");
            }
            this.consolidatedResultsReport.writeNewLineOnResultsSection(report.readResultsSection());
            report.export();
        }
    }

    private void verifyTableRows() throws SQLException, IOException {
        Map<String, Integer> tablesSource = this.sourceDatabase.queryTablesNumberOfRows();
        Map<String, Integer> tablesDest = this.destDatabase.queryTablesNumberOfRows();

        Report report = new Report(this.outputDirectoryPath + "registros.csv");        

        report.writeNewLineOnInfoSection("Tabela,Registros na origem (" + sourceDatabase.getHostname() + "),Registros no destino (" + destDatabase.getHostname() + ")");

        for (Map.Entry<String, Integer> entry : tablesSource.entrySet()) {
            String tableName = entry.getKey();
            report.writeNewLineOnInfoSection(tableName + "," + tablesSource.get(tableName) + "," + tablesDest.get(tableName) + "\n");
        }
        report.export();
    }
}
