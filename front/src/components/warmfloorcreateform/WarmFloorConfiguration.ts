export default class WarmFloorConfiguration {

    private bParameter: number | undefined;
    private adsInput: string | undefined;
    private supportResistorResistance: number | undefined;
    private thermistorResistance: number | undefined;
    private voltage: number | undefined;
    private threshold: number | undefined;
    private enableThreshold: number | undefined;
    private relayPin: number | undefined;
    private name: string | undefined;


    setBParameter(value: number) {
        this.bParameter = value;
    }

    setAdsInput(value: string) {
        this.adsInput = value;
    }

    setSupportResistorResistance(value: number) {
        this.supportResistorResistance = value;
    }

    setThermistorResistance(value: number) {
        this.thermistorResistance = value;
    }

    setVoltage(value: number) {
        this.voltage = value;
    }

    setThreshold(value: number) {
        this.threshold = value;
    }

    setEnableThreshold(value: number) {
        this.enableThreshold = value;
    }

    setRelayPin(value: number) {
        this.relayPin = value;
    }

    setName(value: string) {
        this.name = value;
    }
}