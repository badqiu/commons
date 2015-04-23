package com.github.rapid.common.rpc.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang.UnhandledException;
import org.apache.commons.lang.exception.NestableRuntimeException;
/**
 * 
 * 用于参数的转义,转义字符如下:
 * \001 => ,
 * \002 => ;
 * \003 => :
 * \\ => \
 * 
 * @author badqiu
 *
 */
public class ParameterEscapeUtil {

	public static String unescapeParameter(String str) {
		if (str == null) {
            return null;
        }
        try {
            StringWriter writer = new StringWriter(str.length());
            unescapeParameter(writer, str);
            return writer.toString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            throw new UnhandledException(ioe);
        }
	}
	
	public static void unescapeParameter(Writer out,String str) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (str == null) {
            return;
        }
        int sz = str.length();
        StringBuffer unicode = new StringBuffer(4);
        boolean hadSlash = false;
        boolean inUnicode = false;
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            if (inUnicode) {
                // if in unicode, then we're reading unicode
                // values in somehow
                unicode.append(ch);
                if (unicode.length() == 4) {
                    // unicode now contains the four hex digits
                    // which represents our unicode character
                    try {
                        int value = Integer.parseInt(unicode.toString(), 16);
                        out.write((char) value);
                        unicode.setLength(0);
                        inUnicode = false;
                        hadSlash = false;
                    } catch (NumberFormatException nfe) {
                        throw new NestableRuntimeException("Unable to parse unicode value: " + unicode, nfe);
                    }
                }
                continue;
            }
            if (hadSlash) {
                // handle an escaped value
                hadSlash = false;
                switch (ch) {
                    case '\\':
                        out.write('\\');
                        break;
                    case '\001':
                        out.write(',');
                        break;
                    case '\002':
                        out.write(';');
                        break;  
                    case '\003':
                        out.write(':');
                        break;                               
                    default :
                        out.write(ch);
                        break;
                }
                continue;
            } else if (ch == '\\') {
                hadSlash = true;
                continue;
            }
            out.write(ch);
        }
        if (hadSlash) {
            // then we're in the weird case of a \ at the end of the
            // string, let's output it anyway.
            out.write('\\');
        }
	}
	
	public static String escapeParameter(String str) {
		if(str == null)
			return null;
		try {
            StringWriter writer = new StringWriter(str.length());
            escapeParameter(writer, str,false,false);
            return writer.toString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            throw new UnhandledException(ioe);
        }
	}
	
    /**
     * <p>Worker method for the {@link #escapeJavaScript(String)} method.</p>
     * 
     * @param out write to receieve the escaped string
     * @param str String to escape values in, may be null
     * @param escapeSingleQuote escapes single quotes if <code>true</code>
     * @param escapeForwardSlash
     * @throws IOException if an IOException occurs
     */
    private static void escapeParameter(Writer out, String str, boolean escapeSingleQuote,
            boolean escapeForwardSlash) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (str == null) {
            return;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            switch (ch) {
                case '\'' :
                    if (escapeSingleQuote) {
                        out.write('\\');
                    }
                    out.write('\'');
                    break;
                case '\\' :
                    out.write("\\\\");
                    break;
                case ',' :
                    out.write("\\\001");
                    break;    
                case ';' :
                    out.write("\\\002");
                    break;
                case ':' :
                    out.write("\\\003");
                    break;                    
                case '/' :
                    if (escapeForwardSlash) {
                        out.write('\\');
                    }
                    out.write('/');
                    break;
                default :
                    out.write(ch);
                    break;
            }
        }
    }
    
    /**
     * <p>Returns an upper case hexadecimal <code>String</code> for the given
     * character.</p>
     * 
     * @param ch The character to convert.
     * @return An upper case hexadecimal <code>String</code>
     */
    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
    }
}
