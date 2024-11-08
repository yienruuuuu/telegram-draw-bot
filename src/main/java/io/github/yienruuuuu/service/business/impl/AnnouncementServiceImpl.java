package io.github.yienruuuuu.service.business.impl;

import io.github.yienruuuuu.bean.entity.Announcement;
import io.github.yienruuuuu.repository.AnnouncementRepository;
import io.github.yienruuuuu.service.business.AnnouncementService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Service("announcementService")
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @Override
    public List<Announcement> findAll() {
        return announcementRepository.findAll();
    }

    @Override
    public void save(Announcement announcement) {
        announcementRepository.save(announcement);
    }

    @Override
    public Announcement findById(int id) {
        return announcementRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(int id) {
        announcementRepository.deleteById(id);
    }
}
