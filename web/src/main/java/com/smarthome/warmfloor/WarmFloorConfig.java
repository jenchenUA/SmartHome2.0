package com.smarthome.warmfloor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smarthome.drivers.Ads1115Input;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "warm_floor_config")
public class WarmFloorConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonProperty("bParameter")
    private int bParameter;
    private double supportResistorResistance;
    private double thermistorResistance;
    @Enumerated(EnumType.STRING)
    private Ads1115Input adsInput;
    private double voltage;
    private double threshold;
    private double enableThreshold;
    private int relayPin;
    private String name;
    private boolean enabled;
}
