import React, { Component } from 'react';
import { Form, Button } from 'react-bootstrap';

class NewGame extends Component {
	constructor(props) {
		super(props);
		this.setState({
			date: this.props.date,
		});
	}

	handleDateChange(event) {
		this.setState({
			date: event.target.value,
		});
	}

	handleStartClick() {
		this.props.gameStart(this.state.date);
	}

	render() {
		return (
			<>
			<h2>New Game</h2>
			<Form>
				<Form.Group controlId="inputDate">
					<Form.Label>Select date to start from</Form.Label>
					<Form.Control type="date" value={this.state.date} 
						onChange={this.handleDateChange}/>
				</Form.Group>
				<Button onClick={this.props.gameStart}>
					START
				</Button>
			</Form>
			</>
		);
	}
}

export default NewGame;