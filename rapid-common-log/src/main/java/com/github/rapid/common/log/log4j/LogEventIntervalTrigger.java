package com.github.rapid.common.log.log4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.support.CronSequenceGenerator;

/**
 * 提供条件触发判断服务,用于判断当前系统或日志的条件是否成熟,用于发送报警消息
 * 提供: 
 * 1. 某个环境识别 DWENV=prod
 * 2. 时间判断crons表达式
 * 3. 时间间隔判断
 * 
 * @author badqiu
 *
 */
public class LogEventIntervalTrigger {
	
	private long intervalSeconds = 20 * 60;
	private String[] crons = null;
	// Map<exceptionClassName,lastExceptionTime>
	private Map<String,Long> lastExceptionTimeMap = new HashMap<String,Long>();
	private long lastEventMsgSendTime = 0;
	private List<CronSequenceGenerator> cronSequenceGenerators = new ArrayList<CronSequenceGenerator>();
	
	private String activeEnv = null;
	private String activeEnvValue = null;
	
	public LogEventIntervalTrigger() {
	}
	
	public void setCrons(String crons) {
		if(StringUtils.isNotBlank(crons)) {
			this.crons = StringUtils.split(crons,";");
			
			for(int i = 0; i < this.crons.length; i++) {
				String expression = this.crons[i];
				if(StringUtils.isBlank(expression)) {
					continue;
				}
				cronSequenceGenerators.add(new CronSequenceGenerator(expression, TimeZone.getDefault()));
			}
		}
	}

	public void setIntervalSeconds(long intervalSeconds) {
		this.intervalSeconds = intervalSeconds;
	}

	private static String getSystemProperty(String key) {
		String value = System.getenv(key);
		if(value == null) {
			value = System.getProperty(key);
		}
		return value;
	}
	
	public boolean isActiveByENV() {
		if(StringUtils.isBlank(activeEnv) || StringUtils.isBlank(activeEnvValue)) {
			return true;
		}
		String env = getSystemProperty(activeEnv);
		if(activeEnvValue.equals(env)) {
			return true;
		}
		return false;
	}
	
	public boolean isSendEvent(String errorClassName) {
		if(!isActiveByENV()) {
			return false;
		}
		
		if(null == errorClassName) {
			return isSendByMsg();
		} else {
			return isSendByException(errorClassName);
		}
//		return isActiveByENV() && (isSendByException(event) || isSendByMsg(event));
	}

	boolean isSendByMsg() {
		if(isNeedSend(lastEventMsgSendTime)) {
			lastEventMsgSendTime = System.currentTimeMillis();
			return true;
		} else {
			return false;
		}
	}
	
	public void setActiveEnv(String activeEnv) {
		this.activeEnv = activeEnv;
	}

	public void setActiveEnvValue(String activeEnvValue) {
		this.activeEnvValue = activeEnvValue;
	}
	
	public void setActiveByEnvExpr(String envExpr) {
		String[] array = envExpr.split(":");
		setActiveEnv(array[0]);
		setActiveEnvValue(array[1]);
	}

	boolean isSendByException(String errorClassName) {
		// event.getLogger(); //TODO 增加logger的区分 loggerMap<loggerMap,lastExceptionTimeMap>
		Long lastExceptionTime = lastExceptionTimeMap.get(errorClassName);
		if(isNeedSend(lastExceptionTime)) {
			lastExceptionTimeMap.put(errorClassName, System.currentTimeMillis());
			return true;
		}
		return false;
	}
	
	public boolean isNeedSend(Long lastTime) {
		if(lastTime == null || lastTime <= 0) {
			return true;
		}
		if(testTrigger(new Date(lastTime))) {
			return true;
		}
		return false;
	}

	private boolean testTrigger(Date date) {
		if(crons != null) {
			return isTriggerByCron(date,new Date());
		}
		if(intervalSeconds > 0) {
			if(Math.abs(System.currentTimeMillis() - date.getTime()) > intervalSeconds * 1000) {
				return true;
			}
		}
		return false;
	}

	boolean isTriggerByCron(Date date,Date beforeDate) {
		for(CronSequenceGenerator cron : cronSequenceGenerators) {
			Date next = cron.next(date);
			if(next.before(beforeDate)) {
				return true;
			}
		}
		return false;
	}
	
	
}
