package com.smarthome.config;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfig;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.plugin.mock.platform.MockPlatform;
import com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogInputProviderImpl;
import com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogOutputProviderImpl;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInputProviderImpl;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalOutputProviderImpl;
import com.pi4j.plugin.mock.provider.i2c.MockI2CProviderImpl;
import com.pi4j.plugin.mock.provider.pwm.MockPwmProviderImpl;
import com.pi4j.plugin.mock.provider.serial.MockSerialProviderImpl;
import com.pi4j.plugin.mock.provider.spi.MockSpiProviderImpl;
import com.smarthome.drivers.Ads1115;
import com.smarthome.drivers.impl.Ads1115Driver;
import com.smarthome.listeners.WarmFloorChangeRelayStateListener;
import com.smarthome.repositories.WarmFloorConfigRepository;
import com.smarthome.services.WarmFloorService;
import com.smarthome.warmfloor.WarmFloor;
import com.smarthome.warmfloor.WarmFloorConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class Pi4JConfiguration {

    @Bean(destroyMethod = "shutdown")
    @Profile("!test")
    public Context createContext() {
        return Pi4J.newAutoContext();
    }

    @Bean(destroyMethod = "shutdown")
    @Profile("test")
    public Context createTestContext() {
        return Pi4J.newContextBuilder()
                .add(new MockPlatform())
                .add(new MockI2CProviderImpl())
                .add(new MockDigitalInputProviderImpl())
                .add(new MockDigitalOutputProviderImpl())
                .add(new MockAnalogInputProviderImpl())
                .add(new MockAnalogOutputProviderImpl())
                .add(new MockPwmProviderImpl())
                .add(new MockSpiProviderImpl())
                .add(new MockSerialProviderImpl())
                .build();
    }

    @Bean(destroyMethod = "close")
    public Ads1115 createAds(Context context) {
        return new Ads1115Driver(context);
    }

    @Bean
    public List<WarmFloor> setupWarmFloor(WarmFloorConfigRepository repository, Context context, Ads1115 ads1115) {
        return repository.findAll().stream()
                .map(config -> WarmFloor.builder()
                        .ads1115(ads1115)
                        .relay(prepareDigitalOutput(config, context, repository))
                        .config(config)
                        .warmFloorConfigRepository(repository)
                        .build()
                )
                .collect(Collectors.toList());
    }

    private DigitalOutput prepareDigitalOutput(WarmFloorConfig configuration, Context context,
                                               WarmFloorConfigRepository warmFloorConfigRepository) {
        DigitalOutputConfig relayConfig = DigitalOutputConfig.newBuilder(context)
                .id("D" + configuration.getRelayPin())
                .address(configuration.getRelayPin())
                .initial(DigitalState.HIGH)
                .build();
        DigitalOutput output = context.getDigitalOutputProvider().create(relayConfig);
        output.addListener(new WarmFloorChangeRelayStateListener(warmFloorConfigRepository, configuration));
        return output;
    }
}
