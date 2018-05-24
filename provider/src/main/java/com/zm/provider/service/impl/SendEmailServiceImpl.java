package com.zm.provider.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.zm.provider.entity.Email;
import com.zm.provider.service.SendEmailService;

import freemarker.template.Template;
import freemarker.template.Configuration;

@Service
public class SendEmailServiceImpl implements SendEmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailServiceImpl.class);
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	private Configuration configuration;
	
	//配置文件中的发送方
	@Value("${spring.mail.username}")
	private String sender;
	
	public void sendSimpleMail(Email email) throws Exception {
		LOGGER.info("准备发送简单邮件");
		try {
			String [] receiveArray = email.getEmail();
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setFrom(sender);
	        message.setSubject(email.getSubject());
	        message.setTo(receiveArray);
	        message.setText(email.getContent());
	        mailSender.send(message);
		} catch (Exception e) {
			LOGGER.error("发生未知异常", e);
		}
		
	}

	@Override
	public void sendHtmlSimpleMail() throws Exception {
		LOGGER.info("准备发送HTML邮件");
		MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("2413172711@qq.com");
        helper.setTo("2413172711@qq.com");
        helper.setSubject("HTML主题");
        helper.setText("<html><body><img src=\"cid:springcloud\" >"
        		+ "</br>"
        		+"祝我们520快乐!"
        		+ "</body></html>",true);
        // 发送图片
        File file = ResourceUtils.getFile("classpath:static/image/WechatIMG1.jpeg");
        helper.addInline("springcloud", file);
        // 发送附件
        file = ResourceUtils.getFile("classpath:static/file/a.txt");
        helper.addAttachment("附件名称", file);
        mailSender.send(message);
		
	}
	
	public void sendFreemarker() throws Exception {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom("2413172711@qq.com");
		helper.setTo("2413172711@qq.com");
		helper.setSubject("FREEMARKER主题邮件");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("content", "这是邮件内容");
		model.put("path", "");
		Template template = configuration.getTemplate("welcome.flt");
		String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		helper.setText(text, true);
		mailSender.send(message);
	}
}
