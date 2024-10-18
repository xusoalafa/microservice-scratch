package com.nxh.post.mapper;

import com.nxh.post.dto.response.PostResponse;
import com.nxh.post.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(Post post);
}
