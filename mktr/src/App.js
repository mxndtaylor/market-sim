import React, {Component} from 'react';
import {
	BrowserRouter as Router,
	Route,
	Redirect,
} from 'react-router-dom';
import {Container, Row, Col} from "react-bootstrap";

import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

import APIService from './APIService'

import GameView from './views/GameView'
import StartView from './views/StartView'
import LoaderView from './views/LoaderView';


class App extends Component {
	constructor(props) {
		super(props)
		this.state = {
			activeGame: null,
		}
	}

	redirectToLoaderView(portfolio) {
		this.setState({activeGame: portfolio});
		return <Redirect 
			to={{
				pathname: "/loading",
				state: {portfolio: portfolio},
			}}
		/>
	}

	redirectToGameView(gameId) {
		return <Redirect push
			to={{
				pathname: "/game",
				state: {
					gameId: gameId,
				},
			}} 
		/>
	}

	redirectToStartView() {
		return <Redirect push 
			to={{
				pathname: "/",
			}} 
		/>
	}

	render() {
		return (
			<Router>
				<Container className="p-0" fluid>
					<h1 className="text-center">MKTR</h1>
					<hr/>
					<Switch>
						<Route path="/" exact render={routeProps => (
							<StartView {...routeProps}
								onGameStart={this.redirectToLoaderView} />
						)} />
						<Route path="/loading" render={routeProps => (
							<LoaderView {...routeProps} 
								onLoaded={this.redirectToGameView} />
						)} />
						<Route path="/game" render={routeProps => (
							<GameView {...routeProps} 
								onQuit={this.redirectToStartView} />
						)} />
					</Switch>
				</Container> 
			</Router>
		);
	}
}

export default App;
