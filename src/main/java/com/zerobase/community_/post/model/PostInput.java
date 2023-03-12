package com.zerobase.community_.post.model;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class PostInput {
    private Long postId;
    private String title;
    private String contents;
    private String userName;
}
