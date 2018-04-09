package com.zm.provider.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.zm.provider.dao.LegalHolidaysEntityDao;
import com.zm.provider.entity.LegalHolidaysEntity;
import com.zm.provider.service.LegalHolidaysService;
import com.zm.provider.util.LocalDateUtils;

@Service
public class LegalHolidaysServiceImpl implements LegalHolidaysService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LegalHolidaysServiceImpl.class);

    @Autowired
    private LegalHolidaysEntityDao legalHolidaysEntityDao;

    @Override
    public void insertLegalHolidaysByStartAndEndDate(LocalDate startDate, LocalDate endDate) {
        LOGGER.info("准备插入法定假假日,startDate={} endDate={}", startDate, endDate);
        List<LocalDate> dates = LocalDateUtils.getBetweenDatesContainStartAndEnd(startDate, endDate);
        if (dates.size() > 0) {
            for (LocalDate addDate : dates) {
                this.insertLegalHolidaysInfo(addDate);
            }
        }

    }

    public void insertLegalHolidaysInfo(LocalDate date) {
        LegalHolidaysEntity entity = new LegalHolidaysEntity();
        entity.setHolidayDate(date);
        try {
        	legalHolidaysEntityDao.insetLegalHolidaysInfo(entity);
		} catch (DuplicateKeyException exception) {
			LOGGER.info("唯一性索引异常，可忽略不插入，date ={} ",date);
		}
    }   

}
