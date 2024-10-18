package com.nxh.post.controller;

import com.nxh.post.dto.ApiResponse;
import com.nxh.post.dto.PageResponse;
import com.nxh.post.dto.request.PostRequest;
import com.nxh.post.dto.response.PostResponse;
import com.nxh.post.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
  PostService postService;

  @PostMapping("/create")
  ApiResponse<PostResponse> createPost(@RequestBody PostRequest request) {
    return ApiResponse.<PostResponse>builder().result(postService.createPost(request)).build();
  }

  @GetMapping("/my-posts")
  ApiResponse<PageResponse<PostResponse>> myPosts(
      @RequestParam(name = "page", required = false, defaultValue = "1") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ApiResponse.<PageResponse<PostResponse>>builder()
        .result(postService.getMyPosts(page, size))
        .build();
  }
}
