package com.example.democmdln.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;

@Service
public class ImportService {

    @Value("${inputfile}")
    private String inputfile;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    private static final String SQL_INSERT = "INSERT INTO BALANCES (first_name, last_name, address, city, state, zip, phone, balance, createtime, filename) " +
                                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public String processImport(LocalDateTime runDateTime) {
        int iCnt = 0;
        int iProc = 0;
        File file;
        FileInputStream fis = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        BigDecimal bd = null;

        System.out.println("inputfile: " + inputfile);
        try {
            file = ResourceUtils.getFile(inputfile);
            fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            String line;

            conn = DriverManager.getConnection(datasourceUrl, datasourceUsername, datasourcePassword);
            pstmt = conn.prepareStatement(SQL_INSERT);

            while ((line = br.readLine()) != null) {
                iCnt = iCnt + 1;
                System.out.println(line);
                if (iCnt > 1) {
                    String[] col = line.split("\\,", -1);

                    pstmt.setString(1, col[0]);
                    pstmt.setString(2, col[1]);
                    pstmt.setString(3, col[2]);
                    pstmt.setString(4, col[3]);
                    pstmt.setString(5, col[4]);
                    pstmt.setString(6, col[5]);
                    pstmt.setString(7, col[6]);
                    bd = new BigDecimal(col[7].replaceAll("[$,]",""));
                    pstmt.setBigDecimal(8, bd);
                    pstmt.setTimestamp(9, Timestamp.valueOf(runDateTime));
                    pstmt.setString(10, inputfile);

                    int row = pstmt.executeUpdate();
                    iProc = iProc + 1;
                }

            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (fis != null) {  try {  fis.close();  } catch (Exception e1) { }  }
            if (pstmt != null) {  try {  pstmt.close();  } catch (Exception e2) { }  }
            if (conn != null) {  try {  conn.close();  } catch (Exception e3) { }  }
        }



        return "Processed: " + iCnt + " Imported to database: " + iProc;
    }



}
