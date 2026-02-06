package org.example.utils;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class WeightRandom {
    private WeightRandom(){};

    public static String weightedRandom(List<String> categories,
                                        Map<String, Double> weightMap,
                                        Random random) {

        double rand = random.nextDouble(); // số từ 0.0 -> 1.0
        double cumulative = 0.0;

        for (String category : categories) {
            cumulative += weightMap.get(category);

            if (rand <= cumulative) {
                return category;
            }
        }

        // fallback (trường hợp do sai số double)
        return categories.get(categories.size() - 1);
    }

}
