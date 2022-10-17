package com.smarthome.mappers;

import com.smarthome.dtos.WarmFloorConfigData;
import com.smarthome.warmfloor.WarmFloorConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CommonMapperConfig.class)
public interface WarmFloorConfigDataMapper {

    @Mapping(source = "BParameter", target = "BParameter")
    @Mapping(source = "supportResistorResistance", target = "supportResistorResistance")
    @Mapping(source = "thermistorResistance", target = "thermistorResistance")
    @Mapping(source = "adsInput", target = "adsInput")
    @Mapping(source = "voltage", target = "voltage")
    @Mapping(source = "threshold", target = "threshold")
    @Mapping(source = "enableThreshold", target = "enableThreshold")
    @Mapping(source = "relayPin", target = "relayPin")
    @Mapping(source = "name", target = "name")
    WarmFloorConfig map(WarmFloorConfigData source);
}
