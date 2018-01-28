package com.jacky.backend.testtop5list.controller;

import com.jacky.backend.testtop5list.OpHelper;
import com.jacky.backend.testtop5list.dao.AccountRepository;
import com.jacky.backend.testtop5list.dao.MyBatisMapper;
import com.jacky.backend.testtop5list.entity.ResponseWrapper;
import com.jacky.backend.testtop5list.entity.VisitFilter;
import com.jacky.backend.testtop5list.entity.VisitHistory;
import com.jacky.backend.testtop5list.model.MltestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Jacky Zhang
 */
@RestController
@RequestMapping("/histories")
public class VisitHistoryRestController {
    private final static Logger LOGGER = Logger.getLogger(VisitHistoryRestController.class.getName());
    private final MyBatisMapper myBatisMapper;
    private final AccountRepository actRepo;
    @Value("${mltest.filter.url}")
    private String filterUrl;

    @Autowired
    VisitHistoryRestController(AccountRepository accountRepository, MyBatisMapper myBatisMapper) {
        this.actRepo = accountRepository;
        this.myBatisMapper = myBatisMapper;
    }

    /**
     * REST api to search the data from database.
     * 1. Check online whether there is valid exclusion list in the selected date or not
     * 2. In case there is valid filter, further get the exactly matched website from Db for filtering
     *
     * @param recordDate The date to search the visit history records
     * @return List of VisitHistory object, client will retrieved it in json format
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{recordDate}")
    ResponseWrapper listTopHistories(@PathVariable String recordDate) {
        LOGGER.fine("List By Date: " + recordDate);
        MltestModel filterModel = new MltestModel(filterUrl);
        Date checkedDate = OpHelper.getDate(recordDate);
        List<VisitFilter> webList = filterModel.getWebsiteFilters(checkedDate);
        List<VisitHistory> historyList = null;

        // Get the exact matched website from DB by the filter website list
        List<String> matchedWebsiteList = new ArrayList<>();
        for (VisitFilter website : webList) {
            matchedWebsiteList.addAll(myBatisMapper.findRelatedWetsites("%" + website.getWebsite() + "%"));
        }

        if (CollectionUtils.isEmpty(matchedWebsiteList)) {
            LOGGER.fine("Get without any filter.");
            historyList = this.myBatisMapper.findByRecordDate(checkedDate, 5);
        } else {
            LOGGER.fine("Get with filter: " + matchedWebsiteList);
            historyList = this.myBatisMapper.findByRecordDateWithFilter(checkedDate, matchedWebsiteList, 5);
        }
        return new ResponseWrapper(webList, historyList);
    }

}
