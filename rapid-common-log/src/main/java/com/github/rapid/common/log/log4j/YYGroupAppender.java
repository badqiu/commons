package com.github.rapid.common.log.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.github.rapid.common.log.helper.IPUtil;
import com.github.rapid.common.util.yymsg.YYMsnSender;
import com.github.rapid.common.util.yymsg.YYProtocol.GTopicTextChat;
import com.github.rapid.common.util.yymsg.YYProtocol.MultiRouteGChatMsg;

public class YYGroupAppender  extends AppenderSkeleton{
	LogEventIntervalTrigger eventTrigger = new LogEventIntervalTrigger();
	private int groupId;
	private int folderId;
	private String host = "113.107.111.18";
	private int port = YYMsnSender.DEFAULT_PORT;
	
	private YYMsnSender msgSender;
	private GTopicTextChat msgtextTemplate = new GTopicTextChat();
	
	public YYGroupAppender() {
		setNickname("报警");
		setColor(0x000000FF);
	}
	
	public void close() {
		if(msgSender != null) { 
			msgSender.close();
		}
	}

	public boolean requiresLayout() {
		return true;
	}
	
	public void setCrons(String crons) {
		eventTrigger.setCrons(crons);
	}
	
	public void setActiveByEnvExpr(String envExpr) {
		eventTrigger.setActiveByEnvExpr(envExpr);
	}

	public void setIntervalSeconds(long intervalSeconds) {
		eventTrigger.setIntervalSeconds(intervalSeconds);
	}
	
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public void setNickname(String nickname) {
		msgtextTemplate.setNickname(nickname + " " + IPUtil.getIp());
	}

	public void setFontname(String fontname) {
		msgtextTemplate.setFontname(fontname);
	}

	public void setEffect(int effect) {
		msgtextTemplate.setEffect(effect);
	}

	public void setCharset(int charset) {
		msgtextTemplate.setCharset(charset);
	}

	public void setColor(int color) {
		msgtextTemplate.setColor(color);
	}

	public void setHeight(int height) {
		msgtextTemplate.setHeight(height);
	}
	
	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	protected void append(LoggingEvent event) {
		ThrowableInformation throwable = event.getThrowableInformation();
		String errorClassName = null;
		if(throwable!=null) {
			errorClassName = throwable.getThrowable().getClass().getName();
		}
		
		if(eventTrigger.isSendEvent(errorClassName)) {
			sendYYMsg(event);
		}
	}

	private void sendYYMsg(LoggingEvent event) {
		if(msgSender == null) {
			msgSender = new YYMsnSender(host,port);
		}
		if(layout == null) {
			throw new RuntimeException("layout must be not null");
		}
		
		GTopicTextChat text = msgtextTemplate.clone();
		text.setMsgtext(layout.format(event));
		MultiRouteGChatMsg msg = new MultiRouteGChatMsg(groupId,text);
		if(folderId > 0) {
			msg.setFolderId(folderId);
		}
		
		msgSender.send(msg);
	}

}
