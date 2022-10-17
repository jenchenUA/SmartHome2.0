package com.smarthome.drivers.impl;

import com.pi4j.context.Context;
import com.smarthome.drivers.Ads1115Gain;

import java.util.Random;

public class MockAds1115Driver extends Ads1115Driver {

    private Random random;

    public MockAds1115Driver(Context pi4j, String i2cProvider) {
        super(pi4j, i2cProvider);
        random = new Random();
    }

    public MockAds1115Driver(Context pi4j, int address, String i2cProvider) {
        super(pi4j, address, i2cProvider);
        random = new Random();
    }

    public MockAds1115Driver(Context pi4j, int address, Ads1115Gain gain, int i2cBus, String i2cProvider) {
        super(pi4j, address, gain, i2cBus, i2cProvider);
        random = new Random();
    }

    @Override
    public double getVoltageOn(int input) {
        return (double) random.nextInt(5) / 10;
    }
}
