import React, {Component} from 'react';
import {Card, Button, Form} from 'react-bootstrap';
import CurrencyDollar from '../assets/svgs/CurrencyDollar';

class HoldingCard extends Component {
	handleShareNumChange(event) {
		this.setState({
			numShares: event.target.value,
		});
	}

	handleSellShares() {
		this.props.sellShares(this.props.ticker, this.state.numShares);
	}

	render(){
		const {ticker, price, lastPrice, quantity, invested, sellShares} = this.props;

		const dailyChange = (price - lastPrice) * quantity;
		const dailyChangePercent = (dailyChange) / invested;
		const sign = (dailyChange > 0) ? "+" : "-";
		const displayChange = (sign + '$' + Math.abs(dailyChange));
		const displayChangePercent = (sign + Math.abs(dailyChangePercent) + '%');

		const marketValue = price * quantity;
		return (
			<Card body>
				<Card.Title>{ticker}</Card.Title>
				<Card.Text>
					<Row>
						<Col>
							<p>Current Price:</p>
							<p>${price}</p>
						</Col>
						<Col>
							<p>Quantity Held:</p>
							<p>{quantity}</p>
						</Col>
						<Col>
							<p>Daily Change:</p>
							<p>{displayChange}</p>
							<p>{displayChangePercent}</p>
						</Col>
					</Row>
					<Row>
						<Col>
							<p>Total Value:</p>
							<p>${marketValue}</p>
						</Col>
						<Col>
							<p>Invested:</p>
							<p>${invested}</p>
						</Col>
					</Row>
				</Card.Text>
				<Form>
					<Form.Control type="text" placeholder="# of shares" name="numSharesSell"
							onChange={this.handleShareNumChange} />
					<Button className ="sell-button" variant="success" 
							onClick={this.handleSellShares}>
						<CurrencyDollar />
					</Button>
				</Form>
			</Card>
		);
	}
}

export default HoldingCard;