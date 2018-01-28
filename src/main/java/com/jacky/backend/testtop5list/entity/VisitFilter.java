package com.jacky.backend.testtop5list.entity;

import com.jacky.backend.testtop5list.OpHelper;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author Jacky Zhang
 * Entity class for the filter
 */
public class VisitFilter {
    private String website;
    private Date dateFrom;
    private Date dateTo;

    public VisitFilter(String website, String fromStr, String toStr) {
        this.website = website;
        if (!StringUtils.isEmpty(fromStr)) {
            this.dateFrom = OpHelper.getDate(fromStr);
        }
        if (!StringUtils.isEmpty(toStr)) {
            this.dateTo = OpHelper.getDate(toStr);
        }
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return "VisitFilter{" +
                "website='" + website + '\'' +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                '}';
    }

}
