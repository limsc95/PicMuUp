package com.pmu.picmuup.repository;

import com.pmu.picmuup.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
}
