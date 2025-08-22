package io.github.yienruuuuu.service.business.impl;

import io.github.yienruuuuu.bean.entity.Card;
import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.repository.CardRepository;
import io.github.yienruuuuu.service.business.CardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/14
 */
@Service("cardService")
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional
    @Override
    public Card save(Card card) {
        return cardRepository.save(card);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Card> findAllByPage(Pageable pageable) {
        return cardRepository.findAllByOrderByIdDesc(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Card> findAllByCardPool(CardPool cardPool, Pageable pageable) {
        return cardRepository.findAllByCardPoolOrderByIdDesc(cardPool, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Card> findById(Integer id) {
        return cardRepository.findById(id);
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        cardRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void delete(Card card) {
        cardRepository.delete(card);
    }

    @Transactional
    @Override
    public void deleteByCardPoolId(Integer cardPoolId) {
        cardRepository.deleteAllByCardPool_Id(cardPoolId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByResourceId(Integer id) {
        return cardRepository.existsByResource_Id(id);
    }
}
