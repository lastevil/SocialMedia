package com.socialmedia.example.converters;

import com.socialmedia.example.dto.responses.ResponseDtoMessage;
import com.socialmedia.example.entities.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(expression = "java(message.getSender().getUsername())", target = "fromUser")
    @Mapping(expression = "java(message.getReceiver().getUsername())", target = "toUser")
    @Mapping(source = "createdAt", target="createdAt")
    ResponseDtoMessage fromEntity(Message message);
    default OffsetDateTime map(String value) {
        LocalDateTime localDateTime = LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toOffsetDateTime();
    }
}