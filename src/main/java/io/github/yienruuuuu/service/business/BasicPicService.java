package io.github.yienruuuuu.service.business;

import io.github.yienruuuuu.bean.entity.BasicPic;
import io.github.yienruuuuu.bean.enums.BasicPicType;

import java.util.List;

/**
 * @author Eric.Lee
 * Date: 2025/8/21
 */
public interface BasicPicService {
    List<BasicPic> findAll();

    BasicPic findByType(BasicPicType type);

    BasicPic save(BasicPic basicPic);
}
