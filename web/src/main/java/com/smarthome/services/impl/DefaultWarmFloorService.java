package com.smarthome.services.impl;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfig;
import com.pi4j.io.gpio.digital.DigitalState;
import com.smarthome.drivers.Ads1115;
import com.smarthome.dtos.WarmFloorData;
import com.smarthome.listeners.WarmFloorChangeRelayStateListener;
import com.smarthome.mappers.WarmFloorDataMapper;
import com.smarthome.repositories.WarmFloorConfigRepository;
import com.smarthome.services.WarmFloorService;
import com.smarthome.warmfloor.WarmFloor;
import com.smarthome.warmfloor.WarmFloorConfig;
import com.smarthome.workers.WarmFloorWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private WarmFloorDataMapper warmFloorDataMapper;
    @Value("${smarthome.dout.provider}")
    private String digitalOutputProvider;
    private Map<Number, DigitalOutput> unusedDigitalOutputs = new HashMap<>();

    @Override
    public WarmFloorData createWarmFloorConfiguration(WarmFloorConfig configuration) {
        WarmFloor warmFloor = prepareWarmFloorInstance(configuration);
        warmFloorConfigRepository.save(configuration);
        warmFloorWorker.addWarmFloorInstance(warmFloor);
        return warmFloorDataMapper.map(warmFloor);
    }

    @Override
    public WarmFloor prepareWarmFloorInstance(WarmFloorConfig configuration) {
        return WarmFloor.builder()
                .ads1115(ads1115)
                .relay(prepareDigitalOutput(configuration))
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
            DigitalOutput relay = warmFloor.shutdown();
            unusedDigitalOutputs.put(relay.getAddress(), relay);
            warmFloorConfigRepository.delete(config);
        });
    }

    @Override
    public List<WarmFloorData> getAllWarmFloors() {
        return warmFloorWorker.getAllWarmFloors();
    }

    @Override
    public void setNewThreshold(long id, double newThreshold) {
        warmFloorWorker.findWarmFloor(id).ifPresent(warmFloor -> {
            WarmFloorConfig config = warmFloor.getConfig();
            config.setThreshold(newThreshold);
            warmFloorConfigRepository.save(config);
        });
    }

    private DigitalOutput prepareDigitalOutput(WarmFloorConfig configuration) {
        DigitalOutput relay = unusedDigitalOutputs.computeIfAbsent(configuration.getRelayPin(),
                (key) -> createNewDigitalOutput(configuration));
        unusedDigitalOutputs.remove(configuration.getRelayPin());
        relay.addListener(new WarmFloorChangeRelayStateListener(warmFloorConfigRepository, configuration));
        return relay;
    }

    private DigitalOutput createNewDigitalOutput(WarmFloorConfig configuration) {
        DigitalOutputConfig config = DigitalOutputConfig.newBuilder(context)
                .id("WarmFloorRelayD" + configuration.getRelayPin())
                .address(configuration.getRelayPin())
                .provider(digitalOutputProvider)
                .initial(DigitalState.HIGH)
                .shutdown(DigitalState.HIGH)
                .build();
        return context.create(config);
    }
}
