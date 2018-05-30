package com.zm.provider.service;

import com.zm.provider.entity.Email;

public interface SendEmailService {

	/**
	 * 发送简单邮件
	 * @throws Exception
	 */
	public void sendSimpleMail(Email email) throws Exception;
	
	/**
	 * 发送HTML邮件--代码重复样式难看弃用
	 * @throws Exception
	 */
	public void sendHtmlSimpleMail() throws Exception;
	
	/**
	 * 发送模板文件
	 * @throws Exception
	 */
	public void sendFreemarker() throws Exception;
	
	/**
	 * ftp获取文件然后发送邮件
	 * @param email
	 * @throws Exception
	 */
	public void sendFileMail(Email email) throws Exception;
}
