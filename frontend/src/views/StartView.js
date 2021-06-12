import React, { Component } from 'react';
import APIService from '../APIService';
import NewGame from '../components/NewGame';
import SavedGameList from '../components/SavedGameList';

class StartView extends Component {
	constructor(props) {
		super(props);
		this.fetchSaves();
	}

	fetchSaves() {
		this.setState({
			saveGames : APIService.getPortfolios(),
		});
	}

	// instead of props.function we need to make api calls
	// then redirect? or pass an href down to children
	// and then it's just a Link component instead of buttons?
	// well, load and create both go to the same redirect, so
	// that might be better here?
	handleCreateGame(date) {
		this.props.redirectToLoaderView(APIService.createPortfolio(date));
	}

	handleDeleteGame(id) {
		if (confirm("About to delete Game #" 
				+ id + ". \nContinue?")) {
			if (APIService.deleteGame(id)) {
				alert("Delete successful.");
				this.fetchSaves();
			} else {
				alert("Something went wrong, could not delete.")
			}
		}
	}

	handleLoadGame(id) {
		APIService.getPortfolio(id)
			.then(response => 
				this.props.redirectToLoaderView(respons.data.portfolio)
			);
	}

	render() {
		return (
			<Row>
				<Col>
					<NewGame gameStart={this.handleCreateGame} />
				</Col>
				<Col>
					<SavedGameList saves={this.state.saveGames}
						loadGame={this.handleLoadGame} 
						deleteGame={this.handleDeleteGame} />
				</Col>
			</Row>
		);
	}
}

export default StartView;