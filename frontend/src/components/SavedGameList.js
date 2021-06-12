import React, { Component } from 'react';
import APIService from '../APIService';
import SavedGame from './SavedGame';

class SavedGameList extends Component {
	handleLoadGame(id) {
		this.props.loadGame(id);
	}

	handleDeleteGame(id) {
		this.props.deleteGame(id);
	}

	render() {
		return (
			<>
			<h2>Save Games</h2>
			{this.state.saveGames.map((game, key) => (
				<SavedGame key={key} game={game} 
					deleteGame={this.handleDeleteGame} 
					loadGame={this.handleLoadGame} />
			))}
			</>
		);
	}
}

export default SavedGameList;