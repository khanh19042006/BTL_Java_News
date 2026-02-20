package org.example.service.Impl;

import org.example.dao.HistoryDAO;
import org.example.dao.NewsDAO;
import org.example.dto.NewsDTO;
import org.example.service.RecommendService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class RecommendServiceImpl implements RecommendService {

    private final HistoryDAO historyDAO = new HistoryDAO();
    private final NewsDAO newsDAO = new NewsDAO();

    private final int limitHistory = 20;
    private final int limitLatest = 30;
    private final int finalLimit = 10;

    @Override
    public List<NewsDTO> getRecommendNews(String userId){

        List<NewsDTO> listHistory =
                historyDAO.getHistoryNews(userId, limitHistory);

        if (listHistory == null || listHistory.isEmpty()) {
            return newsDAO.getNewNews(finalLimit);
        }

        Map<String, Double> categoryWeights =
                getCategoryWeight(listHistory);

        List<NewsDTO> latestNews =
                newsDAO.getNewNews(limitLatest);

        Set<String> historyIds = listHistory.stream()
                .map(NewsDTO::getId)
                .collect(Collectors.toSet());

        List<NewsDTO> filteredLatest = latestNews.stream()
                .filter(news -> !historyIds.contains(news.getId()))
                .collect(Collectors.toList());

        if (filteredLatest.isEmpty()) {
            return newsDAO.getNewNews(finalLimit);
        }

        // Sort theo trọng số
        List<NewsDTO> sorted = filteredLatest.stream()
                .sorted((n1, n2) -> Double.compare(
                        getScore(n2, categoryWeights),
                        getScore(n1, categoryWeights)
                ))
                .collect(Collectors.toList());

        int personalizedCount = (int)(finalLimit * 0.7);

        List<NewsDTO> result = new ArrayList<>();

        // Lấy phần personalized
        for (int i = 0; i < Math.min(personalizedCount, sorted.size()); i++) {
            result.add(sorted.get(i));
        }

        // Lấy phần còn lại để random
        List<NewsDTO> remaining = new ArrayList<>();
        if (sorted.size() > personalizedCount) {
            remaining = sorted.subList(personalizedCount, sorted.size());
        }

        Collections.shuffle(remaining);

        for (NewsDTO news : remaining) {
            if (result.size() >= finalLimit) break;
            result.add(news);
        }

        return result;
    }


    // Tính trọng số category từ history
    private Map<String, Double> getCategoryWeight(List<NewsDTO> history){

        Map<String, Long> countMap = history.stream()
                .collect(Collectors.groupingBy(
                        NewsDTO::getCategory,
                        Collectors.counting()
                ));

        long total = history.size();

        Map<String, Double> weightMap = new HashMap<>();

        for (Map.Entry<String, Long> entry : countMap.entrySet()) {
            weightMap.put(
                    entry.getKey(),
                    entry.getValue() / (double) total
            );
        }

        return weightMap;
    }

    // Tính điểm cho từng bài
    private double getScore(NewsDTO news,
                            Map<String, Double> categoryWeights){

        double categoryScore =
                categoryWeights.getOrDefault(news.getCategory(), 0.0);

        long daysSince = getDaysSince(news.getDate());

        double freshnessScore = 1.0 / (daysSince + 1);

        return categoryScore * 0.8 + freshnessScore * 0.2;
    }

    // Tính xem từ ngày đăng đế giờ là bao lâu
    private long getDaysSince(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            return 0;
        }

        try {
            LocalDate createdDate = LocalDate.parse(dateString);
            long days = ChronoUnit.DAYS.between(createdDate, LocalDate.now());

            return Math.max(days, 0); // tránh số âm
        } catch (Exception e) {
            return 0; // nếu lỗi format → coi như bài mới
        }
    }


}
