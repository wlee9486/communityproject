package com.zerobase.community_.post.mapper;

import com.zerobase.community_.post.dto.PostDto;
import com.zerobase.community_.post.model.PostParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    long selectListCount(PostParam parameter);

    List<PostDto> selectList(PostParam parameter);
}
