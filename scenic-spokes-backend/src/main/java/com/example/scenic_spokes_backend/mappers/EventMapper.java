package com.example.scenic_spokes_backend.mappers;

import com.example.scenic_spokes_backend.dto.EventDTO;
import com.example.scenic_spokes_backend.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    EventDTO eventToEventDto(Event event);

    Event eventDtoToEvent(EventDTO dto);

    default EventDTO toDtoWithOwnership(Event event, String callerId) {
        EventDTO dto = eventToEventDto(event);
        dto.setUserEvent(callerId != null && callerId.equals(event.getClerkUserid()));
        return dto;
    }
}
