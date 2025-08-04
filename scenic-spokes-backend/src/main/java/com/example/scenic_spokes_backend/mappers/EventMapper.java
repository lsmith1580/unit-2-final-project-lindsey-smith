package com.example.scenic_spokes_backend.mappers;

import com.example.scenic_spokes_backend.dto.EventDTO;
import com.example.scenic_spokes_backend.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(source = "route.id", target = "routeId")
    EventDTO toDto(Event event);

    @Mapping(target = "route", ignore = true) //route is set manually in controller
    Event toEntity(EventDTO dto);

    default EventDTO toDtoWithOwnership(Event event, String callerId) {
        EventDTO dto = toDto(event);
        dto.setUserEvent(callerId != null && callerId.equals(event.getClerkUserid()));
        return dto;
    }
}
