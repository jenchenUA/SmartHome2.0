CREATE TABLE warm_floor_config (
    id bigint not null AUTO_INCREMENT,
    ads_input varchar(255) not null,
    b_parameter int not null,
    enable_threshold double not null,
    enabled bit not null,
    name varchar(255) not null,
    relay_pin int not null,
    support_resistor_resistance double not null,
    thermistor_resistance double not null,
    threshold double not null,
    voltage double not null,
    PRIMARY KEY (id)
);
