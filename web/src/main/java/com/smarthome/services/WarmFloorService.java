package com.smarthome.services;

import com.smarthome.dtos.WarmFloorData;
import com.smarthome.warmfloor.WarmFloor;
import com.smarthome.warmfloor.WarmFloorConfig;

import java.util.List;

public interface WarmFloorService {

    WarmFloorData createWarmFloorConfiguration(WarmFloorConfig configuration);

    WarmFloor prepareWarmFloorInstance(WarmFloorConfig configuration);

    void toggle(long id);

    void remove(long id);

    List<WarmFloorData> getAllWarmFloors();

    void setNewThreshold(long id, double newThreshold);
}
