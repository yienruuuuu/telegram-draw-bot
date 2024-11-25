package io.github.yienruuuuu.service.business.impl;

import io.github.yienruuuuu.bean.entity.CheatCode;
import io.github.yienruuuuu.repository.CheatCodeRepository;
import io.github.yienruuuuu.service.business.CheatCodeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2024/11/25
 */
@Service("cheatCodeService")
public class CheatCodeServiceImpl implements CheatCodeService {
    private final CheatCodeRepository cheatCodeRepository;

    public CheatCodeServiceImpl(CheatCodeRepository cheatCodeRepository) {
        this.cheatCodeRepository = cheatCodeRepository;
    }

    @Override
    public CheatCode save(CheatCode cheatCode) {
        return cheatCodeRepository.save(cheatCode);
    }

    @Override
    public Page<CheatCode> findAllByPage(Pageable pageable) {
        return cheatCodeRepository.findAll(pageable);
    }

    @Override
    public Optional<CheatCode> findById(Integer id) {
        return cheatCodeRepository.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        cheatCodeRepository.deleteById(id);
    }
}
