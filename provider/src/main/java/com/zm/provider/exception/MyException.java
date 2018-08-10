package com.zm.provider.exception;

public class MyException extends PayplusBizException {
	
	/**
     * 未知异常
     */
    public static MyException REMIT_UNKOWN_EXCEPTION = new MyException("000001");
    
    /**
     * 请求过于频繁
     */
    public static MyException REQUEST_NOT_ALLOWED = new MyException("000002");

    
    public MyException(){}

    public MyException(String defineCode) {
        super(defineCode);
    }

    public MyException newInstance(String message, Object... args) {
    	MyException e = new MyException(this.defineCode);
        e.setMessage(message, args);
        return e;
    }
}
