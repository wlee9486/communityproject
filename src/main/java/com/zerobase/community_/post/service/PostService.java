package com.zerobase.community_.post.service;

import com.zerobase.community_.post.dto.PostDto;
import com.zerobase.community_.post.model.PostInput;
import com.zerobase.community_.post.model.PostParam;

import java.util.List;

public interface PostService {

    Long add(PostInput parameter);

    PostDto getById(long postId);
    List<PostDto> list(PostParam parameter);
    List<PostDto> listAll();
}
