import React, {MouseEventHandler} from 'react';
import './Header.scss'
import {AppBar, IconButton, Toolbar, Typography} from "@mui/material";
import MenuIcon from '@mui/icons-material/Menu';

function Header(props : {toggleFunction: MouseEventHandler}) {
    return (
        <AppBar position={"relative"} sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
            <Toolbar>
                <IconButton size="large" edge="start" color="inherit" aria-label="menu" sx={{ mr: 2 }}
                            onClick={props.toggleFunction}>
                    <MenuIcon />
                </IconButton>
                <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                    Smart Home Console
                </Typography>
            </Toolbar>
        </AppBar>
    );
}

export default Header;
