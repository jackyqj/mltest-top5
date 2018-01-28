package com.jacky.backend.testtop5list;


import com.jacky.backend.testtop5list.entity.VisitHistory;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Jacky Zhang
 * Operation helper class to handle common functionalities
 */
public class OpHelper {
    private final static Logger LOGGER = Logger.getLogger(OpHelper.class.getName());

    private final static String DEFAULT_PATTERN = "yyyy-MM-dd";

    private final static SimpleDateFormat SDF = new SimpleDateFormat(DEFAULT_PATTERN);
    private static final String CSV_SEPARATOR = "\\|";

    /**
     * Simple date parsing function
     *
     * @param dateStr input the date in string with pattern yyyy-MM-dd
     * @return the java.util.Date object
     */
    public static Date getDate(final String dateStr) {
        Date date = null;
        try {
            date = SDF.parse(dateStr);
            LOGGER.fine(String.format("convert date %1s to %2s", dateStr, date));
        } catch (ParseException e) {
            LOGGER.warning("Failed to parse date: " + dateStr + ". " + e.getMessage());
        }
        return date;
    }

    /**
     * Function to get the csv filename from the input folder
     *
     * @param folderPath the input folder to be checked
     * @return csv file name, null in case no file, the 1st item will be returned in case there are multiple files
     */
    public static String getCsvFile(final String folderPath) {
        File folder = new File(folderPath);
        String csvFilename = null;
        if (!folder.exists()) {
            LOGGER.warning("Folder is not exist: " + folderPath);
        } else {
            String[] files = folder.list((File dir, String name) -> {
                return name.toLowerCase().endsWith("csv");
            });
            csvFilename = files.length > 0 ? files[0] : null;
        }
        return csvFilename;
    }

    /**
     * Function to extract VisitHistory object from the csv file.
     *
     * @param filePath the csv file to be parsed.
     * @return a list of VisitHistory
     */
    public static List<VisitHistory> extractVisitHistories(final String filePath) {
        List<VisitHistory> vhList = null;
        Long batchId = Calendar.getInstance().getTimeInMillis();
        File inputF = new File(filePath);
        InputStream inputFS = null;
        try {
            inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            // skip the header of the csv
            vhList = br.lines().skip(1).map((line) -> {
                String[] p = line.split(CSV_SEPARATOR);
                LOGGER.info("processing line: " + line);
                //TODO better to check the files before perform the object creation.
                return new VisitHistory(OpHelper.getDate(p[0]), batchId, p[1], Long.parseLong(p[2]));
            }).collect(Collectors.toList());
            br.close();
        } catch (FileNotFoundException e) {
            LOGGER.warning("File is not found for path: " + filePath);
        } catch (IOException e) {
            LOGGER.warning("Exception when trying to access path: " + filePath);
        }
        return vhList;
    }
}
