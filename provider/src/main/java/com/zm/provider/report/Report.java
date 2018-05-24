package com.zm.provider.report;

import java.io.OutputStream;

public interface Report {

	/**
	 * 初始化报表引擎
	 * @param params
	 * @param target
     * @return
     */
	TxtEngine initReportEngine(ReportParams params, OutputStream target);
}
