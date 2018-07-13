package com.zm.provider.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ResourceUtils;

import com.zm.provider.entity.Email;
import com.zm.provider.service.SendEmailService;

import freemarker.template.Configuration;
import freemarker.template.Template;

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
	
	@Override
	public void sendFileMail(Email email) throws Exception {
		LOGGER.info("准备发送附件给指定邮箱账户");
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
		String time = LocalDate.now().format(df);
        String fileName = "11"+time+"03.txt";
		String [] receiveArray = email.getEmail();
		MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("m18701365103_4@163.com");
        helper.setTo(receiveArray[0]);
        helper.setSubject("301对账文件"+fileName);
        helper.setText("301对账文件见附件",true);
        String filePath = "/externalcustomer/bjccb/1051100110059397/readFiles/"+fileName;
        FTPClient ftp = new FTPClient();
        ftp.connect("59.151.37.187", 21);
        ftp.login("ftp", "d45ec43f");
        File localFile = new File("/apps/share/payplus/yqt/a.txt");
        OutputStream outputStream = new FileOutputStream(localFile);
        ftp.retrieveFile(filePath, outputStream);
        ftp.disconnect();
        helper.addAttachment(fileName, localFile);
        mailSender.send(message);
        localFile.delete();
	}

	@Override
	public void sendHtmlSimpleMail() throws Exception {
		LOGGER.info("准备发送HTML邮件");
		MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //这里设置企业邮箱突然间不可以用了，可能是端口拦截了，可能是企业邮箱拦截了
        helper.setFrom("2413172711@qq.com");
        helper.setTo("2413172711@qq.com");
        helper.setSubject("HTML主题");
        helper.setText("<html><body><img src=\"cid:springcloud\" >"
        		+ "</br>"
        		+"哈哈!"
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

	@Override
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
}
