package com.smarthome.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smarthome.drivers.Ads1115Input;
import lombok.Data;

@Data
public class WarmFloorConfigData {

    @JsonProperty("bParameter")
    private int bParameter;
    private double supportResistorResistance;
    private double thermistorResistance;
    private Ads1115Input adsInput;
    private double voltage;
    private double threshold;
    private double enableThreshold;
    private int relayPin;
    private String name;
}
