import React from 'react';
import { ReactComponent as BurgerIcon } from "./svg/burger-menu.svg";
import './Header.scss'
import {Link} from "react-router-dom";

function Header(props : {toggleFunction: any}) {
    return (
        <header className="header">
            <section className="header--row">
                <button className="header--menu-button" onClick={props.toggleFunction}>
                    <BurgerIcon/>
                </button>
                <Link to="/" className="header--title">
                    <span className="mdc-top-app-bar__title">Smart Home Console</span>
                </Link>
            </section>
        </header>
    );
}

export default Header;
