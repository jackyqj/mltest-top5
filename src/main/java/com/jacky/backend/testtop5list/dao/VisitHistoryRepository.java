package com.jacky.backend.testtop5list.dao;

import com.jacky.backend.testtop5list.entity.VisitHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Date;

/**
 * @author Jacky Zhang
 * Simple DAO class for VisitHistory manangement
 */
public interface VisitHistoryRepository extends JpaRepository<VisitHistory, Long> {
}
