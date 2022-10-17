import React from 'react';
import './Sidebar.scss'
import {Link} from "react-router-dom";
import {ReactComponent as WarmFloorIcon} from "./svg/warm-floor-icon.svg";
import {Drawer, List, ListItem, ListItemButton, ListItemIcon, ListItemText, Toolbar} from "@mui/material";

function Sidebar(props: {isOpen: boolean, toggleDrawer: Function}) {
    return (
        <Drawer anchor={"left"} open={props.isOpen}  onClose={event => props.toggleDrawer()}
                sx={{width: 240}}>
            <Toolbar/>
            <List sx={{width: "100%"}}>
                <ListItem key={"warm-floor"} sx={{textDecoration: "none"}} disablePadding>
                    <Link className="sidebar--item" to="/">
                        <ListItemButton className="sidebar--item">
                            <ListItemIcon>
                                <WarmFloorIcon/>
                            </ListItemIcon>
                            <ListItemText  className="sidebar--item" primary={"Тепла підлога"} />
                        </ListItemButton>
                    </Link>
                </ListItem>
            </List>
        </Drawer>
    )
}

export default Sidebar;
