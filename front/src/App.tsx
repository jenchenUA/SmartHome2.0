import React from 'react';
import './App.scss';
import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';
import Header from "./components/header/Header";
import Sidebar from "./components/sidebar/Sidebar";
import Main from "./components/main/Main";
import {HashRouter} from "react-router-dom";
import {green, lightGreen} from "@mui/material/colors";
import {Box, createTheme, responsiveFontSizes, ThemeProvider} from "@mui/material";

class App extends React.Component<{}, {sidebarOpened: boolean}> {

    constructor(props: {}) {
        super(props);
        this.state = {
            sidebarOpened: false
        }
        this.changeSidebarState = this.changeSidebarState.bind(this);
    }


    changeSidebarState() {
        this.setState({
            sidebarOpened: !this.state.sidebarOpened
        })
    }

    theme = responsiveFontSizes(createTheme({
        palette: {
            primary: {
                main: green[600],
            },
            secondary: {
                main: lightGreen[500]
            }
        },
    }));

    render() {
      return (
          <ThemeProvider theme={this.theme}>
              <HashRouter>
                  <Box className={"app"}>
                      <Header toggleFunction={this.changeSidebarState}/>
                      <Sidebar isOpen={this.state.sidebarOpened} toggleDrawer={this.changeSidebarState}/>
                      <Main/>
                  </Box>
              </HashRouter>
          </ThemeProvider>
      );
    }
}

export default App;
