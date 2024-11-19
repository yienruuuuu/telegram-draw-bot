package io.github.yienruuuuu.service.business.impl;

import io.github.yienruuuuu.bean.entity.Card;
import io.github.yienruuuuu.repository.CardRepository;
import io.github.yienruuuuu.service.business.CardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    @Override
    public Card save(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public Page<Card> findAllByPage(Pageable pageable) {
        return cardRepository.findAllByOrderByIdDesc(pageable);
    }

    @Override
    public Optional<Card> findById(Integer id) {
        return cardRepository.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        cardRepository.deleteById(id);
    }
}
