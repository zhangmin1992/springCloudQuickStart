package com.zm.provider.util.log4j;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

/**
 * 在日志中添加一个写死的字符串888测试
 * @author yp-tc-m-7129
 *
 */
//@Plugin(name = "LogIdPatternConverter", category = PatternConverter.CATEGORY)
//@ConverterKeys({"H"})
//public class LogIdPatternConverter extends LogEventPatternConverter {
//
//    private static final LogIdPatternConverter INSTANCE =
//            new LogIdPatternConverter();
//
//    public static LogIdPatternConverter newInstance(
//            final String[] options) {
//        return INSTANCE;
//    }
//
//    private LogIdPatternConverter(){
//        super("LogId", "logId");
//    }
//
//    @Override
//    public void format(LogEvent event, StringBuilder toAppendTo) {
//    	/*int appendString = (int) (Math.random()*100);
//        toAppendTo.append(appendString);*/
//    	toAppendTo.append("8888");
//    }
//}
