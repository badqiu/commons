package com.github.rapid.common.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SimilarityUtils 提供各种向量之间相似度计算的方法。
 */
public class SimilarityUtils {

    /**
     * 计算两个向量的余弦相似度。
     * @param vec1 第一个向量
     * @param vec2 第二个向量
     * @return 余弦相似度值，范围在 -1 和 1 之间
     */
    public static double cosineSimilarity(List<Double> vec1, List<Double> vec2) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vec1.size(); i++) {
            dotProduct += vec1.get(i) * vec2.get(i);
            normA += Math.pow(vec1.get(i), 2);
            normB += Math.pow(vec2.get(i), 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    /**
     * 计算两个向量的欧氏距离。
     * @param vec1 第一个向量
     * @param vec2 第二个向量
     * @return 欧氏距离值
     */
    public static double euclideanDistance(List<Double> vec1, List<Double> vec2) {
        double sum = 0.0;
        for (int i = 0; i < vec1.size(); i++) {
            sum += Math.pow(vec1.get(i) - vec2.get(i), 2);
        }
        return Math.sqrt(sum);
    }

    /**
     * 计算两个向量的曼哈顿距离。
     * @param vec1 第一个向量
     * @param vec2 第二个向量
     * @return 曼哈顿距离值
     */
    public static double manhattanDistance(List<Double> vec1, List<Double> vec2) {
        double sum = 0.0;
        for (int i = 0; i < vec1.size(); i++) {
            sum += Math.abs(vec1.get(i) - vec2.get(i));
        }
        return sum;
    }

    /**
     * 计算两个向量的杰卡德相似度。
     * @param vec1 第一个向量
     * @param vec2 第二个向量
     * @return 杰卡德相似度值，范围在 0 和 1 之间
     */
    public static double jaccardSimilarity(List<Double> vec1, List<Double> vec2) {
        Set<Double> set1 = new HashSet<>(vec1);
        Set<Double> set2 = new HashSet<>(vec2);

        Set<Double> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<Double> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }

    /**
     * 计算两个向量的皮尔逊相关系数。
     * @param vec1 第一个向量
     * @param vec2 第二个向量
     * @return 皮尔逊相关系数值，范围在 -1 和 1 之间
     */
    public static double pearsonCorrelation(List<Double> vec1, List<Double> vec2) {
        double meanVec1 = vec1.stream().mapToDouble(val -> val).average().orElse(0.0);
        double meanVec2 = vec2.stream().mapToDouble(val -> val).average().orElse(0.0);

        double numerator = 0.0;
        double denominatorA = 0.0;
        double denominatorB = 0.0;

        for (int i = 0; i < vec1.size(); i++) {
            numerator += (vec1.get(i) - meanVec1) * (vec2.get(i) - meanVec2);
            denominatorA += Math.pow((vec1.get(i) - meanVec1), 2);
            denominatorB += Math.pow((vec2.get(i) - meanVec2), 2);
        }

        return numerator / Math.sqrt(denominatorA * denominatorB);
    }

    public static void main(String[] args) {
        List<Double> vec1 = Arrays.asList(1.0, 2.0, 3.0, 4.0);
        List<Double> vec2 = Arrays.asList(2.0, 3.0, 4.0, 5.0);

        System.out.println("Cosine Similarity: " + cosineSimilarity(vec1, vec2));
        System.out.println("Euclidean Distance: " + euclideanDistance(vec1, vec2));
        System.out.println("Manhattan Distance: " + manhattanDistance(vec1, vec2));
        System.out.println("Jaccard Similarity: " + jaccardSimilarity(vec1, vec2));
        System.out.println("Pearson Correlation: " + pearsonCorrelation(vec1, vec2));
    }
}
