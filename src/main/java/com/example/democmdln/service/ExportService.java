package com.example.democmdln.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDateTime;

@Service
public class ExportService {

    @Value("${inputfile}")
    private String inputfile;

    @Value("${outputfile}")
    private String outputfile;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    private static final String HEADER = "STATE,MIN_BALANCE,MAX_BALANCE,MEAN_BALANCE,TOTAL_BALANCE";

    private static final String SQL_SELECT = "select b.state, min(b.balance) min_balance, max(b.balance) max_balance, avg(b.balance) mean_balance, sum(b.balance) total_balance \n" +
                                                "from balances b \n" +
                                                "where b.createtime = ? and b.filename = ? \n" +
                                                "group by b.state \n" +
                                                "order by b.state";

    public String processExport(LocalDateTime runDateTime) {
        int iCnt = 0;
        File file;
        FileWriter fileWriter = null;
        FileOutputStream fos = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        String line;

        System.out.println("outputfile: " + outputfile);
        try {
            try {
                file = ResourceUtils.getFile(outputfile);
            } catch (FileNotFoundException fe) {
                file = new File(outputfile);
            }

            fileWriter = new FileWriter(file);
            fileWriter.write(HEADER + "\n");

            conn = DriverManager.getConnection(datasourceUrl, datasourceUsername, datasourcePassword);
            pstmt = conn.prepareStatement(SQL_SELECT);
            pstmt.setTimestamp(1, Timestamp.valueOf(runDateTime));
            pstmt.setString(2, inputfile);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                iCnt = iCnt + 1;

                line = resultSet.getString("STATE") + "," +
                        resultSet.getBigDecimal("MIN_BALANCE") + "," +
                        resultSet.getBigDecimal("MAX_BALANCE") + "," +
                        resultSet.getBigDecimal("MEAN_BALANCE") .setScale(2, RoundingMode.HALF_UP)+ "," +
                        resultSet.getBigDecimal("TOTAL_BALANCE");

                fileWriter.write(line + "\n");
                fileWriter.flush();

                System.out.println(line);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (fileWriter != null) {  try {  fileWriter.close();  } catch (Exception e1) { }  }
            if (pstmt != null) {  try {  pstmt.close();  } catch (Exception e2) { }  }
            if (conn != null) {  try {  conn.close();  } catch (Exception e2) { }  }
        }



        return "Exported: " + iCnt;
    }



}
