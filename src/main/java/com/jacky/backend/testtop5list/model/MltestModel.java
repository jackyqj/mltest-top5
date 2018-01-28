package com.jacky.backend.testtop5list.model;

import com.jacky.backend.testtop5list.entity.VisitFilter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Jacky Zhang
 * Model class for loading the filter
 */
public class MltestModel {
    private final static Logger LOGGER = Logger.getLogger(MltestModel.class.getName());
    private String filterUrl;

    public MltestModel(final String url) {
        this.filterUrl = url;
    }

    /**
     * Load the valid website filters from web according to the url
     *
     * @param checkedDate The date user checked for the visit report
     * @return a list of VisitFilter object
     */
    public List<VisitFilter> getWebsiteFilters(final Date checkedDate) {
        List<VisitFilter> filterList = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();

            URL oracle = new URL(this.filterUrl);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            JSONArray jsArray = (JSONArray) parser.parse(in);

            // Loop through each item
            for (Object obj : jsArray) {
                JSONObject tutorials = (JSONObject) obj;
/*
[
    {
        "host": "facebook.com",
        "excludedSince": "2016-12-01"
    },
    {
        "host": "google.com",
        "excludedSince": "2016-03-12",
        "excludedTill": "2016-03-14"
    }
]
 */
                VisitFilter vFilter = new VisitFilter(
                        (String) tutorials.get("host"),
                        (String) tutorials.get("excludedSince"),
                        (String) tutorials.get("excludedTill")
                );
                //Only the filter with the valid date range need to be added to the filter list
                if (isValidFilter(vFilter, checkedDate)) {
                    LOGGER.fine("Filter was added: " + vFilter);
                    filterList.add(vFilter);
                } else {
                    LOGGER.fine("Filter was ignored: " + vFilter);
                }
            }
            in.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            LOGGER.warning("Get exception when loading filter from web." + e.getMessage());
        }
        return filterList;
    }

    /**
     * Check the date range for the filter
     *
     * @param vFilter     the filter to be checked
     * @param checkedDate the date of the report
     * @return true or false
     */
    protected boolean isValidFilter(VisitFilter vFilter, Date checkedDate) {
        if (vFilter.getDateFrom() == null) {
            if (vFilter.getDateTo() == null) {
                return true; //No date range limitation.
            } else {
                return !checkedDate.after(vFilter.getDateTo()); //check date is not after the limitation to date
            }
        } else {
            if (vFilter.getDateTo() == null) {
                return !checkedDate.before(vFilter.getDateFrom()); //check date is not before the limitation from date
            } else {
                return !checkedDate.before(vFilter.getDateFrom()) &&
                        !checkedDate.after(vFilter.getDateTo()); //check date is within the date range
            }
        }
    }
}
