package com.smarthome.services.impl;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfig;
import com.pi4j.io.gpio.digital.DigitalState;
import com.smarthome.drivers.Ads1115;
import com.smarthome.dtos.WarmFloorData;
import com.smarthome.listeners.WarmFloorChangeRelayStateListener;
import com.smarthome.repositories.WarmFloorConfigRepository;
import com.smarthome.services.WarmFloorService;
import com.smarthome.warmfloor.WarmFloor;
import com.smarthome.warmfloor.WarmFloorConfig;
import com.smarthome.workers.WarmFloorWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultWarmFloorService implements WarmFloorService {

    @Autowired
    public WarmFloorConfigRepository warmFloorConfigRepository;
    @Autowired
    public Ads1115 ads1115;
    @Autowired
    private Context context;
    @Autowired
    private WarmFloorWorker warmFloorWorker;

    @Override
    public void createWarmFloorConfiguration(WarmFloorConfig configuration) {
        warmFloorConfigRepository.save(configuration);
        warmFloorWorker.addWarmFloorInstance(prepareWarmFloorInstance(configuration));
    }

    @Override
    public WarmFloor prepareWarmFloorInstance(WarmFloorConfig configuration) {
        DigitalOutputConfig relayConfig = prepareDigitalOutput(configuration);
        DigitalOutput relay = context.getDigitalOutputProvider().create(relayConfig);
        relay.addListener(new WarmFloorChangeRelayStateListener(warmFloorConfigRepository, configuration));
        return WarmFloor.builder()
                .ads1115(ads1115)
                .relay(relay)
                .config(configuration)
                .warmFloorConfigRepository(warmFloorConfigRepository)
                .build();
    }

    @Override
    public void toggle(long id) {
        WarmFloor warmFloor = warmFloorWorker.findWarmFloor(id)
              .orElseThrow(() -> new IllegalArgumentException(String.format("Warm floor with id %s is not found", id)));
        WarmFloorConfig config = warmFloor.getConfig();
        config.setEnabled(!config.isEnabled());
        warmFloorConfigRepository.save(config);
        warmFloor.stopHeating();
    }

    @Override
    public void remove(long id) {
        warmFloorWorker.findWarmFloor(id).ifPresent(warmFloor -> {
            warmFloorWorker.remove(warmFloor);
            WarmFloorConfig config = warmFloor.getConfig();
            config.setEnabled(false);
            warmFloor.stopHeating();
            warmFloor.close();
            warmFloorConfigRepository.delete(config);
        });
    }

    @Override
    public List<WarmFloorData> getAllWarmFloors() {
        return warmFloorWorker.getAllWarmFloors();
    }

    private DigitalOutputConfig prepareDigitalOutput(WarmFloorConfig configuration) {
        return DigitalOutputConfig.newBuilder(context)
                .id("WarmFloorRelayD" + configuration.getRelayPin())
                .address(configuration.getRelayPin())
                .initial(DigitalState.HIGH)
                .build();
    }
}
