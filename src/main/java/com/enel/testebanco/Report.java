package com.enel.testebanco;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class Report {

    private StringBuilder infoSection;
    private StringBuilder resultsSection;
    private File file;

    public Report(String filePath) throws IOException {
        this.infoSection = new StringBuilder();
        this.resultsSection = new StringBuilder();
        createReportFile(filePath);
    }

    private void createReportFile(String filePath) throws IOException {
        this.file = new File(filePath);
        if (this.file.exists()) {
            this.file.delete();
        }
        this.file.createNewFile();
    }

    public void writeNewLineOnInfoSection(String msg) {
        this.infoSection.append(msg).append("\n");
    }

    public void writeNewLineOnResultsSection(String msg) {
        this.resultsSection.append(msg).append("\n");
    }

    public String readResultsSection() {
        return this.resultsSection.toString();
    }

    public void export() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(this.file);
        if (infoSection.length() > 0) {
            pw.write(infoSection.toString());
        }
        if (resultsSection.length() > 0) {
            pw.write("\n");
            pw.write("\n");
            pw.write("------------------- RESULTADOS:");
            pw.write("\n");
            pw.write("\n");
            pw.write(resultsSection.toString());
        }         
        pw.close();
    }
}
