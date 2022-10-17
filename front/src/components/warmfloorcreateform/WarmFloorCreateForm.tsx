import './WarmFloorCreateForm.scss'
import React from "react";
import {Button, Dialog, DialogActions, DialogContent, DialogTitle, Grid, MenuItem, TextField} from "@mui/material";
import WarmFloorConfiguration from "./WarmFloorConfiguration";

export default class WarmFloorCreateForm extends React.Component<{saveCallback: Function,
    closeCallback: Function, open: boolean}, {configuration: WarmFloorConfiguration}> {

    private adsInputs = ['A0', 'A1', 'A2', 'A3']
    private formFields = [
        {
            id: "name",
            label: "Назва",
            onChange: (event: any) => this.state.configuration.setName(event.target.value)
        },
        {
            id: "bParameter",
            label: "'B' параметр",
            onChange: (event: any) => this.state.configuration.setBParameter(Number(event.target.value))
        },
        {
            id: "supportResistorResistance",
            label: "Опір допоміжного резистора (Ом)",
            onChange: (event: any) => this.state.configuration.setSupportResistorResistance(Number(event.target.value))
        },
        {
            id: "thermistorResistance",
            label: "Опір датчика температури (Ом)",
            onChange: (event: any) => this.state.configuration.setThermistorResistance(Number(event.target.value))
        },
        {
            id: "voltage",
            label: "Напруга (B)",
            onChange: (event: any) => this.state.configuration.setVoltage(Number(event.target.value))
        },
        {
            id: "threshold",
            label: "Початкова температура (°C)",
            onChange: (event: any) => this.state.configuration.setThreshold(Number(event.target.value))
        },
        {
            id: "enableThreshold",
            label: "Різниця температури ввімкнення (°C)",
            onChange: (event: any) => this.state.configuration.setEnableThreshold(Number(event.target.value))
        },
        {
            id: "relayPin",
            label: "Пін реле",
            onChange: (event: any) => this.state.configuration.setRelayPin(Number(event.target.value))
        },
    ]


    constructor(props: {saveCallback: Function, closeCallback: Function, open: boolean}, ) {
        super(props);
        this.state = {
            configuration: new WarmFloorConfiguration()
        }
    }

    submitForm(event: any, configuration: WarmFloorConfiguration) {
        event.preventDefault();
        this.props.saveCallback(configuration);
    }

    render() {
        const configuration = this.state.configuration;
        return (
            <Dialog open={this.props.open} onClose={event => this.props.closeCallback()}>
                <DialogTitle>
                    Створення конфігурації теплої підлоги
                </DialogTitle>
                <DialogContent>
                    <Grid container spacing={2}>
                        {this.formFields.map(field => (
                            <Grid item xs={12} md={6} key={field.id}>
                                <TextField id={field.id} key={field.id} label={field.label} variant="standard"
                                           required fullWidth onChange={field.onChange}/>
                            </Grid>
                        ))}
                        <Grid item  xs={12} md={6}>
                            <TextField  id="adsInput" label="Вхід АЦП" variant="standard" required select fullWidth
                                        onChange={event => configuration.setAdsInput(event.target.value)}>
                                {this.adsInputs.map(item => (
                                    <MenuItem key={item} value={item}>
                                        {item}
                                    </MenuItem>
                                ))}
                            </TextField>
                        </Grid>
                    </Grid>
                </DialogContent>
                <DialogActions>
                    <Button onClick={event => this.props.closeCallback()}>Відмінити</Button>
                    <Button onClick={event => {this.submitForm(event, configuration)}} variant={"contained"}>Створити</Button>
                </DialogActions>
            </Dialog>
        )
    }
}

