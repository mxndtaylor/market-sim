import React, { Component } from 'react';
import APIService from '../APIService';
import NewGame from '../components/NewGame';
import SavedGameList from '../components/SavedGameList';

class StartView extends Component {
	constructor(props) {
		super(props);
	}

	componentDidMount() {
		this.fetchSaves();
	}

	fetchSaves() {
		APIService.getPortfolios()
			.then(response => 
				this.setState({
					saveGames: response.data,
				})
			);
	}

	handleCreateGame(date) {
		APIService.createPortfolio(date)
			.then(response => 
				this.props.redirectToLoaderView(response.data)
			);
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
				this.props.redirectToLoaderView(response.data)
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