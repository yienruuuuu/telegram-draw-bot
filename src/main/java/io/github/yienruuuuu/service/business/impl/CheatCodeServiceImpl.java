package io.github.yienruuuuu.service.business.impl;

import io.github.yienruuuuu.bean.entity.CheatCode;
import io.github.yienruuuuu.repository.CheatCodeRepository;
import io.github.yienruuuuu.service.business.CheatCodeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Eric.Lee
 * Date: 2024/11/25
 */
@Service("cheatCodeService")
public class CheatCodeServiceImpl implements CheatCodeService {
    private final CheatCodeRepository cheatCodeRepository;
    private final ConcurrentHashMap<String, CheatCode> cache = new ConcurrentHashMap<>();


    public CheatCodeServiceImpl(CheatCodeRepository cheatCodeRepository) {
        this.cheatCodeRepository = cheatCodeRepository;
    }

    @Override
    public CheatCode save(CheatCode cheatCode) {
        // Save to database
        CheatCode savedCheatCode = cheatCodeRepository.save(cheatCode);
        // Update cache
        cache.put(savedCheatCode.getCode(), savedCheatCode);
        return savedCheatCode;
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
    public Optional<CheatCode> findByCode(String code) {
        // Check cache first
        CheatCode cachedCheatCode = cache.get(code);
        if (cachedCheatCode != null) {
            return Optional.of(cachedCheatCode);
        }

        // If not in cache, query from repository
        Optional<CheatCode> cheatCodeOptional = cheatCodeRepository.findByCode(code);
        cheatCodeOptional.ifPresent(cheatCode -> cache.put(code, cheatCode));

        return cheatCodeOptional;
    }

    @Override
    public void deleteById(Integer id) {
        // Find and remove from cache if it exists
        cheatCodeRepository.findById(id).ifPresent(cheatCode -> cache.remove(cheatCode.getCode()));
        // Delete from database
        cheatCodeRepository.deleteById(id);
    }
}
