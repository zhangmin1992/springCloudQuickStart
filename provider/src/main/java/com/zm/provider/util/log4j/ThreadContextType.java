package com.zm.provider.util.log4j;

/**
 * 线程创建方式枚举
 * @author yp-tc-m-7129
 *
 */
public enum ThreadContextType {

	/**
	 * 产品层WEB请求的处理线程
	 */
	WEB,
	/**
	 * 子系统接口请求的处理线程
	 */
	INTERFACE,
	/**
	 * 通过线程组件创建的线程
	 */
	TASK,
	/**
	 * 手动创建的线程
	 */
	MANUAL
}
