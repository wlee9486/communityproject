package com.zerobase.community_.post.dto;

import com.zerobase.community_.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostDto {
    private Long postId;
    private String title;
    private String contents;
    private String userName;
    private LocalDate postedAt;
    long totalCount;
    long seq;


    public static PostDto of(Post post) {
        return PostDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .contents(post.getContents())
                .userName(post.getUserName())
                .postedAt(post.getPostedAt())
                .build();
    }

    public static List<PostDto> of(List<Post> posts) {
        if (posts == null) {
            return null;
        }
        List<PostDto> postList = new ArrayList<>();
        for (Post x : posts) {
            postList.add(PostDto.of(x));
        }
        return postList;
    }
}
