package com.zm.provider.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface LegalHolidaysService {

	/**
	 * 开始和截止日期(左右包含)内的日期插入表
	 * @param startDate
	 * @param endDate
	 */
	void insertLegalHolidaysByStartAndEndDate(LocalDate startDate,LocalDate endDate);

	/**
	 * 只插入一天日期
	 * @param date
	 */
	void insertLegalHolidaysInfo(LocalDate date);
}
