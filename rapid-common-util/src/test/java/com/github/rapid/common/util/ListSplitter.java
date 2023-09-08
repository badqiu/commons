package com.github.rapid.common.util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

public class ListSplitter {
	
    public static <T> List<List<T>> chunk(List<T> list, int size) {
    	if(CollectionUtils.isEmpty(list)) return Collections.EMPTY_LIST;
    	
        List<List<T>> splitLists = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            int endIndex = Math.min(i + size, list.size());
            List<T> sublist = list.subList(i, endIndex);
            splitLists.add(sublist);
        }
        return splitLists;
    }

    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            numbers.add(i);
        }
        
        List<List<Integer>> splitNumbers = chunk(numbers, 4);
        for (List<Integer> sublist : splitNumbers) {
            System.out.println(sublist);
        }
        
        System.out.println("-----------------------");
        splitNumbers = chunk(numbers, 20);
        for (List<Integer> sublist : splitNumbers) {
            System.out.println(sublist);
        }
        
        splitNumbers = chunk(null, 4);
        System.out.println("-----------------------");
    }
}