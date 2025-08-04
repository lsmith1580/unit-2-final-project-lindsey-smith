package com.example.scenic_spokes_backend.mappers;

import com.example.scenic_spokes_backend.dto.MotorcycleRouteDTO;
import com.example.scenic_spokes_backend.entities.MotorcycleRoute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MotorcycleRouteMapper {

    MotorcycleRouteMapper INSTANCE = Mappers.getMapper(MotorcycleRouteMapper.class);

    @Mapping(target = "eventCount", expression = "java(route.getEvents() != null ? route.getEvents().size() : 0)")
    MotorcycleRouteDTO toDto(MotorcycleRoute route);

    MotorcycleRoute toEntity(MotorcycleRouteDTO dto);
}
