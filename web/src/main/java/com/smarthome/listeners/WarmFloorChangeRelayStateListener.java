package com.smarthome.listeners;

import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;
import com.smarthome.repositories.WarmFloorConfigRepository;
import com.smarthome.warmfloor.WarmFloorConfig;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WarmFloorChangeRelayStateListener implements DigitalStateChangeListener {

    private WarmFloorConfigRepository repository;
    private WarmFloorConfig configuration;

    @Override
    public void onDigitalStateChange(DigitalStateChangeEvent event) {
        boolean enabled = event.state().isLow();
        configuration.setEnabled(enabled);
        repository.save(configuration);
    }
}
