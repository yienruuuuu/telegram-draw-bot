package io.github.yienruuuuu.service.business;

import io.github.yienruuuuu.bean.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/14
 */
public interface CardService {
    Card save(Card card);

    Page<Card> findAllByPage(Pageable pageable);

    Optional<Card> findById(Integer id);

    void deleteById(Integer id);
}
