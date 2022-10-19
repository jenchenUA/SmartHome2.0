import './WarmFloorCard.scss'
import React from "react";
import WarmFloorData, {WarmFloorProperties} from "./WarmFloorData";
import WarmFloorService from "../../services/WarmFloorService";
import EventBus from "../../services/EventBus";
import {ReactComponent as WarmFloorIcon} from "./svg/warm-floor-icon.svg";
import MoreVertIcon from '@mui/icons-material/MoreVert';
import DeleteIcon from '@mui/icons-material/Delete';
import {
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    CardHeader,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Fade,
    IconButton,
    ListItemIcon,
    ListItemText,
    Menu,
    MenuItem,
    Slider,
    Switch,
    Typography
} from "@mui/material";


export default class WarmFloorCard extends React.Component<WarmFloorProperties, WarmFloorCardState> {

    private warmFloorService: WarmFloorService;
    private initialThreshold: number;
    private menuAnchor: any;
    private marks = [{
        value: 10,
        label: '10°C',
    }, {
        value: 40,
        label: '40°C',
    },]

    constructor(props: WarmFloorProperties) {
        super(props)
        const cardData = this.props.cardData;
        this.state = new WarmFloorCardState(cardData, false);
        this.initialThreshold = cardData.threshold;
        this.warmFloorService = WarmFloorService.getInstance();
        this.openMenu = this.openMenu.bind(this);
        this.closeMenu = this.closeMenu.bind(this);
        this.toggle = this.toggle.bind(this);
        this.deleteAction = this.deleteAction.bind(this);
        this.closeDeleteConfirmationDialog = this.closeDeleteConfirmationDialog.bind(this);
        this.openDeleteConfirmation = this.openDeleteConfirmation.bind(this);
        this.confirmRemoving = this.confirmRemoving.bind(this);
    }

    componentDidMount() {
        EventBus.getInstance().onEvent(`warmfloor${this.props.cardData.id}`,
            (event: any) => {
                this.initialThreshold = event.detail.threshold;
                this.setState({warmFloor: event.detail});
            }
        )
    }

    componentWillUnmount() {
        EventBus.getInstance().stopListening(`warmfloor${this.props.cardData.id}`,
            (event: any) => this.setState({warmFloor: event.detail}))
    }

    private openMenu(event: any) {
        this.menuAnchor = event.currentTarget;
        let state = this.state;
        this.setState(new WarmFloorCardState(state.warmFloor, true, state.isDeleteConfirmationDialog));
    }

    private closeMenu(event: any) {
        this.setState(new WarmFloorCardState(this.state.warmFloor, false));
    }

    private toggle(id: number) {
        const state = this.state;
        const warmFloor = {...state.warmFloor};
        warmFloor.enabled = !warmFloor.enabled;
        this.setState(new WarmFloorCardState(warmFloor, state.isMoreMenuOpened, state.isDeleteConfirmationDialog));
        this.warmFloorService.toggle(id);
    }

    private deleteAction(event: any) {
        this.closeMenu(event);
    }

    private closeDeleteConfirmationDialog(event: any) {
        let state = this.state;
        this.setState(new WarmFloorCardState(state.warmFloor, state.isMoreMenuOpened, false))
    }

    private openDeleteConfirmation(event: any) {
        let state = this.state;
        this.setState(new WarmFloorCardState(state.warmFloor, false, true));
    }

    private confirmRemoving(event: any, id: number) {
        this.closeDeleteConfirmationDialog(event);
        this.props.deleteCallback(id);
    }

    render() {
        const warmFloorData = this.state.warmFloor;
        let thresholdValue = warmFloorData.threshold;
        return (
            <Card id={String(warmFloorData.id)} sx={{position: "relative"}}>
                <CardContent>
                    <CardHeader title={warmFloorData.name} action={
                        <IconButton aria-label="settings" onClick={this.openMenu}>
                            <MoreVertIcon/>
                        </IconButton>
                    }/>
                    <Menu open={this.state.isMoreMenuOpened} onClose={this.closeMenu} anchorEl={this.menuAnchor}>
                        <MenuItem onClick={this.openDeleteConfirmation}>
                            <ListItemIcon>
                                <DeleteIcon/>
                            </ListItemIcon>
                            <ListItemText>
                                Видалити
                            </ListItemText>
                        </MenuItem>
                    </Menu>

                    <Box component={"div"} className={"card"}>

                        <Typography variant="h4" component="div" className={"card--temperature"}>
                            {Math.round(warmFloorData.currentTemperature * 10) / 10}°C
                        </Typography>

                        <Slider id={`warm-floor-${warmFloorData.id}-range`} className={"card--slider"} min={10} max={40}
                                step={0.5} defaultValue={this.initialThreshold} valueLabelDisplay="auto"
                                getAriaValueText={this.formatTemperatureValue} marks={this.marks}
                                onChange={(event: any) => thresholdValue = event.target.value}
                                onChangeCommitted={event => {
                                    this.warmFloorService.updateThreshold(warmFloorData.id, thresholdValue)
                                }}/>
                    </Box>

                    <CardActions className={"card--actions"}>
                        <Switch checked={warmFloorData.enabled}
                                onChange={event => this.toggle(warmFloorData.id)}/>

                        <Fade in={warmFloorData.heatingEnabled}>
                            <WarmFloorIcon className="card--heating"/>
                        </Fade>
                    </CardActions>


                    <Dialog open={this.state.isDeleteConfirmationDialog} onClose={this.closeDeleteConfirmationDialog}>
                        <DialogTitle>
                            Підтвердження видалення
                        </DialogTitle>
                        <DialogContent>
                            Ви підверджуєте видалення?
                        </DialogContent>
                        <DialogActions>
                            <Button onClick={this.closeDeleteConfirmationDialog}>Ні</Button>
                            <Button onClick={event => this.confirmRemoving(event, warmFloorData.id)}
                                    variant={"contained"}>
                                Так
                            </Button>
                        </DialogActions>
                    </Dialog>
                </CardContent>
            </Card>
        )
    }

    private formatTemperatureValue(value: number) {
        return `${value}°C`;
    }
}

class WarmFloorCardState {
    warmFloor: WarmFloorData;
    isMoreMenuOpened: boolean;
    isDeleteConfirmationDialog: boolean;

    constructor(warmFloor: WarmFloorData,
                isMoreMenuOpened: boolean = false,
                isDeleteConfirmationDialog: boolean = false) {
        this.warmFloor = warmFloor;
        this.isMoreMenuOpened = isMoreMenuOpened;
        this.isDeleteConfirmationDialog = isDeleteConfirmationDialog;
    }
}
