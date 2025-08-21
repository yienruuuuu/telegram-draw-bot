package io.github.yienruuuuu.repository;

import io.github.yienruuuuu.bean.entity.BasicPic;
import io.github.yienruuuuu.bean.enums.BasicPicType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicPicRepository extends JpaRepository<BasicPic, Integer> {
    BasicPic findBasicPicByType(BasicPicType type);
}