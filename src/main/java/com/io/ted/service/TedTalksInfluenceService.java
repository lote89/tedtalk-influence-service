package com.io.ted.service;

import com.io.ted.dto.SpeakerInfluenceDto;
import com.io.ted.model.TedTalk;
import com.io.ted.repository.TedTalksRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TedTalksInfluenceService {

    private final TedTalksRepository repo;

    public TedTalksInfluenceService(TedTalksRepository repo) {
        this.repo = repo;
    }

    public List<SpeakerInfluenceDto> ranking() {
        Map<String, long[]> map = new HashMap<>();

        for (TedTalk t : repo.findAll()) {
            String s = t.getSpeaker();
            map.putIfAbsent(s, new long[]{0, 0});
            map.get(s)[0] += t.getViews() == null ? 0 : t.getViews();
            map.get(s)[1] += t.getLikes() == null ? 0 : t.getLikes();
        }

        List<SpeakerInfluenceDto> list = new ArrayList<>();

        for (var e : map.entrySet()) {
            long views = e.getValue()[0];
            long likes = e.getValue()[1];
            double score = views * 0.7 + likes * 0.3;

            list.add(new SpeakerInfluenceDto(e.getKey(), views, likes, score));
        }

        list.sort((a, b) -> Double.compare(b.influence(), a.influence()));
        return list;
    }


    // Most influential TED Talk for a specific year
    public Optional<TedTalk> mostInfluentialTalkForYear(int year) {
        return repo.findAll().stream()
                .filter(t -> t.getYear() != null && t.getYear() == year)
                .max(Comparator.comparingDouble(this::score));
    }

    // Most influential TED Talk per year (all years)
    public Map<Integer, TedTalk> mostInfluentialTalkPerYear() {
        return repo.findAll().stream()
                .filter(t -> t.getYear() != null)
                .collect(Collectors.groupingBy(
                        TedTalk::getYear,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingDouble(this::score)),
                                opt -> opt.orElse(null)   // fix Optional::orElse error
                        )
                ));
    }

 

    // Influence score for a single talk
    private double score(TedTalk t) {
        long views = t.getViews() == null ? 0 : t.getViews();
        long likes = t.getLikes() == null ? 0 : t.getLikes();
        return views * 0.7 + likes * 0.3;
    }
}
