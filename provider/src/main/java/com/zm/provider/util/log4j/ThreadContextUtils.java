package com.zm.provider.util.log4j;

/**
 * 获取线程上下文中参数
 * @author yp-tc-m-7129
 *
 */
public class ThreadContextUtils {

	public static final String KEY_TRANSFER_LEVEL_START_VAL = "1";

	/**
	 * 初始化线程上下文
	 */
	private static ThreadLocal<ThreadContext> threadContext = new ThreadLocal<ThreadContext>() {
		protected ThreadContext initialValue() {
			return null;
		}
	};
	
	/**
	 * 判断本地线程变量是否已经初始化过
	 *
	 * @return
	 */
	public static boolean contextInitialized() {
		return threadContext.get() != null;
	}
	
	/**
	 * 初始化本地线程上下文变量
	 *
	 * @param sourceUID
	 * @param type
	 */
	public static void initContext(String appName, String sourceUID, ThreadContextType type) {
		initContext(appName, sourceUID, type, KEY_TRANSFER_LEVEL_START_VAL);
	}
	
	/**
	 * 初始化本地线程上下文变量
	 *
	 * @param appName
	 * @param sourceUID
	 * @param type
	 * @param transferLevel
	 */
	public static void initContext(String appName, String sourceUID, ThreadContextType type, String transferLevel) {
		ThreadContext context = new ThreadContext(appName, sourceUID, type, transferLevel);
		threadContext.set(context);
	}
	
	/**
	 * 获取线程上下文变量
	 *
	 * @return
	 */
	public static ThreadContext getContext() {
		return threadContext.get();
	}
}
