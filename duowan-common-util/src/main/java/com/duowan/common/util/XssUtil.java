package com.duowan.common.util;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * 防范XSS攻击的攻击类
 * 
 * @author badqiu
 * @see <a href='https://www.owasp.org/index.php/XSS_Filter_Evasion_Cheat_Sheet'>XSS_Filter_Evasion_Cheat_Sheet</a>
 */
public class XssUtil {
	static private String WRITE_SPACE = " \t\n\r\f";
	
	static private final String[] XSS_STRINGS = new String[]{
    	"</script>",
    	"<script",
    	"src=",
    	"eval(",
    	"expression(",
    	"javascript:",
    	"vbscript:",
    	"onload=",
    	"&#", // &#0000106 , &#106; ...
    	
    	"onAbort=",
    	"onActivate=",
    	"onAfterPrint=",
    	"onAfterUpdate=",
    	"onBeforeActivate=",
    	"onBeforeCopy=",
    	"onBeforeCut=",
    	"onBeforeDeactivate=",
    	"onBeforeEditFocus=",
    	"onBeforePaste=",
    	"onBeforePrint=",
    	"onBeforeUnload=",
    	"onBegin=",
    	"onBlur=",
    	"onBounce=",
    	"onCellChange=",
    	"onChange=",
    	"onClick=",
    	"ontextMenu=",
    	"ontrolSelect=",
    	"onCopy=",
    	"onCut=",
    	"onDataAvailable=",
    	"onDataSetChanged=",
    	"onDataSetComplete=",
    	"onDblClick=",
    	"onDeactivate=",
    	"onDrag=",
    	"onDragEnd=",
    	"onDragLeave=",
    	"onDragEnter=",
    	"onDragOver=",
    	"onDragDrop=",
    	"onDrop=",
    	"onEnd=",
    	"onError=",
    	"onErrorUpdate=",
    	"onFilterChange=",
    	"onFinish=",
    	"onFocus=",
    	"onFocusIn=",
    	"onFocusOut=",
    	"onHelp=",
    	"onKeyDown=",
    	"onKeyPress=",
    	"onKeyUp=",
    	"onLayoutComplete=",
    	"onLoad=",
    	"onLoseCapture=",
    	"onMediaComplete=",
    	"onMediaError=",
    	"onMouseDown=",
    	"onMouseEnter=",
    	"onMouseLeave=",
    	"onMouseMove=",
    	"onMouseOut=",
    	"onMouseOver=",
    	"onMouseUp=",
    	"onMouseWheel=",
    	"onMove=",
    	"onMoveEnd=",
    	"onMoveStart=",
    	"onOutOfSync=",
    	"onPaste=",
    	"onPause=",
    	"onProgress=",
    	"onPropertyChange=",
    	"onReadyStateChange=",
    	"onRepeat=",
    	"onReset=",
    	"onResize=",
    	"onResizeEnd=",
    	"onResizeStart=",
    	"onResume=",
    	"onReverse=",
    	"onRowsEnter=",
    	"onRowExit=",
    	"onRowDelete=",
    	"onRowInserted=",
    	"onScroll=",
    	"onSeek=",
    	"onSelect=",
    	"onChange=",
    	"onSelectStart=",
    	"onStart=",
    	"onStop=",
    	"onSyncRestored=",
    	"onSubmit=",
    	"onTimeError=",
    	"onTrackChange=",
    	"onUnload=",
    	"onURLFlip=",
    	"seekSegmentTime="
    };
	
	static {
		for(int i = 0; i < XSS_STRINGS.length; i++) {
			XSS_STRINGS[i] = XSS_STRINGS[i].toLowerCase();
		}
	}
	
    /**
     * 判断字符串是否有XSS攻击风险
     * @param value
     * @return
     */
    public static boolean hasXSS(String value) {
    	if(value == null) return false;
 
    	if(StringUtils.isAlphanumericSpace(value)) {
    		return false;
    	}
    	
        value = removeWriteSpaceAndLower(value);
 
        // Remove all sections that match a pattern
        for (String chars : XSS_STRINGS){
            if(value.indexOf(chars) >= 0) {
            	return true;
            }
        }
        return false;
    }
    
    /**
     * 检查字符串是否有XSS攻击字符串
     * @param str
     * @throws XssException 有Xss攻击时抛出
     */
    public static void checkXSS(String str) throws XssException {
    	String exception = checkXSSForExceptionMessage(str);
    	if(exception == null) {
    		return;
    	}else {
    		throw new XssException(exception);
    	}
    }
    
    /**
     * 检查request.getParameterMap() 是否有异常攻击
     * @param parameterMap
     * @throws XssException
     */
    public static void checkXSS(Map<String,Object> parameterMap) throws XssException {
    	Set<Map.Entry<String,Object>> entrySet = parameterMap.entrySet();
    	for(Map.Entry<String,Object> entry : entrySet) {
    		String key = (String)entry.getKey();
    		Object value = entry.getValue();
    		if(value == null) {
    			continue;
    		}
    		if(value.getClass().isArray()) {
    			String[] values = (String[])value;
    			for(int i = 0; i < values.length; i++) {
    				checkXSS(values[i],", map key:"+key);
    			}
    		}else {
    			checkXSS(String.valueOf(value),", map key:"+key);
    		}
    	}
    }
    
    private static void checkXSS(String str,String attachExceptionMsg) throws XssException {
    	String exception = checkXSSForExceptionMessage(str);
    	if(exception == null) {
    		return;
    	}else {
    		throw new XssException(exception+attachExceptionMsg);
    	}
    }
    
    private static String checkXSSForExceptionMessage(String str) throws XssException {
    	if(str == null) return null;
 
    	if(StringUtils.isAlphanumericSpace(str)) {
    		return null;
    	}
    	
        str = removeWriteSpaceAndLower(str);
 
        // Remove all sections that match a pattern
        for (String xss : XSS_STRINGS){
            if(str.indexOf(xss) >= 0) {
            	return "xss string:["+str+"] protected by:"+xss;
            }
        }
        return null;
    }
    
	private static String removeWriteSpaceAndLower(String value) {
		value = StringUtils.replaceChars(value, WRITE_SPACE, "").toLowerCase();
		return value;
	}
    
    
	/**
     * Part of HTTP content type header.
     */
	private static final String MULTIPART = "multipart/";
	/**
	 * 判断是否文件上传request
	 * @param request
	 * @return
	 */
	public static final boolean isMultipartContent(
            HttpServletRequest request) {
        if (!"post".equals(request.getMethod().toLowerCase())) {
            return false;
        }
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        if (contentType.toLowerCase().startsWith(MULTIPART)) {
            return true;
        }
        return false;
    }
}
