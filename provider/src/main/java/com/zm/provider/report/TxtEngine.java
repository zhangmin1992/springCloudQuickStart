package com.zm.provider.report;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zm.provider.service.impl.ExcelServiceImpl;
import com.zm.provider.util.CheckUtils;

public class TxtEngine {

	private static final Logger logger = LoggerFactory.getLogger(ExcelServiceImpl.class);
	
	/**
     * 模板
     */
    private BufferedReader template;

    /**
     * 输出文件
     */
    private OutputStream target;

    /**
     * txt格式
     */
    private FormatTxt formatTxt;

    public TxtEngine(BufferedReader template, OutputStream target, FormatTxt formatTxt) {
        this.template = template;
        this.target = target;
        this.formatTxt = formatTxt;
    }
    
    /**
     * 集合内数据处理生成文件
     * @param data
     * @throws IOException
     */
    public void process(Collection<?> data) throws IOException {
    	logger.debug("start to generate txt file.");

        // 处理模板
        processTemplate();

        // 迭代前输出
        processContentBeforeFormatRow();

        // 处理数据
        processData(data);

        // 迭代后输出
        processContentAfterFormatRow();

        destroyEngine();

        logger.debug("generate txt file successfully.");
    }
    
    /**
     * 描述： 处理模板
     *
     * @throws IOException
     */
    public void processTemplate() throws IOException {
        String lineContent = null;
        int line = 1;
        StringBuilder beforeFormatRowContent = new StringBuilder();
        String formatRowContent = null;
        StringBuilder afterFormatRowContent = new StringBuilder();
        while ((lineContent = template.readLine()) != null) {
            if (line < formatTxt.getFormatRow()) {
                beforeFormatRowContent.append(lineContent).append(formatTxt.getNewLine());
            } else if (line == formatTxt.getFormatRow()) {
                formatRowContent = lineContent + formatTxt.getNewLine();
            } else {
                afterFormatRowContent.append(lineContent).append(formatTxt.getNewLine());
            }
            line++;
        }

        // 预编译模板
        try {

            // 预编译模板行
            if (!CheckUtils.isEmpty(formatRowContent)) {
                CompiledTemplate formatRowCompiledTempate = TemplateCompiler.compileTemplate(formatRowContent);
                formatTxt.setFormatRowCompiledTempate(formatRowCompiledTempate);
            } else {
                throw new RuntimeException("can not get txt tempate format row content");
            }

            // 预编译迭代行之前的模板
            if (beforeFormatRowContent.length() != 0) {
                CompiledTemplate beforeFormatRowCompiledTemplate = TemplateCompiler.compileTemplate(beforeFormatRowContent);
                formatTxt.setBeforeFormatRowCompiledTemplate(beforeFormatRowCompiledTemplate);
            }

            // 预编译迭代行之后的模板
            if (afterFormatRowContent.length() != 0) {
                CompiledTemplate afterFormatRowCompiledTemplate = TemplateCompiler.compileTemplate(afterFormatRowContent);
                formatTxt.setAfterFormatRowCompiledTemplate(afterFormatRowCompiledTemplate);
            }
        } catch (Exception e) {
            logger.debug("compiled txt template failed.", e);
        }
    }
    
    /**
     * 描述： 处理数据
     *
     * @throws IOException
     */
    public void processData(Collection<?> datas) throws IOException {
        Collection<?> data = datas;
        Iterator<?> rowDataIterator = data.iterator();
        long iterateIndex = 1;
        Map<String, Object> context = formatTxt.getContext();

        CompiledTemplate formatRowCompiledTemplate = formatTxt.getFormatRowCompiledTempate();
        if (formatRowCompiledTemplate == null) {
            throw new RuntimeException("can not get txt report format row compiled template.");
        }

        while (rowDataIterator.hasNext()) {
            Object rowData = rowDataIterator.next();

            context.put(CommonConstants.ITERATE_INDEX, iterateIndex++);
            Object output = MVELUtils.executeTemplate(formatRowCompiledTemplate, rowData, context);
            if (output != null) {
                try {
                    target.write(output.toString().getBytes(formatTxt.getCharset()));
                } finally {
                    target.flush();
                }
            }
        }
    }
    
    /**
     * 描述： 迭代前输出
     *
     * @throws IOException
     */
    public void processContentBeforeFormatRow() throws IOException {

        if (formatTxt.getBeforeFormatRowCompiledTemplate() == null) {
            return;
        } else {
            Object output = MVELUtils.executeTemplate(formatTxt.getBeforeFormatRowCompiledTemplate(), null, formatTxt.getContext());
            if (output != null) {
                try {
                    target.write(output.toString().getBytes(formatTxt.getCharset()));
                } finally {
                    target.flush();
                }
            }
        }

    }
    
    /**
     * 描述： 迭代后输出
     *
     * @throws IOException
     */
    public void processContentAfterFormatRow() throws IOException {
        if (formatTxt.getAfterFormatRowCompiledTemplate() == null) {
            return;
        } else {
            Object output = MVELUtils.executeTemplate(formatTxt.getAfterFormatRowCompiledTemplate(), null, formatTxt.getContext());
            if (output != null) {
                try {
                    target.write(output.toString().getBytes(formatTxt.getCharset()));
                } finally {
                    target.flush();
                }
            }
        }
    }
    
    /**
     * 关闭
     */
    public void destroyEngine() {
        try {
            this.target.flush();
            this.template.close();
            this.target.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
