package com.example.scenic_spokes_backend.mappers;

import com.example.scenic_spokes_backend.dto.EventDTO;
import com.example.scenic_spokes_backend.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "userEvent", ignore = true)
    EventDTO toDto(Event event);

    default EventDTO toDtoWithOwnership(Event event, String currentClerkId) {
        EventDTO dto = toDto(event);
        dto.setUserEvent(currentClerkId != null && currentClerkId.equals(event.getAppUser().getClerkId()));
        return dto;
    }
}
