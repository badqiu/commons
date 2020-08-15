package com.github.rapid.common.redis;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

class JedisURIHelper {

  private static final int DEFAULT_DB = 0;

  public static String getPassword(URI uri) {
    String userInfo = uri.getUserInfo();
    if (userInfo != null) {
      return userInfo.split(":", 2)[1];
    }
    return null;
  }

  public static int getDBIndex(URI uri) {
    String[] pathSplit = uri.getPath().split("/", 2);
    if (pathSplit.length > 1) {
      String dbIndexStr = pathSplit[1];
      if (dbIndexStr.isEmpty()) {
        return DEFAULT_DB;
      }
      return Integer.parseInt(dbIndexStr);
    } else {
      return DEFAULT_DB;
    }
  }

  public static boolean isValid(URI uri) {
    if (isEmpty(uri.getScheme()) || isEmpty(uri.getHost()) || uri.getPort() == -1) {
      return false;
    }

    return true;
  }
  
  public static Map parseQueryString(String s) {
		String valArray[] = null;
		if (s == null) {
			throw new IllegalArgumentException("queryString must not null");
		}
		Map ht = new HashMap();
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(s, "&");
		while (st.hasMoreTokens()) {
			String pair = (String) st.nextToken();
			if(pair.trim().length() == 0)
				continue;
			int pos = pair.indexOf('=');
			if (pos == -1) {
				throw new IllegalArgumentException("cannot parse queryString:"+s);
			}
			String key = parseName(pair.substring(0, pos), sb);
			String val = parseName(pair.substring(pos + 1, pair.length()), sb);
			if (ht.containsKey(key)) {
				String oldVals[] = (String[]) ht.get(key);
				valArray = new String[oldVals.length + 1];
				for (int i = 0; i < oldVals.length; i++)
					valArray[i] = oldVals[i];
				valArray[oldVals.length] = val;
			} else {
				valArray = new String[1];
				valArray[0] = val;
			}
			ht.put(key, valArray);
		}
		return fixValueArray2SingleStringObject(ht);

	}
  

	private static Map fixValueArray2SingleStringObject(Map ht) {
		Map result = new HashMap();
		for(Iterator it = ht.entrySet().iterator();it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			String[] valueArray = (String[])entry.getValue();
			if(valueArray == null)
				result.put(entry.getKey(), null);
			else
				result.put(entry.getKey(),valueArray.length == 1 ? valueArray[0] : valueArray);
		}
		return result;
	}

	static private String parseName(String s, StringBuffer sb) {
		sb.setLength(0);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '+':
				sb.append(' ');
				break;
			case '%':
				try {
					sb.append((char) Integer.parseInt(
							s.substring(i + 1, i + 3), 16));
					i += 2;
				} catch (NumberFormatException e) {
					// need to be more specific about illegal arg
					throw new IllegalArgumentException();
				} catch (StringIndexOutOfBoundsException e) {
					String rest = s.substring(i);
					sb.append(rest);
					if (rest.length() == 2)
						i++;
				}

				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

  private static boolean isEmpty(String value) {
    return value == null || value.trim().length() == 0;
  }

}