package io.github.yienruuuuu.repository;

import io.github.yienruuuuu.bean.entity.Announcement;
import io.github.yienruuuuu.bean.enums.AnnouncementType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    Announcement findAnnouncementByType(AnnouncementType type);
}