package com.smarthome.services;

import com.smarthome.warmfloor.WarmFloor;
import com.smarthome.warmfloor.WarmFloorConfig;

public interface WarmFloorService {

    void createWarmFloorConfiguration(WarmFloorConfig configuration);

    WarmFloor prepareWarmFloorInstance(WarmFloorConfig configuration);
}
