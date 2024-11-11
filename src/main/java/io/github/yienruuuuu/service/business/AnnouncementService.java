package io.github.yienruuuuu.service.business;

import io.github.yienruuuuu.bean.entity.Announcement;
import io.github.yienruuuuu.bean.enums.AnnouncementType;

import java.util.List;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
public interface AnnouncementService {
    List<Announcement> findAll();

    void save(Announcement announcement);

    Announcement findById(int id);

    void delete(int id);

    Announcement findAnnouncementByType(AnnouncementType type);
}
