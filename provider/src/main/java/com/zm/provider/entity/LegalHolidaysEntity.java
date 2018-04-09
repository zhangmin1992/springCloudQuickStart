package com.zm.provider.entity;

import java.time.LocalDate;
public class LegalHolidaysEntity {

	private Long id;

	 /**
	  * 法定节假日
	  */
	private LocalDate holidayDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(LocalDate holidayDate) {
		this.holidayDate = holidayDate;
	}

}
