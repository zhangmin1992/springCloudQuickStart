package com.zm.provider.exception;

import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * 业务异常基类，所有业务异常都必须继承于此异常
 *
 * @author: siyu.xu
 * @Date: 2018:01:28 下午9:04
 * @company: 易宝支付(YeePay)
 */

public class PayplusBizException extends RuntimeException{

	private static final long serialVersionUID = -5097768787801034398L;

	/**
	 * 异常ID，用于表示某一异常实例，每一个异常实例都有一个唯一的异常ID
	 */
	protected String id;

	/**
	 * 异常信息，包含必要的上下文业务信息，用于打印日志
	 */
	protected String message;

	/**
	 * 具体异常码，即异常码code的后3位，由各具体异常实例化时自己定义
	 */
	protected String defineCode;
	
	protected String realClassName;

	public PayplusBizException(){}


	protected PayplusBizException(String defineCode) {
		super();
		this.defineCode = defineCode;
		initId();
	}

	private void initId() {
		this.id = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
	}

	public String getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message, Object... args) {
		this.message = MessageFormat.format(message, args);
	}


	public String getDefineCode() {
		return defineCode;
	}
 
	public static <T extends PayplusBizException> T newException(T exception, String message, Object...args){
		if(exception == null){
			throw new RuntimeException("no exception instance specified");
		}
		try {
			Constructor constructor = exception.getClass().getDeclaredConstructor(String.class);
			constructor.setAccessible(true);
			T newException = (T)constructor.newInstance(exception.getDefineCode());
			newException.setMessage(message, args);
			return newException;
		} catch (Throwable e) {
			throw new RuntimeException("create exception instance fail : "+e.getMessage(), e);
		}
	}
	
	/**
	 * 比较异常的class和defineCode是否相同
	 * @param e
	 * @return
	 */
	public boolean codeEquals(PayplusBizException e){
		if(e == null){
			return false;
		}
		if(!e.getClass().equals(this.getClass())){
			return false;
		}
		if(!e.getDefineCode().equals(getDefineCode())){
			return false;
		}
		return true;
	}
	
	public PayplusBizException upcasting() {
		if(this.getClass().equals(PayplusBizException.class)){
			return this;
		}
		PayplusBizException superexception = new PayplusBizException(this.defineCode);
		superexception.message = this.message;
		superexception.realClassName = this.getClass().getName();
		superexception.id = this.id;
		superexception.setStackTrace(this.getStackTrace());
		return superexception;
	}
	
	public PayplusBizException downcasting(){
		if(this.realClassName == null || PayplusBizException.class.getName().equals(this.realClassName)){
			return this;
		}
		Class clz = null;
		try{
			clz = Class.forName(this.realClassName);
		}catch(Exception e){
		}
		if(clz == null){
			return this;
		}
		try {
			Constructor constructor = clz.getDeclaredConstructor(String.class);
			constructor.setAccessible(true);
			PayplusBizException newException = (PayplusBizException)constructor.newInstance(this.defineCode);
			newException.message = this.message;
			newException.id = this.id;
			newException.setStackTrace(this.getStackTrace());
			return newException;
		} catch (Throwable e) {
			return this;
		}
	}

	public String getRealClassName() {
		if(realClassName==null){
			return this.getClass().getName();
		}
		return realClassName;
	}
	
	public StackTraceElement[] getCoreStackTrace(){
		List<StackTraceElement> list = new ArrayList<StackTraceElement>();
		for(StackTraceElement traceEle : getStackTrace()){
			if(traceEle.getClassName().startsWith("com.payplus")){
				list.add(traceEle);
			}
		}
		StackTraceElement[] stackTrace = new StackTraceElement[list.size()];
		return list.toArray(stackTrace);
	}
	
	public String getCoreStackTraceStr(){
		StringBuffer sb = new StringBuffer();
		for(StackTraceElement traceEle : getCoreStackTrace()){
			sb.append("\n"+traceEle.toString());
		}
		return sb.toString();
	}
}
