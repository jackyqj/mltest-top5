package com.jacky.backend.testtop5list.dao;

import com.jacky.backend.testtop5list.entity.VisitHistory;
import org.apache.ibatis.annotations.*;

import com.jacky.backend.testtop5list.entity.Account;

import java.util.Date;
import java.util.List;

/**
 * @author Jacky Zhang
 * DAO class to use MyBatis framework
 */
@Mapper
public interface MyBatisMapper {
    /**
     * Search account by the username passed in
     * @param username The username for searching the user
     * @return Account object related to the username
     */
    @Select("select * from account where username = #{username}")
    Account findByUserName(@Param("username") String username);

    /**
     * List the matched visit history records with the restriction list.
     * @param date Record date to be used when searching
     * @param websites website restriction list to be filtered
     * @param limit records limit passed in
     * @return A list of VisitHistory object
     */
    @Select({"<script>",
            "SELECT *",
            "FROM visit_history",
            "WHERE record_date = #{date} AND website NOT IN",
            "<foreach item='item' index='index' collection='list'",
            "open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "ORDER By visit_count desc limit #{limit}",
            "</script>"})
    @Results({
            @Result(property = "visitCount", column = "visit_count"),
            @Result(property = "processDate", column = "process_date"),
            @Result(property = "recordDate", column = "record_date")
    })
    List<VisitHistory> findByRecordDateWithFilter(@Param("date") Date date, @Param("list") List<String> websites, @Param("limit") int limit);

    /**
     * List the matched visit history records with the record date.
     * @param recordDate Record date to be used when searching
     * @param limit records limit passed in
     * @return A list of VisitHistory object
     */
    @Select({"SELECT *",
            "FROM visit_history",
            "WHERE record_date = #{recordDate} ORDER By visit_count desc limit #{limit}"})
    @Results({
            @Result(property = "visitCount", column = "visit_count"),
            @Result(property = "processDate", column = "process_date"),
            @Result(property = "recordDate", column = "record_date")
    })
    List<VisitHistory> findByRecordDate(@Param("recordDate") Date recordDate, @Param("limit") int limit);

    /**
     * Get the exact matched website from DB by the filter website list, as the website in the exclusion list is mainly the domain
     * @param website the main part of the website to be filtered
     * @return A list of website related to the input
     */
    @Select({"SELECT distinct website from visit_history where website like #{website}"})
    List<String> findRelatedWetsites(@Param("website") String website);
}
