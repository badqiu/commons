package com.github.rapid.common.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.ComparatorUtils;

public class VectorSimilarity {

//     // 计算两个向量的点积
//     private static double dotProduct(List<Double> vec1, List<Double> vec2) {
//         double dotProduct = 0.0;
//         for (int i = 0; i < vec1.size(); i++) {
//             dotProduct += vec1.get(i) * vec2.get(i);
//         }
//         return dotProduct;
//     }

//     // 计算一个向量的模
//     private static double magnitude(List<Double> vec) {
//         double mag = 0.0;
//         for (double v : vec) {
//             mag += v * v;
//         }
//         return Math.sqrt(mag);
//     }

//     // 计算两个向量之间的余弦相似度
//     private static double cosineSimilarity(List<Double> vec1, List<Double> vec2) {
//         return dotProduct(vec1, vec2) / (magnitude(vec1) * magnitude(vec2));
//     }

//     // 计算一个向量与一组向量之间的相似度
//     public static List<Double> calculateSimilarities(List<Double> targetVec, List<List<Double>> vectorSet) {
//         List<Double> similarities = new ArrayList<>();
//         for (List<Double> vec : vectorSet) {
//             similarities.add(cosineSimilarity(targetVec, vec));
//         }
//         return similarities;
//     }
    
//     public static Double calculateMinSimilarities(List<Double> targetVec, List<List<Double>> vectorSet) {
//         List<Double> similarities = calculateSimilarities(targetVec,vectorSet);
//         Optional result = similarities.stream().collect(Collectors.minBy(ComparatorUtils.NATURAL_COMPARATOR));
// 		return (Double)result.get();
//     }

//     public static void main(String[] args) {
//         // 示例向量
//         List<Double> targetVec = Arrays.asList(1.0, 2.0, 3.0, 4.0);
        
//         List<List<Double>> vectorSet = new ArrayList<>();
//         vectorSet.add(Arrays.asList(2.0, 4.0, 6.0, 8.0));
//         vectorSet.add(Arrays.asList(1.0, 0.0, 0.0, 0.0));
//         vectorSet.add(Arrays.asList(0.0, 1.0, 0.0, 0.0));
// //        vectorSet.add(Arrays.asList(-1.0, -2.0, -3.0, -4.0));

//         // 计算相似度
//         List<Double> similarities = calculateSimilarities(targetVec, vectorSet);
        
//         // 输出结果
//         for (double similarity : similarities) {
//             System.out.println("result:"+similarity);
//         }
//         Double minSimilarities = calculateMinSimilarities(targetVec, vectorSet);
//         System.out.println("minSimilarities:"+minSimilarities);
//     }
}
