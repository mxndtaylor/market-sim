import React, { Component } from 'react';
import { Card, Button } from 'react-bootstrap';

class SavedGame extends Component {
	constructor(props) {
		super(props);
		let {game, deleteGame, loadGame} = this.props;
		this.setState({
			id: game.id,
			startDate: game.startDate,
			date: game.date,
			startCash: game.startCash,
			cash: game.cash,
		});
	}

	handleDeleteClick() {
		if (confirm("About to delete Game #" 
				+ this.state.id + ". \nContinue?")){
			this.props.deleteGame(this.state.id);
		}
	}

	handleLoadClick() {
		this.props.loadGame(this.state.id);
	}

	render() {
		return (
			<Card>
				<Card.Header>Game #{this.state.id}</Card.Header>
				<Card.Body>
					<Row className="g-2">
						<Col md="5">{this.state.startDate}</Col>
						<Col md="5">{this.state.date}</Col>
						<Col md="5">{this.state.startCash}</Col>
						<Col md="5">{this.state.cash}</Col>
					</Row>
					<Row>
						<Button variant="primary" value="load" size="sm"
							onClick={this.handleLoadClick} />
						<Button variant="danger" value="delete" size="sm"
							onClick={this.handleDeleteClick} />
					</Row>
				</Card.Body>
			</Card>
		);
	}
}

export default SavedGame;