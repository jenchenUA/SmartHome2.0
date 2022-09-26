import React from 'react';
import './Sidebar.scss'
import {Link} from "react-router-dom";
import {ReactComponent as MainPageIcon} from "./svg/main-page-icon.svg";
import {ReactComponent as WarmFloorIcon} from "./svg/warm-floor-icon.svg";
import {ReactComponent as SettingsIcon} from "./svg/settings-icon.svg";
import {CSSTransition} from "react-transition-group";

function Sidebar(props: {isOpen: boolean}) {
    return (
        <CSSTransition in={props.isOpen} timeout={300} classNames="sidebar--active">
            <nav className={"sidebar"}>
                <div className="sidebar--container">
                    <Link className="sidebar--item" to="/">
                        <MainPageIcon className="sidebar-item-icon"/>
                        <p className="sidebar--item--title mdc-typography--subtitle1">Main menu</p>
                    </Link>
                    <Link className="sidebar--item" to="/warm-floor">
                        <WarmFloorIcon className="sidebar-item-icon"/>
                        <p className="sidebar--item--title mdc-typography--subtitle1">Warm floor</p>
                    </Link>
                    <Link className="sidebar--item" to="/settings">
                        <SettingsIcon className="sidebar-item-icon"/>
                        <p className="sidebar--item--title mdc-typography--subtitle1">Settings</p>
                    </Link>
                </div>
            </nav>
        </CSSTransition>
    )
}

export default Sidebar;
