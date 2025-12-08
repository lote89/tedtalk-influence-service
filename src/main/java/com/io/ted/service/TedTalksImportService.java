 package com.io.ted.service;

import com.io.ted.model.TedTalk;
import com.io.ted.repository.TedTalksRepository;
import com.opencsv.CSVReaderHeaderAware;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Map;

@Service
public class TedTalksImportService {

    private final TedTalksRepository repo;

    public TedTalksImportService(TedTalksRepository repo) {
        this.repo = repo;
    }

    public int importCsv(MultipartFile file) throws Exception {
        CSVReaderHeaderAware reader =
                new CSVReaderHeaderAware(new InputStreamReader(file.getInputStream()));

        Map<String, String> row;
        int count = 0;

        while ((row = reader.readMap()) != null) {
            TedTalk t = new TedTalk();
            t.setTitle(row.get("title"));
            t.setSpeaker(row.get("author"));
            t.setViews(parseLong(row.get("views")));
            t.setLikes(parseLong(row.get("likes")));
            t.setLink(row.get("link"));
            t.setDate(parseDate(row.get("date")));

            repo.save(t);
            count++;
        }

        return count;
    }

    private long parseLong(String s) {
        try { return Long.parseLong(s.replaceAll("[^0-9]", "")); }
        catch (Exception e) { return 0; }
    }

    private LocalDate parseDate(String s) {
        try { return LocalDate.parse(s); }
        catch (Exception e) { return null; }
    }
}
