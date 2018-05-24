package com.zm.provider.report;

import java.util.Collection;
import java.util.Map;

import org.mvel2.templates.CompiledTemplate;

/**
 * txt格式模板
 * @author yp-tc-m-7129
 *
 */
public class FormatTxt {

	/**
	 * 文件编码集
     */
	private String charset;
	
	/**
	 * 模板开始行
	 */
	private Integer formatRow;
	
	/**
	 * 导出的数据
	 */
	private Collection<?> data;

	/**
	 * 表达式上下文
	 */
	private Map<String, Object> context;

	/**
	 * 换行
	 */
	private String newLine = "\n";

	/**
	 * 文件头
     */
	private CompiledTemplate beforeFormatRowCompiledTemplate;

	/**
	 * 文件data
     */
	private CompiledTemplate formatRowCompiledTempate;

	/**
	 * 文件尾
     */
	private CompiledTemplate afterFormatRowCompiledTemplate;

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Integer getFormatRow() {
		return formatRow;
	}

	public void setFormatRow(Integer formatRow) {
		this.formatRow = formatRow;
	}

	public Collection<?> getData() {
		return data;
	}

	public void setData(Collection<?> data) {
		this.data = data;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}

	public String getNewLine() {
		return newLine;
	}

	public void setNewLine(String newLine) {
		this.newLine = newLine;
	}

	public CompiledTemplate getBeforeFormatRowCompiledTemplate() {
		return beforeFormatRowCompiledTemplate;
	}

	public void setBeforeFormatRowCompiledTemplate(
			CompiledTemplate beforeFormatRowCompiledTemplate) {
		this.beforeFormatRowCompiledTemplate = beforeFormatRowCompiledTemplate;
	}

	public CompiledTemplate getFormatRowCompiledTempate() {
		return formatRowCompiledTempate;
	}

	public void setFormatRowCompiledTempate(
			CompiledTemplate formatRowCompiledTempate) {
		this.formatRowCompiledTempate = formatRowCompiledTempate;
	}

	public CompiledTemplate getAfterFormatRowCompiledTemplate() {
		return afterFormatRowCompiledTemplate;
	}

	public void setAfterFormatRowCompiledTemplate(
			CompiledTemplate afterFormatRowCompiledTemplate) {
		this.afterFormatRowCompiledTemplate = afterFormatRowCompiledTemplate;
	}
	
}
