package com.github.rapid.common.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * 敏感词检测工具类
 * 
 */
public class SensitiveWordChecker {
	private String name;
	
    private TrieNode rootNode = new TrieNode(new HashMap(5000));
    private Set<String> sensitiveWords = new HashSet<String>();

    public SensitiveWordChecker() {
    }
    
    public SensitiveWordChecker(List<String> words) {
        for (String word : words) {
            addWord(word);
        }
    }
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addWord(String word) {
    	if(StringUtils.isBlank(word)) return;
    	
    	word = word.trim();
    	
    	sensitiveWords.add(word);
    	
        TrieNode currentNode = rootNode;
        for (char c : word.toCharArray()) {
            currentNode = currentNode.getOrAddChild(c);
        }
        currentNode.setEndOfWord(true);
    }

    /**
     * tair 高性能敏感词库检索
     * 
     * @param text
     * @return 返回敏感词列表
     */
    public List<String> getSensitiveWords(String text) {
    	if(StringUtils.isBlank(text)) return Collections.EMPTY_LIST;
    	
        List<String> foundWords = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            TrieNode currentNode = rootNode;
            int j = i;
            while (j < text.length()) {
                char chatItem = text.charAt(j);
				currentNode = currentNode.getChild(chatItem);
                if (currentNode == null) {
                    break;
                }
                if (currentNode.isEndOfWord()) {
                    String foundWord = text.substring(i, j + 1);
					foundWords.add(foundWord);
                    break;
                }
                j++;
            }
        }
        return distinctList(foundWords);
    }

	private static ArrayList distinctList(List<String> foundWords) {
		return new ArrayList(new LinkedHashSet(foundWords));
	}
    
    /**
     * tair 最低性能敏感词库检索
     * 
     * @param text
     * @return 返回敏感词列表
     */
    public List<String> getSensitiveWordsBySimpleSearch(String text) {
    	if(StringUtils.isBlank(text)) return Collections.EMPTY_LIST;
    	
    	List<String> foundWords = new ArrayList<>();

        for (String word : sensitiveWords) {
            if (text.contains(word)) {
                foundWords.add(word);
            }
        }
        return distinctList(foundWords);
    }
    
    public Collection<String> getSensitiveWords() {
		return Collections.unmodifiableSet(sensitiveWords);
	}
    
	public TrieNode getRootNode() {
		return rootNode;
	}




	public static class TrieNode {
        private Map<Character, TrieNode> children = new HashMap<>();
        private boolean endOfWord;

        public TrieNode() {}
        
        public TrieNode(Map<Character, TrieNode> children) {
        	this.children = children;
        }
        
        public TrieNode getOrAddChild(char c) {
            return children.computeIfAbsent(c, k -> new TrieNode());
        }

        public TrieNode getChild(char c) {
            return children.get(c);
        }

        public boolean isEndOfWord() {
            return endOfWord;
        }

        public void setEndOfWord(boolean endOfWord) {
            this.endOfWord = endOfWord;
        }

		public Map<Character, TrieNode> getChildren() {
			return children;
		}
        
    }

    public static void main(String[] args) {
        List<String> sensitiveWordsList = Arrays.asList("敏感词1", "敏感词2", "敏感词3","习近平");
        SensitiveWordChecker checker = new SensitiveWordChecker(sensitiveWordsList);

        String text = "这是一个包含敏感词1和其他内容的文本。敏感词2也在这里习近平。";
        List<String> detectedWords = checker.getSensitiveWords(text);

        if (!detectedWords.isEmpty()) {
            System.out.println("检测到政治敏感词:");
            for (String word : detectedWords) {
                System.out.println(word);
            }
        } else {
            System.out.println("未检测到政治敏感词。");
        }
    }
}
