package io.github.yienruuuuu.service.business.impl;

import io.github.yienruuuuu.bean.entity.BasicPic;
import io.github.yienruuuuu.bean.enums.BasicPicType;
import io.github.yienruuuuu.repository.BasicPicRepository;
import io.github.yienruuuuu.service.business.BasicPicService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Eric.Lee
 * Date: 2025/8/21
 */
@Service("basicPicService")
public class BasicPicServiceImpl implements BasicPicService {
    private final BasicPicRepository basicPicRepository;

    public BasicPicServiceImpl(BasicPicRepository basicPicRepository) {
        this.basicPicRepository = basicPicRepository;
    }


    @Override
    public List<BasicPic> findAll() {
        return basicPicRepository.findAll();
    }

    @Override
    public BasicPic findByType(BasicPicType type) {
        return basicPicRepository.findBasicPicByType(type);
    }

    @Override
    public BasicPic save(BasicPic basicPic) {
        return basicPicRepository.save(basicPic);
    }
}
