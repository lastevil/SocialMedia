package com.socialmedia.example.converters;

import com.socialmedia.example.dto.responses.ResponsePostDto;
import com.socialmedia.example.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "body", target = "body")
    @Mapping(source = "header", target = "header")
    @Mapping(expression = "java(post.getPhotoLink()==null " +
            " ? "+false+" : "+true+")",  target = "photo")
    ResponsePostDto fromEntityToResp(Post post);

    default OffsetDateTime map(String value) {
        LocalDateTime localDateTime = LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toOffsetDateTime();
    }
}
