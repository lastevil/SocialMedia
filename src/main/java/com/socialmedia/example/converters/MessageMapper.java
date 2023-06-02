package com.socialmedia.example.converters;

import com.socialmedia.example.dto.responses.ResponseDtoMessage;
import com.socialmedia.example.entities.Message;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    ResponseDtoMessage fromEntity(Message message);

}