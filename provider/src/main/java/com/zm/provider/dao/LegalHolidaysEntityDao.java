package com.zm.provider.dao;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Param;

import com.zm.provider.entity.LegalHolidaysEntity;

public interface LegalHolidaysEntityDao {

	/**
	 * 插入单挑非工作日
	 * @param entity
	 */
	void insetLegalHolidaysInfo(LegalHolidaysEntity entity);
	
	/**
	 * 得到大于指定时间的最小的非工作日信息
	 * @param date
	 * @return
	 */
	LegalHolidaysEntity getLegalHolidaysInfoByDate(@Param("holidayDate") LocalDate date);
	
	/**
	 * 指定日期是否是非工作日
	 * @param date
	 * @return
	 */
	LegalHolidaysEntity getLegalHolidaysInfo(@Param("holidayDate") LocalDate date);
}
