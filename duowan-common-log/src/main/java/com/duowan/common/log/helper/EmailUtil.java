package com.duowan.common.log.helper;

import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * EmailUtil.java 2013-1-26 下午11:20:33
 * 
 * @author chenming@yy.com TODO:说点什么呗
 * 
 */
public class EmailUtil {
	// private Logger iLog = LoggerFactory.getLogger(EmailUtil.java);
	public static final JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
	static {
		// 设定mail server
		senderImpl.setHost("mail.duowan.com");
		senderImpl.setUsername("service@duowan.com"); // 根据自己的情况,设置username
		senderImpl.setPassword("yy!@#$duowan)(*&"); // 根据自己的情况, 设置password
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
		prop.put("mail.smtp.timeout", "25000");
		senderImpl.setJavaMailProperties(prop);
	}

	/**
	 * 同步方式发送邮件
	 * 
	 * @param subject
	 *            => 邮件主题
	 * @param content
	 *            => 邮件内容
	 * @param tos
	 *            => 收件人列表
	 */
	public static void send(String subject, String content, List<String> tos) {
		for (String to : tos) {
			sendHtml(to, subject, content);
		}
	}

	static void sendHtml(String to, String subject, String content) {
		// 建立邮件消息,发送简单邮件和html邮件的区别
		MimeMessage mailMessage = senderImpl.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);
		try {
			messageHelper.setFrom("service@duowan.com");
			// 设置收件人，寄件人
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			// true 表示启动HTML格式的邮件
			messageHelper.setText(content, true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		// 发送邮件
		senderImpl.send(mailMessage);
	}
	
	/**
	 * 异步的方式发送邮件
	 * 
	 * @param subject
	 *            => 邮件主题
	 * @param content
	 *            => 邮件内容
	 * @param tos
	 *            => 收件人列表
	 */
	public static void asynSend(final String subject, final String content, List<String> tos) {
		for (final String to : tos) {
			new Thread() {

                public void run(){
                    sendHtml(to, subject, content);
                }

            }.start();
		}
	}
}
