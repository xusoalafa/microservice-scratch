package com.nxh.post.service;

import com.nxh.post.dto.PageResponse;
import com.nxh.post.dto.request.PostRequest;
import com.nxh.post.dto.response.PostResponse;
import com.nxh.post.entity.Post;
import com.nxh.post.mapper.PostMapper;
import com.nxh.post.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
  PostRepository postRepository;
  PostMapper postMapper;

  public PostResponse createPost(PostRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Post post =
        Post.builder()
            .content(request.getContent())
            .userId(authentication.getName())
            .createdDate(Instant.now())
            .modifiedDate(Instant.now())
            .build();

    post = postRepository.save(post);
    return postMapper.toPostResponse(post);
  }

  public PageResponse<PostResponse> getMyPosts(int page, int size) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();
    Sort sort = Sort.by("createdDate").ascending();
    Pageable pageable = PageRequest.of(page - 1, size, sort);
    Page<Post> pageData = postRepository.findAllByUserId(userId, pageable);
    return PageResponse.<PostResponse>builder()
        .currentPage(page)
        .pageSize(pageData.getSize())
        .totalPages(pageData.getTotalPages())
        .totalElements(pageData.getTotalElements())
        .data(pageData.getContent().stream().map(postMapper::toPostResponse).toList())
        .build();
  }
}
