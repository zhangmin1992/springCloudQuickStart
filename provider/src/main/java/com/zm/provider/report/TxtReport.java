package com.zm.provider.report;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zm.provider.service.impl.ExcelServiceImpl;

public class TxtReport {

	private static final Logger LOGGER = LoggerFactory.getLogger(TxtReport.class);
	
	/**
	 * 加载模板文件
	 * @param params
	 * @return
	 */
	protected InputStream loadTemplateFile(ReportParams params) {
		InputStream template = null;
		String templateName = params.getTemplateName();
		template = Thread.currentThread().getContextClassLoader().getResourceAsStream(templateName);
        if (null == template) {
            throw new RuntimeException("can not load template file " + templateName + " from classpath.");
        }
        return template;
		
	}
	public TxtEngine initReportEngine(ReportParams params, OutputStream target)  {
		// 构建TXT格式参数
        FormatTxt formatTxt = new FormatTxt();
        //行数
        formatTxt.setFormatRow(params.getFormatRow());
        // 设定额外参数
        formatTxt.setContext(params.getContext());
        //设定引擎编码集
        formatTxt.setCharset(params.getCharset());
        formatTxt.setNewLine(params.getNewLine());
        
        // 构造输入流
        BufferedReader template = null;
        InputStreamReader reader = new InputStreamReader(loadTemplateFile(params));
        template = new BufferedReader(reader);
        
        // 构造输出流
        OutputStream targetFile = null;
        if (target != null) {
            targetFile = target;
        } else {
        	try {
        		LOGGER.info("file path = {} fileName = {}", params.getOutputDir(), params.getTargetFileName());
                File path = new File(params.getOutputDir());
                if(!path.exists()) {
                    path.mkdirs();
                }
                targetFile = new BufferedOutputStream(new FileOutputStream(params.getOutputDir() + params.getTargetFileName()));
            } catch (Exception e) {
                throw new RuntimeException("create target file failed.", e);
            }
        }
        
         // 生成引擎
        TxtEngine txtEngine = new TxtEngine(template, targetFile, formatTxt);
        return txtEngine;
	}
}
