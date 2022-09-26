package com.smarthome.workers;

import com.smarthome.warmfloor.WarmFloor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarmFloorWorker {

    @Autowired
    private List<WarmFloor> warmFloors;

    @Scheduled(fixedDelay = 1000L)
    public void workWithWarmFloors() {
        warmFloors.forEach(WarmFloor::doWork);
    }

    public void addWarmFloorInstance(WarmFloor warmFloor) {
        warmFloors.add(warmFloor);
    }
}
