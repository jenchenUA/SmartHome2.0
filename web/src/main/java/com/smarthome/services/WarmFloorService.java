package com.smarthome.services;

import com.smarthome.dtos.WarmFloorData;
import com.smarthome.warmfloor.WarmFloor;
import com.smarthome.warmfloor.WarmFloorConfig;

import java.util.List;

public interface WarmFloorService {

    void createWarmFloorConfiguration(WarmFloorConfig configuration);

    WarmFloor prepareWarmFloorInstance(WarmFloorConfig configuration);

    void toggle(long id);

    void remove(long id);

    List<WarmFloorData> getAllWarmFloors();
}
