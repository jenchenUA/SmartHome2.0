package com.smarthome.workers;

import com.smarthome.config.WebSocketConfiguration;
import com.smarthome.dtos.WarmFloorData;
import com.smarthome.mappers.WarmFloorDataMapper;
import com.smarthome.warmfloor.WarmFloor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WarmFloorWorker {

    @Autowired
    private List<WarmFloor> warmFloors;
    @Autowired
    private WarmFloorDataMapper warmFloorDataMapper;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Scheduled(fixedDelay = 1000L)
    public void workWithWarmFloors() {
        warmFloors.stream()
                .filter(WarmFloor::isEnabled)
                .forEach(this::runJob);
    }

    private void runJob(WarmFloor warmFloor) {
        warmFloor.run();
        WarmFloorData message = warmFloorDataMapper.map(warmFloor);
        simpMessagingTemplate.convertAndSend(WebSocketConfiguration.WARM_FLOOR_TOPIC, message);
    }

    public List<WarmFloorData> getAllWarmFloors() {
        return warmFloors.stream()
                .map(warmFloorDataMapper::map)
                .collect(Collectors.toList());
    }

    public void addWarmFloorInstance(WarmFloor warmFloor) {
        warmFloors.add(warmFloor);
    }

    public Optional<WarmFloor> findWarmFloor(long id) {
        return warmFloors.stream()
                .filter(warmFloor -> warmFloor.getId() == id)
                .findFirst();
    }

    public void remove(WarmFloor warmFloor) {
        warmFloors.remove(warmFloor);
    }
}
