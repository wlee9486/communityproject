package com.zerobase.community_.post.controller;


import com.zerobase.community_.member.entity.Member;
import com.zerobase.community_.member.repository.MemberRepository;
import com.zerobase.community_.post.model.PostInput;
import com.zerobase.community_.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.zerobase.community_.post.dto.PostDto;
import com.zerobase.community_.post.model.PostParam;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class PostController extends BaseController{

    private final PostService postService;
    private final MemberRepository memberRepository;

    @GetMapping("/post/add")
    public String add(Model model, Principal principal) {
        return "post/add";
    }

    @PostMapping("/post/add")
    public String addSubmit(Model model, PostInput parameter, Principal principal)
            throws IOException {
        String userEmail = principal.getName();
        Optional<Member> member = memberRepository.findByUserId(userEmail);
        parameter.setUserName(member.get().getUserName());
        Long postId = postService.add(parameter);

        return "redirect:/post/list";
    }

    @GetMapping("/post/list")
    public String list(Model model, PostParam parameter) {
        parameter.init();
        List<PostDto> posts = postService.list(parameter);

        long totalCount = 0;
        if (posts != null && posts.size() > 0) {
            totalCount = posts.get(0).getTotalCount();
        }
        String queryString = parameter.getQueryString();
        String pagerHtml = getPaperHtml(totalCount, parameter.getPageSize(),
                parameter.getPageIndex(), queryString);

        model.addAttribute("list", posts);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "post/list";
    }


    @GetMapping("/post/detail/{postId}")
    public String postDetail(Model model, PostParam parameter) {
        PostDto detail = postService.getById(parameter.getPostId());
        model.addAttribute("detail", detail);

        return "post/detail";
    }

}
