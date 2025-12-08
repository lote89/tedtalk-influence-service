package com.io.ted.service;

import com.io.ted.dto.SpeakerInfluenceDto;
import com.io.ted.model.TedTalk;
import com.io.ted.repository.TedTalksRepository;
import org.springframework.stereotype.Service;

import java.util.*;

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
}
