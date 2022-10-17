package com.smarthome.mappers;

import com.smarthome.dtos.WarmFloorData;
import com.smarthome.warmfloor.WarmFloor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CommonMapperConfig.class)
public interface WarmFloorDataMapper {

    @Mapping(target = "heatingEnabled", expression = "java(warmFloor.isHeatingTurnedOn())")
    @Mapping(target = "currentTemperature", expression = "java(warmFloor.getCurrentTemperatureInCelsius())")
    @Mapping(source = "warmFloor.config.id", target = "id")
    @Mapping(source = "warmFloor.config.name", target = "name")
    @Mapping(source = "warmFloor.config.enabled", target = "enabled")
    @Mapping(source = "warmFloor.config.threshold", target = "threshold")
    WarmFloorData map(WarmFloor warmFloor);
}
