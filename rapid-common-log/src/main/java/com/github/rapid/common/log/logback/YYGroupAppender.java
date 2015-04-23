package com.github.rapid.common.log.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;

import com.github.rapid.common.log.helper.LogbackTriggerUtil;
import com.github.rapid.common.log.log4j.LogEventIntervalTrigger;
import com.github.rapid.common.util.yymsg.YYMsnSender;
import com.github.rapid.common.util.yymsg.YYProtocol.GTopicTextChat;
import com.github.rapid.common.util.yymsg.YYProtocol.MultiRouteGChatMsg;

/**
 * YYAppender.java
 * 2012-12-28 下午05:51:22
 * 
 * @author chenming@yy.com
 * TODO:说点什么呗
 * 
 */
public class YYGroupAppender<ILogEvent> extends OutputStreamAppender<ILogEvent>{

	public YYGroupAppender() {
		setOutputStream(System.out);
	}
	
	@Override
	protected void append(ILogEvent eventObject) {
		if(eventObject instanceof ILoggingEvent) {
			try {
				ILoggingEvent logEvent = (ILoggingEvent)eventObject;
				if(_msgSender == null) {
					_msgSender = new YYMsnSender(yyMsgServerHost, yyMsgServerPort);
				}
				if(logEvent.getLevel().levelInt < _threshold.levelInt) {
					return;
				}
				
				String errorClassName = LogbackTriggerUtil.event2Key(logEvent);
				if(trigger.isSendEvent(errorClassName)) {
					GTopicTextChat text = _messageTemplate.clone();
					text.setMsgtext(LogbackTriggerUtil.simpleEvent2String(logEvent));
					MultiRouteGChatMsg msg = new MultiRouteGChatMsg(groupId, text);
					if(folderId > 0) {
						msg.setFolderId(folderId);
					}
					_msgSender.send(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * YY群ID
	 */
	private int groupId;
	/**
	 * 群内分组ID
	 */
	private int folderId;
	/**
	 * yy Message Server域名
	 * 匡冬杰 说: (09:42:39)
	 *接口已迁移到 101.226.185.11:54321 114.111.163.49:54321
	 * 如果一个地址不通，可以尝试连接另一个
	 */
	private String yyMsgServerHost;
	private int yyMsgServerPort;
	
	/**
	 * 用于在YY群显示的模板
	 * 如:转换异常
	 */
	private GTopicTextChat _messageTemplate = new GTopicTextChat();
	
	/**
	 * 向yy message server发送信息
	 * Ip:
	 * port:
	 */
	private YYMsnSender _msgSender;
	
	private LogEventIntervalTrigger trigger = new LogEventIntervalTrigger();
	
	/**
	 * 日志输出最低级别,如果不配置或配置错误,则为DEBUG
	 */
	private Level _threshold = Level.DEBUG;
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}
	public void setYyMsgServerHost(String yyMsgServerHost) {
		this.yyMsgServerHost = yyMsgServerHost;
	}
	public void setYyMsgServerPort(int yyMsgServerPort) {
		this.yyMsgServerPort = yyMsgServerPort;
	}
	public void setNickName(String nickName) {
		_messageTemplate.setNickname(nickName);
		_messageTemplate.setColor(0x000000FF);
	}
	public void setIntervalSeconds(long intervalSeconds) {
		trigger.setIntervalSeconds(intervalSeconds);
	}
	public void setCrons(String crons) {
		trigger.setCrons(crons);
	}
	public void setActiveByEnvExpr(String envExpr) {
		trigger.setActiveByEnvExpr(envExpr);
	}
	public void setThreshold(String threshold) {
		_threshold = Level.valueOf(threshold);
	}
}
