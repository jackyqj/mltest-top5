package com.jacky.backend.testtop5list.entity;

import lombok.Data;

import java.util.List;

/**
 * @author Jacky Zhang
 * Wrapper class for response to return additional filter inforamtion
 */
@Data
public class ResponseWrapper {
    private List<VisitFilter> filters;
    private List<VisitHistory> histories;

    public List<VisitFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<VisitFilter> filters) {
        this.filters = filters;
    }

    public List<VisitHistory> getHistories() {
        return histories;
    }

    public void setHistories(List<VisitHistory> histories) {
        this.histories = histories;
    }

    public ResponseWrapper(List<VisitFilter> filters, List<VisitHistory> histories) {
        this.filters = filters;
        this.histories = histories;
    }
}
