package com.jacky.backend.testtop5list;
import com.jacky.backend.testtop5list.entity.VisitHistory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class OpHelperTest {
    private static Logger LOGGER = Logger.getLogger(OpHelperTest.class.getName());
    @Before
    public void init() {
        File tmpFile = new File("/tmp/tmp.csv");
        try {
            if (!tmpFile.exists()) {
                tmpFile.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFile));
                bw.append("date|website|visit");
                bw.newLine();
                bw.write("2018-01-27|www.google.com|223456");
                bw.newLine();
                bw.write("2018-01-27|www.facebook.com|123456");
                bw.newLine();
                bw.flush();
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testGetDate() {
        assertNotNull("should be able to parse successfully: ", OpHelper.getDate("2018-01-26"));
    }
    @Test
    public void testGetCsvFile() {
        assertEquals("Should get the expected Filename.", "tmp.csv", OpHelper.getCsvFile("/tmp"));
    }
    @Test
    public void testGetVisitHistory() {
        List<VisitHistory> list = OpHelper.extractVisitHistories("/tmp/tmp.csv");
        assertEquals("Should be equals", 2, list.size());
        LOGGER.info("Get list: " + list.toString());
    }
    @After
    public void destroy() {
        File tmpFile = new File("/tmp/tmp.csv");
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
    }
}
