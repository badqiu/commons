package com.github.rapid.common.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;  
  
public class GroupByUtil {  
  
	public static Map<Object, Object> groupByMultipleLevels(List<Map<String, Object>> rows,boolean reverseOrder, String... groupKeys) {  
		return groupByMultipleLevels(rows,reverseOrder,Arrays.asList(groupKeys));
	}
	
    /**  
     * 根据提供的keys列表对rows进行分组。  
     *  
     * @param rows      需要分组的原始数据列表，每个元素都是一个Map。  
     * @param groupKeys 用于分组的键列表，按分组层级顺序排列。  
     * @return 分组后的多层嵌套Map，其中最后一个层级是List。  
     */  
    private static Map<Object, Object> groupByMultipleLevels(List<Map<String, Object>> rows,boolean reverseOrder, List<String> groupKeys) {  
        if (rows == null || rows.isEmpty() || groupKeys == null || groupKeys.isEmpty()) {  
            return Collections.emptyMap();  
        }
        
        Map<Object, Object> currentMap = newTreeMap(reverseOrder);
        for (Map<String, Object> row : rows) {  
        	Map<Object, Object> tempMap = currentMap;  
            Object tempObj = tempMap;  
  
            for (int i = 0; i < groupKeys.size(); i++) {  
                String key = groupKeys.get(i);  
                Object value = row.get(key);  
  
                // 检查是否到达最后一个分组键  
                boolean isLastKey = i == groupKeys.size() - 1;  
  
                // 如果当前Map中不存在这个键的值作为key的Map/List，则创建并放入  
                if (!tempMap.containsKey(value)) {  
                    if (isLastKey) {  
                        // 如果是最后一个分组键，则创建List  
                        tempMap.put(value, new ArrayList<>());  
                    } else {  
                        // 否则创建新的Map  
                        tempMap.put(value, newTreeMap(reverseOrder));  
                    }  
                }  
  
                // 更新tempMap为下一层级的Map或List  
                tempObj = tempMap.get(value);  
  
                // 如果是最后一个分组键，则将行添加到List中  
                if (isLastKey && tempObj instanceof List) {  
                    ((List<Map<String, Object>>) tempObj).add(row);  
                    // 注意：一旦添加了行，我们就跳出内层循环，因为不需要再进一步分组  
                    break;  
                }else {
                	tempMap = (Map)tempObj;
                }
            }  
        }  
  
        return currentMap;  
    }

	private static Map<Object, Object> newTreeMap(boolean reverseOrder) {
		return reverseOrder ? new TreeMap(Collections.reverseOrder()) : new TreeMap<>();
	}  
    
    // 示例用法  
    public static void main(String[] args) {  
        List<Map<String, Object>> rows = new ArrayList<>(); 
        rows.add(MapUtil.newMap("tdate","2024-01-01","platform","pf_1","model","model_1"));
        rows.add(MapUtil.newMap("tdate","2024-01-01","platform","pf_1","model","model_2"));
        rows.add(MapUtil.newMap("tdate","2024-01-02","platform","pf_2","model","model_3"));
        rows.add(MapUtil.newMap("tdate","2024-01-02","platform","pf_2","model","model_4"));
        rows.add(MapUtil.newMap("tdate","2024-01-02","platform","pf_2","model","model_4"));
        // 假设这里填充了rows数据  
  
        List<String> groupKeys = Arrays.asList("tdate", "platform", "model");  
        Map<Object, Object> grouped = groupByMultipleLevels(rows,false, groupKeys);  
  
        // 打印结果（仅供演示，实际使用时可能需要根据结构做更复杂的处理）  
        System.out.println(grouped);  
    }  
}