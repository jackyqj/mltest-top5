package com.jacky.backend.testtop5list.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Jacky Zhang
 * Entity class for visit history record
 */
@Data
@ToString(exclude = "password")
@Entity
public class VisitHistory {

    @Id
    @GeneratedValue
    private Long id;

    private Date recordDate;

    @JsonIgnore
    private Long batchId;

    private String website;

    private Long visitCount;

    private Date processDate;

    public VisitHistory(Date recordDate, Long batchId, String website, Long visitCount) {
        this.recordDate = recordDate;
        this.batchId = batchId;
        this.website = website;
        this.visitCount = visitCount;
        this.processDate = Calendar.getInstance().getTime();

    }

    public String getWebsite() {
        return website;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    VisitHistory() {
        //jpa only
    }
}
