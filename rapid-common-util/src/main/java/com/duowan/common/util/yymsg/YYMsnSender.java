package com.duowan.common.util.yymsg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.duowan.common.util.yymsg.YYProtocol.GTopicTextChat;
import com.duowan.common.util.yymsg.YYProtocol.Header;
import com.duowan.common.util.yymsg.YYProtocol.MultiRouteGChatMsg;


/**
 * YY消息发送器
 * 
 * 电信：
 * 	114.111.163.49：54321
 * 	114.111.165.49：54321
 * 
 * 网通：
 * 	101.226.185.11：54321
 * 	140.207.209.11：54321
 * 	 
* */
 
public class YYMsnSender {
	public static final int DEFAULT_PORT = 54321;
	private String host;
	private int port = DEFAULT_PORT;
	private Socket socket = null;
	private Logger logger = LoggerFactory.getLogger(YYMsnSender.class);
	

	public YYMsnSender(String host) {
		this(host,DEFAULT_PORT);
	}
	
	public YYMsnSender(String host, int port) {
		Assert.hasText(host,"host must be not empty");
		Assert.isTrue(port > 0,"port > 0");
		this.host = host;
		this.port = port;
	}
	
	public void connect() {
		try {
			socket = new Socket(host, port);
			logger.info("connect to yy group msg server => "+host+":"+port);
		}catch(IOException e) {
			throw new RuntimeException("connect to yy msg host error,host:"+host+" port:"+port);
		}
	}
	
	/**
	 * 用于发送yy组消息
	 * @param groupId  YY群ID(内部ID)
	 * @param msg 消息
	 * @param nickname 呢称
	 */
	public void sendGroupMsg(int groupId,String nickname,String msg) {
		send(new MultiRouteGChatMsg(groupId,new GTopicTextChat(nickname,msg)));
	}
	
	/**
	 * 用于发送yy组消息
	 * @param groupMsg
	 */
	public void send(MultiRouteGChatMsg groupMsg)  {
		try {
			byte[] msgBytes = groupMsg.toBytes();
			Header header = new Header(groupMsg.uri,msgBytes.length);
			
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			result.write(header.toBytes());
			result.write(msgBytes);
			sendBytes(result.toByteArray());
			logger.info("sended yy group msg,groupId:"+groupMsg.getGroupId()+" folderId:"+groupMsg.getFolderId()+" nickname:"+groupMsg.getMsgText().getNickname()+" msgtext:"+groupMsg.getMsgText().getMsgtext());
		}catch(IOException e) {
			throw new RuntimeException("send has error",e);
		}
	}

	private void sendBytes(byte[] msgBytes)  {
		if(socket == null || socket.isClosed() || socket.isConnected()) {
			connect();
		}
		try {
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write(msgBytes);
			outputStream.flush();
		}catch(IOException e) {
			closeSocket(socket);
			logger.warn("close socket from "+host+":"+port+" after sendBytes() error, cause:"+e);
			throw new RuntimeException("sendBytes error",e);
		}
	}

	private static void closeSocket(Socket socket) {
		try {
			if(socket != null) socket.close();
		}catch(IOException e) {
			//ignore
		}
	}
	
	public void close() {
		closeSocket(socket);
	}


	public static void main(String[] args) {
		YYMsnSender msg = new YYMsnSender("114.111.163.49");
		msg.sendGroupMsg(4601261,"nickname","content,my name is haimeimei, 中国人民银行 http://www.163.com");
	}

}
