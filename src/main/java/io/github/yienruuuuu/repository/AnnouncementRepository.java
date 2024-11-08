package io.github.yienruuuuu.repository;

import io.github.yienruuuuu.bean.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
}