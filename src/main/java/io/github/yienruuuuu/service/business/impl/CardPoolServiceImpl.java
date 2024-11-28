package io.github.yienruuuuu.service.business.impl;

import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.repository.CardPoolRepository;
import io.github.yienruuuuu.service.business.CardPoolService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/14
 */
@Service("cardPoolService")
public class CardPoolServiceImpl implements CardPoolService {
    private final CardPoolRepository cardPoolRepository;

    public CardPoolServiceImpl(CardPoolRepository cardPoolRepository) {
        this.cardPoolRepository = cardPoolRepository;
    }

    @Override
    public List<CardPool> findOpenCardPools() {
        return cardPoolRepository.findOpenPoolsAfter(Instant.now());
    }

    @Override
    public CardPool save(CardPool cardPool) {
        return cardPoolRepository.save(cardPool);
    }

    @Override
    public Page<CardPool> findAllByPage(Pageable pageable) {
        return cardPoolRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Optional<CardPool> findById(Integer id) {
        return cardPoolRepository.findById(id);
    }

    @Override
    public Optional<CardPool> findByIdIsOpen(Integer id) {
        return cardPoolRepository.findOpenPoolByIdAndEndTime(id, Instant.now());
    }

    @Override
    public void deleteById(Integer id) {
        cardPoolRepository.deleteById(id);
    }
}
