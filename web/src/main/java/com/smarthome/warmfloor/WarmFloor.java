package com.smarthome.warmfloor;

import com.pi4j.io.gpio.digital.DigitalOutput;
import com.smarthome.drivers.Ads1115;
import com.smarthome.repositories.WarmFloorConfigRepository;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Builder
public class WarmFloor implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(WarmFloor.class);

    private static final float ROOM_TEMPERATURE = 298.15f;
    private static final float ZERO_TEMPERATURE = 273.15f;

    private final Ads1115 ads1115;
    private final DigitalOutput relay;
    private final WarmFloorConfig config;
    private final WarmFloorConfigRepository warmFloorConfigRepository;

    public WarmFloor(Ads1115 ads1115, DigitalOutput relay, WarmFloorConfig config,
                     WarmFloorConfigRepository warmFloorConfigRepository) {
        this.ads1115 = ads1115;
        this.relay = relay;
        this.config = config;
        this.warmFloorConfigRepository = warmFloorConfigRepository;
    }

    public long getId() {
        return config.getId();
    }

    public boolean isEnabled() {
        return config.isEnabled();
    }

    public double getCurrentTemperatureInCelsius() {
        double voltage = ads1115.getVoltageOn(config.getAdsInput().getInputCode());
        double temperature = getCurrentTemperatureInKelvin(voltage);
        if (Double.isNaN(temperature)) {
            return 0;
        }
        return temperature - ZERO_TEMPERATURE;
    }

    @Override
    public void run() {
        double currentTemperature = getCurrentTemperatureInCelsius();
        LOG.info("Current temperature {} in {}", currentTemperature, config.getName());
        relay.setState(shouldChangeState(currentTemperature));
    }

    public boolean isHeatingTurnedOn() {
        return relay.isLow();
    }

    public boolean isHeatingTurnedOff() {
        return !isHeatingTurnedOn();
    }

    public WarmFloorConfig getConfig() {
        return config;
    }

    public void stopHeating() {
        if (!isEnabled() && isHeatingTurnedOn()) {
            relay.setState(true);
        }
    }

    private boolean shouldChangeState(double currentTemperature) {
        double enableThreshold = isHeatingTurnedOff() ? config.getEnableThreshold() : 0;
        return !(currentTemperature < config.getThreshold() - enableThreshold);
    }

    private double getCurrentTemperatureInKelvin(double voltage) {
        return (config.getBParameter() * ROOM_TEMPERATURE) /
                (
                    config.getBParameter() +
                    (
                        ROOM_TEMPERATURE *
                        Math.log(getCurrentThermistorResistance(voltage) / config.getThermistorResistance())
                    )
                );
    }

    private double getCurrentThermistorResistance(double voltage) {
        return config.getSupportResistorResistance() * ((config.getVoltage() / voltage) - 1f);
    }

    public DigitalOutput shutdown() {
        return (DigitalOutput) relay.shutdown(ads1115.getContext());
    }
}
