import './WarmFloorPage.scss'
import React from "react";
import WarmFloorCard from "../../components/warmfloorcard/WarmFloorCard";
import WarmFloorData from "../../components/warmfloorcard/WarmFloorData";
import WarmFloorService from "../../services/WarmFloorService";
import {Alert, AlertColor, Box, Fab, Grid, Snackbar} from "@mui/material";
import WarmFloorCreateForm from "../../components/warmfloorcreateform/WarmFloorCreateForm";
import WarmFloorConfiguration from "../../components/warmfloorcreateform/WarmFloorConfiguration";
import AddIcon from '@mui/icons-material/Add';

class WarmFloorPage extends React.Component<{}, WarmFloorPageState> {

    private warmFloorService: WarmFloorService;

    constructor(props: object) {
        super(props);
        this.warmFloorService = WarmFloorService.getInstance();
        this.state = new WarmFloorPageState();
        this.deleteCard = this.deleteCard.bind(this);
        this.closeModal = this.closeModal.bind(this);
        this.sendConfigurationToServer = this.sendConfigurationToServer.bind(this);
        this.openSnackbar = this.openSnackbar.bind(this);
        this.closeSnackbar = this.closeSnackbar.bind(this);
        this.openCreateDialog = this.openCreateDialog.bind(this);
        this.handleError = this.handleError.bind(this);
        this.addConfigurationToPage = this.addConfigurationToPage.bind(this);
    }

    componentDidMount() {
        this.warmFloorService.getAllWarmFloors()
            .then((result: WarmFloorData[]) => {
                this.setState(new WarmFloorPageState(false, false,
                    "success", "", result))
            }).catch(console.log)
        this.warmFloorService.listenUpdates()
    }

    componentWillUnmount() {
        this.warmFloorService.stopListeningUpdates();
    }

    deleteCard(id: number) {
        this.warmFloorService.removeWarmFloorConfig(id)
            .then(res => {
                if (res.ok) {
                    const state = this.state;
                    const items = state.items.filter(item => item.id !== id);
                    this.setState(new WarmFloorPageState(state.isCreateFormOpened, true, "success",
                        "Успішно видаленно", items))
                }
            }).catch(this.handleError)
    }

    private closeModal() {
        const state = this.state;
        this.setState(new WarmFloorPageState(false, state.isSnackBarOpened, state.snackbarSeverity,
            state.snackbarMessage, state.items));
    }

    private sendConfigurationToServer(configuration: WarmFloorConfiguration) {
        this.closeModal();
        this.warmFloorService.createWarmFloorConfig(configuration)
            .then(this.addConfigurationToPage)
            .catch(this.handleError)
    }

    private addConfigurationToPage(warmFloor: WarmFloorData) {
        const state = this.state;
        const items = state.items;
        items.push(warmFloor);
        this.setState(new WarmFloorPageState(false, true, "success",
            "Успішно створенно", items));
    }

    private handleError(error: any) {
        this.openSnackbar("error", error.message)
    }

    private openSnackbar(severity: AlertColor, message: string) {
        const state = this.state;
        this.setState(new WarmFloorPageState(state.isCreateFormOpened,
            true, severity, message, this.state.items));
    }

    private closeSnackbar() {
        const state = this.state;
        this.setState(new WarmFloorPageState(state.isCreateFormOpened,
            false, state.snackbarSeverity, state.snackbarMessage, this.state.items));
    }

    private openCreateDialog(event: any) {
        const state = this.state;
        this.setState(new WarmFloorPageState(true, state.isSnackBarOpened, state.snackbarSeverity,
            state.snackbarMessage, state.items))
    }

    render() {
        const {items, isCreateFormOpened} = this.state;

        return (
            <Box className={"warm-floor--container"}>
                <Grid container spacing={2}>
                        {items?.map(item =>
                            <Grid item key={`warm-floor-grid-${item.id}`} xs={12} sm={6} md={4} lg={2}
                                  sx={{minWidth: 300}}>
                                <WarmFloorCard cardData={item} key={`warm-floor-${item.id}`}
                                    deleteCallback={this.deleteCard}/>
                            </Grid>
                        )}
                </Grid>

                <WarmFloorCreateForm saveCallback={this.sendConfigurationToServer}
                                     closeCallback={this.closeModal} open={isCreateFormOpened}/>

                <div className={"warm-floor--fab"}>
                    <Fab color={"primary"} aria-label={"add"}
                         onClick={this.openCreateDialog}>
                        <AddIcon/>
                    </Fab>
                </div>

                <Snackbar open={this.state.isSnackBarOpened} autoHideDuration={6000} onClose={this.closeSnackbar}>
                    <Alert onClose={this.closeSnackbar} severity={this.state.snackbarSeverity} sx={{ width: '100%' }}>
                        {this.state.snackbarMessage}
                    </Alert>
                </Snackbar>
            </Box>
        )
    }
}

class WarmFloorPageState {

    isCreateFormOpened: boolean;
    isSnackBarOpened: boolean;
    snackbarSeverity: AlertColor;
    snackbarMessage: string;
    items: WarmFloorData[];

    constructor(isCreateModalOpened: boolean = false,
                isSnackBarOpened: boolean = false,
                snackbarSeverity: AlertColor = "success",
                snackbarMessage: string = "",
                items: WarmFloorData[] = []) {
        this.isCreateFormOpened = isCreateModalOpened;
        this.items = items;
        this.isSnackBarOpened = isSnackBarOpened;
        this.snackbarSeverity = snackbarSeverity;
        this.snackbarMessage = snackbarMessage;
    }
}

export default WarmFloorPage;
