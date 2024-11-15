package io.github.yienruuuuu.service.business.impl;

import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.repository.CardPoolRepository;
import io.github.yienruuuuu.service.business.CardPoolService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public CardPool save(CardPool cardPool) {
        return cardPoolRepository.save(cardPool);
    }

    @Override
    public Page<CardPool> findAllByPage(Pageable pageable) {
        return cardPoolRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}
