package com.jacky.backend.testtop5list.model;

import com.jacky.backend.testtop5list.OpHelper;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;
import java.util.logging.Logger;

public class MltestModelTest {
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
    private static Logger LOGGER = Logger.getLogger(MltestModelTest.class.getName());
    @Test
    public void testGetWebsiteFilter1() {
        MltestModel model = new MltestModel("http://private-1de182-mamtrialrankingadjustments4.apiary-mock.com/exclusions");
        assertEquals(0,
                model.getWebsiteFilters(OpHelper.getDate("2016-06-20")).size());
        assertEquals("facebook.com",
                model.getWebsiteFilters(OpHelper.getDate("2016-12-01")).get(0).getWebsite());
        assertEquals("facebook.com",
                model.getWebsiteFilters(OpHelper.getDate("2017-03-13")).get(0).getWebsite());
        assertEquals(0,
                model.getWebsiteFilters(OpHelper.getDate("2016-11-30")).size());

    }
    @Test
    public void testGetWebsiteFilter2() {
        MltestModel model = new MltestModel("http://private-1de182-mamtrialrankingadjustments4.apiary-mock.com/exclusions");
        assertEquals(0,
                model.getWebsiteFilters(OpHelper.getDate("2016-03-11")).size());
        assertEquals("google.com",
                model.getWebsiteFilters(OpHelper.getDate("2016-03-12")).get(0).getWebsite());
        assertEquals("google.com",
                model.getWebsiteFilters(OpHelper.getDate("2016-03-13")).get(0).getWebsite());
        assertEquals("google.com",
                model.getWebsiteFilters(OpHelper.getDate("2016-03-14")).get(0).getWebsite());
        assertEquals(0,
                model.getWebsiteFilters(OpHelper.getDate("2016-03-15")).size());

    }
}
