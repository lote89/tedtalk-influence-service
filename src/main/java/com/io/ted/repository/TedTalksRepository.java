package com.io.ted.repository;

import com.io.ted.model.TedTalk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TedTalksRepository extends JpaRepository<TedTalk, Long> {
  List<TedTalk> findBySpeaker(String speaker);
  List<TedTalk> findByYear(Integer year);
}
