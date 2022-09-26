import React from 'react';
import './Main.scss'
import {Route, Routes} from "react-router-dom";
import DashboardPage from "../../pages/dashboard/DashboardPage";
import WarmFloorPage from "../../pages/warm-floor-page/WarmFloorPage";
import SettingsPage from "../../pages/settings-page/SettingsPage";

function Main() {
    return (
        <main className="main-content">
            <div className="main-content--container">
                <Routes>
                    <Route path="/" element={<DashboardPage/>}/>
                    <Route path="/warm-floor" element={<WarmFloorPage/>}/>
                    <Route path="/settings" element={<SettingsPage/>}/>
                </Routes>
            </div>
        </main>
    )
}

export default Main;
