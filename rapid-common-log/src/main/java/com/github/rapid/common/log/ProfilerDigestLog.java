package com.github.rapid.common.log;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;

import com.github.rapid.common.util.Profiler;
import com.github.rapid.common.util.Profiler.Step;

/**
 * 打印摘要日志的工具类
 * 
 * 可以通过 setExceptionDigest() 方法设置自定义的异常摘要打印
 * 
 * @author badqiu
 *
 */
public class ProfilerDigestLog {

	/**
	 * 摘要日志的附加数据格式，默认为: ${clientIp}
	 */
    public static final String KEY_DIGEST_LOG_FORMAT = "digestLogFormat";
    
    public static final String KEY_CLIENT_IP = "clientIp";
    
    private static final String DEFAULT_DIGEST_LOG_FORMAT = "${clientIp},${traceId}";
    
    /**
     * 摘要日志格式
     */
    private static String digestLogFormat = System.getProperty(KEY_DIGEST_LOG_FORMAT,DEFAULT_DIGEST_LOG_FORMAT);
    /**
     * 摘要日志 ThreadLocal
     */
    private static final ThreadLocal<Map<String,Object>> digestLogContext = new ThreadLocal<Map<String,Object>>();
    
    private static ExceptionDigest exceptionDigest = new ExceptionDigest() {
		public String getErrorCode(Throwable e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			return rootCause == null ?  e.getClass().getSimpleName() : rootCause.getClass().getSimpleName() + ARRAY_SEPERATOR + e.getClass().getSimpleName() ;
		}
	};
    
    private static String ARRAY_SEPERATOR = "|";
    public static Map<String,Object> getDigestLogContext() {
    	Map<String,Object> result = digestLogContext.get();
    	if(result == null) {
    		result = new HashMap<String,Object>();
    		digestLogContext.set(result);
    	}
    	return result;
    }
    
    public static void setDigestLogContext(Map<String,Object> context) {
    	digestLogContext.set(context);
    }
    
    public static void clearDigestLogContext() {
    	digestLogContext.set(null);
    }
    
    public static void setExceptionDigest(ExceptionDigest ed) {
    	if(ed == null) throw new IllegalArgumentException("exceptionDigest must be not null");
    	exceptionDigest = ed;
    }
    
    public static void setDigestLogFormat(String digestLogFormat) {
    	ProfilerDigestLog.digestLogFormat = digestLogFormat;
	}
    
	private static String getAppendDigestLog() {
		Map context = getDigestLogContextWithMDC();
		if(context == null || context.isEmpty()) {
			return "";
		}
		
		String format = (String)context.get(KEY_DIGEST_LOG_FORMAT);
		if(StringUtils.isBlank(format)) {
			format = digestLogFormat;
		}
		StrSubstitutor substitutor = new StrSubstitutor(new DefaultValueMapStrLookup(context));
		return substitutor.replace(format);
	}

	private static Map getDigestLogContextWithMDC() {
		Map context = new HashMap();
		if(LoggerUtil.slf4jAvailable) {
			Map slf4jMDC = org.slf4j.MDC.getCopyOfContextMap();
			if(slf4jMDC != null) {
				context.putAll(slf4jMDC);
			}
		}
		
		if(LoggerUtil.log4jAvailable) {
			Hashtable log4jMDC = org.apache.log4j.MDC.getContext();
			if(log4jMDC != null) {
				context.putAll(log4jMDC);
			}
		}
		
		Map<String,Object> map = digestLogContext.get();
		if(map != null) {
			context.putAll(map);
		}
		return context;
	}
	
	public static String getDigestLog() {
		return getDigestLog("");
	}
	
	/**
	 * 
	 * @param appendDigestInfo 附加的摘要日志，通过逗号分隔
	 * @return
	 */
	public static String getDigestLog(final String appendDigestInfo) {
		Step step = Profiler.getStep();
		if(step == null) {
			return "";
		}
		
		final StringBuilder sb = new StringBuilder();
		visitAllChild(step,new StepVisitor() {
			public void visit(Step step) {
				sb.append(getDigestLog(step));
				if(StringUtils.isNotEmpty(appendDigestInfo)) {
					sb.append(",").append(appendDigestInfo);
				}
				sb.append("\n");
			}
		});
		return sb.substring(0,sb.length()-1);
	}

	private static void visitAllChild(Step step,StepVisitor visitor) {
		visitor.visit(step);
		for(Step subStep : step.getSubStepList()) {
			visitAllChild(subStep,visitor);
		}
	}

	private interface StepVisitor {
		void visit(Step step);
	}
	
    // 返回: 动作时间，耗时(单位毫秒)，动作ID，成功标志(Y为成功，其它为错误)，loopCount,resultSize
	private static String getDigestLog(Step step) {
		String appendDigestLog = getAppendDigestLog();
		long printLoogCount = step.getLoopCount() <= 0 ? 0 : step.getLoopCount();
		return new Timestamp(step.getStartTime()) + "," + step.getDuration()  + "," + printLoogCount + ","  + step.getResultSize()  + "," + getErrorCode(step.getException()) + "," + getInheritanceMessage(step,ARRAY_SEPERATOR) + "," + appendDigestLog;
	}

	/**
	 * 得到有继承层次结局的message,将 parent step打印在前面 
	 * @return
	 */
	private static String getInheritanceMessage(Step step,String seperator) {
		StringBuilder sb = new StringBuilder();
		Stack<Step> stack = new Stack<Step>();
		Step parent = step;
		while(parent != null) {
			stack.push(parent);
			parent = parent.getParentStep();
		}
		
		boolean isFirst = true;
		for(Step s  = null ; !stack.empty() ;) {
			s  = stack.pop();
			String message2 = s.getMessage();
			if(message2 == null) {
				continue;
			}
			
			if(isFirst) {
				sb.append(message2);
				isFirst = false;
			}else {
				sb.append(seperator);
				sb.append(message2);
			}
		}
		
		return sb.toString();
	}

	private static String getErrorCode(Throwable e) {
		if(e == null) return "Y";
		return exceptionDigest.getErrorCode(e);
	}
	
	/**
     * Lookup implementation that uses a Map.
     */
    static class DefaultValueMapStrLookup extends StrLookup {

        /** Map keys are variable names and value. */
        private final Map map;

        /**
         * Creates a new instance backed by a Map.
         *
         * @param map  the map of keys to values, may be null
         */
        DefaultValueMapStrLookup(Map map) {
            this.map = map;
        }

        /**
         * Looks up a String key to a String value using the map.
         * <p>
         * If the map is null, then null is returned.
         * The map result object is converted to a string using toString().
         *
         * @param key  the key to be looked up, may be null
         * @return the matching value, null if no match
         */
        public String lookup(String key) {
            if (map == null) {
                return "";
            }
            Object obj = map.get(key);
            if (obj == null) {
                return "";
            }
            return obj.toString();
        }
    }
    
    static class LoggerUtil {
    	static boolean slf4jAvailable = false;
    	static {
    		try {
    				Class.forName("org.slf4j.MDC");
    				slf4jAvailable = true;
    		}catch(ClassNotFoundException e) {
    		}
    	}
    	
    	static boolean log4jAvailable = false;
    	static {
    		try {
    				Class.forName("org.apache.log4j.MDC");
    				log4jAvailable = true;
    		}catch(ClassNotFoundException e) {
    		}
    	}
    }
}
