package com.io.ted.service;

import com.io.ted.model.TedTalk;
import com.io.ted.repo.TedTalkRepository;
import com.io.ted.dto.InfluenceDto;
import com.io.ted.dto.TedTalkDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TedTalkService {

    private final TedTalkRepository repo;
    private static final double VIEWS_WEIGHT = 0.7;
    private static final double LIKES_WEIGHT = 0.3;

    public TedTalkService(TedTalkRepository repo) {
        this.repo = repo;
    }

    // CRUD
    public List<TedTalk> findAll() { return repo.findAll(); }
    public Optional<TedTalk> findById(Long id) { return repo.findById(id); }

    public TedTalk create(TedTalk talk) { return repo.save(talk); }

    public TedTalk update(Long id, TedTalkDto dto) {
        TedTalk t = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Talk not found: " + id));
        if (dto.getTitle() != null) t.setTitle(dto.getTitle());
        if (dto.getSpeaker() != null) t.setSpeaker(dto.getSpeaker());
        if (dto.getYear() != null) t.setYear(dto.getYear());
        if (dto.getViews() != null) t.setViews(dto.getViews());
        if (dto.getLikes() != null) t.setLikes(dto.getLikes());
        if (dto.getUrl() != null) t.setUrl(dto.getUrl());
        return repo.save(t);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // Influence scoring for a single talk
    public double influenceScore(TedTalk talk) {
        long views = (talk.getViews() == null) ? 0L : talk.getViews();
        long likes = (talk.getLikes() == null) ? 0L : talk.getLikes();
        return views * VIEWS_WEIGHT + likes * LIKES_WEIGHT;
    }

    // Speaker influence: aggregate all talks per speaker (sum scores) and return sorted list
    public List<InfluenceDto> speakerInfluenceRanking() {
        Map<String, Double> scoreBySpeaker = new HashMap<>();
        Map<String, Long> totalViewsBySpeaker = new HashMap<>();
        Map<String, Long> totalLikesBySpeaker = new HashMap<>();
        Map<String, Integer> talksCount = new HashMap<>();

        for (TedTalk t : repo.findAll()) {
            String speaker = (t.getSpeaker() == null) ? "Unknown" : t.getSpeaker();
            double score = influenceScore(t);
            scoreBySpeaker.merge(speaker, score, Double::sum);
            totalViewsBySpeaker.merge(speaker, (t.getViews() == null ? 0L : t.getViews()), Long::sum);
            totalLikesBySpeaker.merge(speaker, (t.getLikes() == null ? 0L : t.getLikes()), Long::sum);
            talksCount.merge(speaker, 1, Integer::sum);
        }

        return scoreBySpeaker.entrySet().stream()
                .map(e -> new InfluenceDto(e.getKey(), e.getValue(),
                        totalViewsBySpeaker.getOrDefault(e.getKey(), 0L),
                        totalLikesBySpeaker.getOrDefault(e.getKey(), 0L),
                        talksCount.getOrDefault(e.getKey(), 0)))
                .sorted(Comparator.comparingDouble(InfluenceDto::getScore).reversed())
                .collect(Collectors.toList());
    }

    // Most influential talk for a single year
    public Optional<TedTalk> mostInfluentialTalkForYear(int year) {
        return repo.findByYear(year).stream()
                .max(Comparator.comparingDouble(this::influenceScore));
    }

    // Most influential talk for each year (map year -> talk)
    public Map<Integer, TedTalk> mostInfluentialTalkPerYear() {
        return repo.findAll().stream()
                .filter(t -> t.getYear() != null)
                .collect(Collectors.groupingBy(TedTalk::getYear,
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingDouble(this::influenceScore)), Optional::orElse)));
    }
}
