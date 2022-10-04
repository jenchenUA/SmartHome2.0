package com.smarthome.dtos;

import lombok.Data;

@Data
public class WarmFloorData {

    private long id;
    private String name;
    private double currentTemperature;
    private boolean heatingEnabled;
    private boolean enabled;
}
