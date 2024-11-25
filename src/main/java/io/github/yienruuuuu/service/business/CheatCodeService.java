package io.github.yienruuuuu.service.business;

import io.github.yienruuuuu.bean.entity.CheatCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/14
 */
public interface CheatCodeService {
    CheatCode save(CheatCode cheatCode);

    Page<CheatCode> findAllByPage(Pageable pageable);

    Optional<CheatCode> findById(Integer id);

    Optional<CheatCode> findByCode(String code);

    void deleteById(Integer id);
}
