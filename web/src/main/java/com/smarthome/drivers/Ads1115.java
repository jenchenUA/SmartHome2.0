package com.smarthome.drivers;

import one.microproject.rpi.hardware.gpio.sensors.I2CDevice;

public interface Ads1115 extends I2CDevice {

    int A0_IN = 0b0100000000000000;
    int A1_IN = 0b0101000000000000;
    int A2_IN = 0b0110000000000000;
    int A3_IN = 0b0111000000000000;

    Ads1115Gain getGain();

    double getAIn0();

    double getAIn1();

    double getAIn2();

    double getAIn3();

    double getVoltageOn(int input);
}
