package com.zm.provider.report;
import java.util.HashMap;
import java.util.Map;

public class ReportParams {
	
	/**
	 * 换行
     */
	private String newLine = CommonConstants.LINE_BREAK;
	
	/**
	 * target文件编码集
     */
	private String charset = "utf-8";
	
	
	/**
	 * 模板名称
	 */
	private String templateName;
	
	/**
	 * 输出目录
	 */
	private String outputDir;
	
	/**
	 * 生成文件名称
	 */
	private String targetFileName;
	
	/**
	 * 模板样式的行
	 */
	private Integer formatRow;
	
	/**
	 * 表达式上下文
	 */
	private Map<String, Object> context = new HashMap<String, Object>();

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public String getTargetFileName() {
		return targetFileName;
	}

	public void setTargetFileName(String targetFileName) {
		this.targetFileName = targetFileName;
	}

	public Integer getFormatRow() {
		return formatRow;
	}

	public void setFormatRow(Integer formatRow) {
		this.formatRow = formatRow;
	}

	public String getNewLine() {
		return newLine;
	}

	public void setNewLine(String newLine) {
		this.newLine = newLine;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}
	
}
