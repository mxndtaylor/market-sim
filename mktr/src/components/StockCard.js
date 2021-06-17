import React, {Component} from 'react';
import {PiggyBank} from '../assets/svgs/*';

class StockCard extends Component {	
	handleSharesChange(event) {
		const numShares = event.target.value;
		this.setState({
			numShares: numShares,
		});
	}

	handleBuyShares() {
		this.props.onBuyShares(this.props.ticker, this.state.numShares);
	}

	render() {
		return (
			<Card style={{ width: '18rem' }} id="stockDisplay" 
					name="stockDisplay">
				<Card.Header>{ticker}</Card.Header>
				<Card.Body>
					<Card.Text>
						${price}
					</Card.Text>
					<Form style={{width: '100%'}}>
						<Form.Control type="text" placeholder="# of shares" 
								name="numShares" onChange={this.handleSharesChange} />
					</Form>
					<Button className="buy-button" variant="warning" 
							onClick={this.handleBuyShares}>
						<PiggyBank width="22" height="22" />
					</Button>
				</Card.Body>
			</Card>
		)
	}
}

export default StockCard;