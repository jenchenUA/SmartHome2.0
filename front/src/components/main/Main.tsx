import React from 'react';
import './Main.scss'
import {Route, Routes} from "react-router-dom";
import WarmFloorPage from "../../pages/warm-floor-page/WarmFloorPage";
import {Box} from "@mui/material";

function Main() {
    return (
        <Box component={"main"} className={"main-content--container"}>
            <Routes>
                <Route path="/" element={<WarmFloorPage/>}/>
                {/*<Route path="/" element={<DashboardPage/>}/>*/}
                {/*<Route path="/settings" element={<SettingsPage/>}/>*/}
            </Routes>
        </Box>
    )
}

export default Main;
