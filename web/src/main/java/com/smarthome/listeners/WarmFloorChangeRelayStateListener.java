package com.smarthome.listeners;

import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;
import com.smarthome.repositories.WarmFloorConfigRepository;
import com.smarthome.warmfloor.WarmFloor;
import com.smarthome.warmfloor.WarmFloorConfig;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class WarmFloorChangeRelayStateListener implements DigitalStateChangeListener {

    private static final Logger LOG = LoggerFactory.getLogger(WarmFloor.class);

    private WarmFloorConfigRepository repository;
    private WarmFloorConfig configuration;

    @Override
    public void onDigitalStateChange(DigitalStateChangeEvent event) {
        boolean enabled = event.state().isLow();
        LOG.info("Relay on the GPIO pin {} is enabled: {}", event.source().id(), enabled);
    }
}
