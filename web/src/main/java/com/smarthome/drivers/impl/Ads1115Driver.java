package com.smarthome.drivers.impl;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import com.smarthome.drivers.Ads1115;
import com.smarthome.drivers.Ads1115Gain;
import com.smarthome.drivers.Ads1115Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ads1115Driver implements Ads1115 {

    private static final Logger LOG = LoggerFactory.getLogger(Ads1115Driver.class);

    public static final int ADDRESS = 0x48;
    private static final int CONVERSION_REGISTER = 0x00;
    private static final int CONFIG_REGISTER     = 0x01;
    private static final int LO_THRESH_REGISTER  = 0x02;
    private static final int HI_THRESH_REGISTER  = 0x03;

    private static final int CONFIG_REGISTER_TEMPLATE = 0b1000000110000011;


    private final int address;
    private final String deviceId;
    private final Context context;
    private final int i2cBus;
    private final I2C i2c;
    private final Ads1115Gain gain;

    public Ads1115Driver(Context pi4j) {
        this(pi4j, ADDRESS, Ads1115Gain.GAIN_4_096V, 1);
    }

    public Ads1115Driver(Context pi4j, int address) {
        this(pi4j, address, Ads1115Gain.GAIN_4_096V, 1);
    }

    public Ads1115Driver(Context pi4j, int address, Ads1115Gain gain, int i2cBus) {
        this.address = address;
        this.deviceId = "ADS1115";
        this.context = pi4j;
        this.i2cBus = i2cBus;
        this.gain = gain;
        I2CProvider i2CProvider = pi4j.provider("linuxfs-i2c");
        I2CConfig i2cConfig = I2C.newConfigBuilder(pi4j).id(deviceId).bus(i2cBus).device(address).build();
        i2c = i2CProvider.create(i2cConfig);
        LOG.info("ADS1115 Connected to i2c bus={} address={}. OK.", i2cBus, address);
    }

    @Override
    public int getAddress() {
        return address;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public int getI2CBus() {
        return i2cBus;
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public Ads1115Gain getGain() {
        return gain;
    }

    @Override
    public double getAIn0() {
        return gain.gainPerByte() * readIn(calculateConfig(Ads1115Input.A0.getInputCode()));
    }

    @Override
    public double getAIn1() {
        return  gain.gainPerByte() * readIn(calculateConfig(Ads1115Input.A1.getInputCode()));
    }

    @Override
    public double getAIn2() {
        return  gain.gainPerByte() * readIn(calculateConfig(Ads1115Input.A2.getInputCode()));
    }

    @Override
    public double getAIn3() {
        return  gain.gainPerByte() * readIn(calculateConfig(Ads1115Input.A3.getInputCode()));
    }

    @Override
    public double getVoltageOn(int input) {
        return gain.gainPerByte() * readIn(input);
    }

    private int readIn(int input) {
        i2c.writeRegisterWord(CONFIG_REGISTER, input);
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            LOG.error("Error: ", e);
        }
        int result = i2c.readRegisterWord(CONVERSION_REGISTER);
        LOG.debug("readIn: {}, raw {}", input, result);
        return result;
    }

    private int calculateConfig(int pinId) {
        return CONFIG_REGISTER_TEMPLATE | gain.gain() | pinId;
    }

    @Override
    public void close() throws Exception {
        i2c.close();
    }

}
