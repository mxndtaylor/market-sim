import React, { Component } from 'react';
import NewGame from '../components/NewGame';
import SavedGameList from '../components/SavedGameList';

class StartView extends Component {

	// instead of props.function we need to make api calls
	// then redirect? or pass an href down to children
	// and then it's just a Link component instead of buttons?
	// well, load and create both go to the same redirect, so
	// that might be better here?
	handleCreateGame(id) {
		this.props.createGame(id);
	}

	handleDeleteGame(id) {
		this.props.deleteGame(id);
	}

	handleLoadGame(id) {
		this.props.loadGame(id);
	}

	render() {
		return (
			<Row>
				<Col>
					<NewGame gameStart={this.handleCreateGame} />
				</Col>
				<Col>
					<SavedGameList loadGame={this.handleLoadGame} 
							deleteGame={this.handleDeleteGame} />
				</Col>
			</Row>
		);
	}
}

export default StartView;