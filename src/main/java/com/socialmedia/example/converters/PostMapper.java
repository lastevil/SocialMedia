package com.socialmedia.example.converters;

import com.socialmedia.example.dto.responses.ResponsePostDto;
import com.socialmedia.example.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "body", target = "body")
    @Mapping(source = "header", target = "header")
    @Mapping(expression = "java(post.getPhotoLink()==null " +
            " ? "+false+" : "+true+")",  target = "photo")
    ResponsePostDto fromEntityToResp(Post post);
}
