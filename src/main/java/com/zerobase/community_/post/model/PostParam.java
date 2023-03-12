package com.zerobase.community_.post.model;

import lombok.Data;

@Data
public class PostParam extends Pager {
    long postId;
    String searchType;
    String searchValue;

}
