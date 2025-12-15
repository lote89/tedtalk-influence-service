package com.io.ted.controller;

import com.io.ted.dto.SpeakerInfluenceDto;
import com.io.ted.dto.TedTalkDto;
import com.io.ted.model.TedTalk;
import com.io.ted.repository.TedTalksRepository;
import com.io.ted.service.TedTalksImportService;
import com.io.ted.service.TedTalksInfluenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URI;
import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class TedTalksController {

    private final TedTalksImportService csv;
    private final TedTalksRepository repo;
    private final TedTalksInfluenceService influence;

    public TedTalksController(
            TedTalksImportService csv,
            TedTalksRepository repo,
            TedTalksInfluenceService influence
    ) {
        this.csv = csv;
        this.repo = repo;
        this.influence = influence;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
        try {
            int count = csv.importCsv(file);
            return ResponseEntity.ok("Imported: " + count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error importing CSV");
        }
    }

    @GetMapping("/talks")
    public List<TedTalk> getAll() {
        return repo.findAll();
    }

    @GetMapping("/talks/{id}")
    public ResponseEntity<TedTalk> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/talks")
    public ResponseEntity<TedTalk> create(@Valid @RequestBody TedTalkDto dto) {
    TedTalk talk = new TedTalk(
            dto.title(),
            dto.speaker(),
            dto.year(),
            dto.views(),
            dto.likes(),
            dto.url()
    );
    TedTalk saved = repo.save(talk);
    return ResponseEntity.created(URI.create("/api/talks/" + saved.getId())).body(saved);
}

    @PutMapping("/talks/{id}")
    public ResponseEntity<TedTalk> update(@PathVariable Long id,@Valid @RequestBody TedTalkDto dto){
        return repo.findById(id).map(t -> {
            if (dto.title() != null) t.setTitle(dto.title());
            if (dto.speaker() != null) t.setSpeaker(dto.speaker());
            if (dto.year() != null) t.setYear(dto.year());
            if (dto.views() != null) t.setViews(dto.views());
            if (dto.likes() != null) t.setLikes(dto.likes());
            if (dto.url() != null) t.setUrl(dto.url());
            return ResponseEntity.ok(repo.save(t));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/talks/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
  
    @GetMapping("/influence")
    public List<SpeakerInfluenceDto> ranking() {
        return influence.ranking();
    }

    @GetMapping("/influence/year/{year}")
    public ResponseEntity<TedTalk> mostInfluentialForYear(@PathVariable int year) {
        return influence.mostInfluentialTalkForYear(year)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/influence/by-year")
    public Map<Integer, TedTalk> mostInfluentialPerYear() {
        return influence.mostInfluentialTalkPerYear();
    }
}
