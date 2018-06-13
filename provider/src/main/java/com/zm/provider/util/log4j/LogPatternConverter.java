package com.zm.provider.util.log4j;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MultiformatMessage;

/**
 * 有时候需要在日志中实现类似aop的效果，统一加进去某些参数。对于这种需要，log4j2则可以使用插件机制
 * Plugin 表示的是这是一个插件，name是名称，category为PatternConverter.CATEGORY（目前插件只有这个选择）
   ConverterKeys表示的就是自定义的参数,可以多个
 * @author yp-tc-m-7129
 *
 */
@Plugin(name = "LogPatternConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({ "H" })
public class LogPatternConverter extends LogEventPatternConverter {
	
	/**
	 * 检查全局guid是否正确 - GUID[45a696c385f341efbebf05fd0b3b1344] - TYPE[MANUAL] 
	 * @param args
	 */
	/*public static void main(String[] args) {
		System.out.println(getPayplusLogUUID());
	}*/
	
	private static final LogPatternConverter INSTANCE = new LogPatternConverter();

    public static LogPatternConverter newInstance(final String[] options) {
        return INSTANCE;
    }

    private LogPatternConverter(){
        super("LogId", "logId");
    }

	/**
	 * 另外的格式化日志,在日志中加入一个全局guid
	 * 这里有两个参数，LogEvent是系统已经存在的一些可选数据，StringBuilder 表示的是最终的输出字符流。一般都是将自定义的append进去
	 */
	@Override
	public void format(LogEvent event, StringBuilder builder) {
		final Message msg = event.getMessage();
		if (msg != null) {
			String result = msg.getFormattedMessage();
			result = getPayplusLogUUID() + result;
			builder.append(result);
		}
	}
	
	/**
	 * 业务日志全局UUID
	 *
	 * @return
	 */
	protected  String getPayplusLogUUID() {
		StringBuilder builder = new StringBuilder();
		if (!ThreadContextUtils.contextInitialized()) {
			ThreadContextUtils.initContext("applicationName", null, ThreadContextType.MANUAL);
		}
		builder.append("- GUID[");
		builder.append(ThreadContextUtils.getContext().getThreadUID());
		builder.append("] - TYPE[");
		builder.append(ThreadContextUtils.getContext().getType());
		builder.append("] ");
		return builder.toString();
	}

}
