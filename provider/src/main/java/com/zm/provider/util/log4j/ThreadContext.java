package com.zm.provider.util.log4j;

import java.util.Map;
import java.util.UUID;

/**
 * 线程上下文对象
 * @author yp-tc-m-7129
 *
 */
public class ThreadContext {

	/**
	 * 应用名
	 */
	private String appName;

	/**
	 * 线程请求唯一号(和线程号不同，每次运行都产生一个唯一号)
	 */
	private String threadUID;

	/**
	 * 线程请求唯一号(和线程号不同，每次运行都产生一个唯一号)
	 */
	private String newThreadUID;

	/**
	 * 线程启动时间
	 */
	private long threadStartTime;
	/**
	 * 线程类型
	 */
	private ThreadContextType type;
	/**
	 * 线程绑定参数
	 */
	private Map<String, Object> threadValues;

	/**
	 * 调用层级顺序
	 */
	private int transferLevelCount = 1;
	
	private String transferLevel;
	
	private String preTransferLevel;
	
	/**
	 * 获取16个字符的唯一号
	 * @return
	 */
	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 *
	 * 创建一个新的实例 ThreadContext.
	 *
	 * @param a_sourceUID
	 *            外部线程传递过来的唯一号
	 * @param a_type
	 *            线程类型
	 */
	public ThreadContext(String appName, String a_sourceUID, ThreadContextType a_type, String transferLevel) {
		this.appName = appName;
		if (a_sourceUID == null || a_sourceUID.trim().length() == 0) {
			this.threadUID = getUUID();
		} else {
			this.threadUID = a_sourceUID;
			this.newThreadUID = getUUID();
		}
		this.type = a_type;
		this.preTransferLevel = transferLevel;
		this.transferLevel = transferLevel;
	}
	
	/**
	 * 得到线程上下文的全局guid
	 * @return
	 */
	public String getThreadUID() {
		return threadUID;
	}
	
	/**
	 * 得到线程创建方式
	 * @return
	 */
	public ThreadContextType getType() {
		return type;
	}
}
