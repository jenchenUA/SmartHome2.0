package com.smarthome.drivers;

public enum Ads1115Input {

    A0(0b0100000000000000),
    A1(0b0101000000000000),
    A2(0b0110000000000000),
    A3(0b0111000000000000);
    private int input;

    Ads1115Input(int input) {
        this.input = input;
    }

    public int getInputCode() {
        return this.input;
    }
}
