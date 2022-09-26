package com.smarthome.warmfloor;

import com.pi4j.io.gpio.digital.DigitalOutput;
import com.smarthome.drivers.Ads1115;
import com.smarthome.repositories.WarmFloorConfigRepository;
import lombok.Builder;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Builder
public class WarmFloor implements AutoCloseable {

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

    public double getCurrentTemperatureInCelsius() {
        double voltage = ads1115.getVoltageOn(config.getAdsInput().getInputCode());
        double temperature = getCurrentTemperatureInKelvin(voltage);
        if (Double.isNaN(temperature)) {
            return ZERO_TEMPERATURE;
        }
        return temperature - ZERO_TEMPERATURE;
    }

    public void doWork() {
        double currentTemperature = getCurrentTemperatureInCelsius();
        LOG.info("Current temperature {} in {}", currentTemperature, config.getName());
        if (currentTemperature < config.getThreshold() - config.getEnableThreshold()) {
            relay.setState(true);
        } else if (currentTemperature >= config.getThreshold()) {
            relay.setState(false);
        }
    }

    public boolean isWarmingTurnedOn() {
        return relay.isOn();
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

    @Override
    public void close() throws Exception {
        relay.shutdown(ads1115.getContext());
    }
}
