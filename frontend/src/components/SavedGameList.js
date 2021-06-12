import React, { Component } from 'react';
import SavedGame from './SavedGame';

class SavedGameList extends Component {
	constructor(props) {
		super(props);
		this.setState({
			saveGames : props.saveGames,
		});
	}

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