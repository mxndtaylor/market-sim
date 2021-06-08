import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import { BrowserRouter, Route, Switch } from "react-router-dom";
import App from './App';
import MainPage from './components/MainPage'

ReactDOM.render(
  <BrowserRouter>
       <Switch>
        <Route exact path="/" component={App} />
        <Route path="/components/MainPage" component={MainPage} />
      </Switch>
      </BrowserRouter>,
  document.getElementById('root')
);