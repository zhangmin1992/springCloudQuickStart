package com.zm.provider.service.impl;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zm.provider.report.ReportParams;
import com.zm.provider.report.TxtEngine;
import com.zm.provider.report.TxtReport;
import com.zm.provider.service.ExcelService;

/**
 * 根据txt模板生成excel文件
 * @author yp-tc-m-7129
 *
 */
@Service
public class ExcelServiceImpl implements ExcelService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelServiceImpl.class);
	
	@Override
	public void makeExcel(Collection<?> data) {
		ReportParams params = new ReportParams();
		//模板名称
		params.setTemplateName("MerchantBillTemplate.txt");
		//生成文件的路径
		params.setOutputDir("/apps/share/payplus/yqt/merchant/bill_files/");
		//文件格式
        params.setCharset("GBK");
		//生成文件名称
		params.setTargetFileName("test.xls");
		//设定开始迭代行,要根据模板文件的行数决定
		params.setFormatRow(2);
		
		TxtReport defaultTxtReport = new TxtReport();
		TxtEngine txtEngine = defaultTxtReport.initReportEngine(params, null);
		try {
			LOGGER.info("start to generate csv file");
			txtEngine.process(data);
		} catch (Exception e) {
			LOGGER.error("makeExcel happen exception",e);
		}
		
	}

}
