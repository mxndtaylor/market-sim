import React, {Component} from 'react';
import {PiggyBank} from '../assets/svgs/*';

class StockCard extends Component {	
	handleShareNumChange(event) {
		const shareNum = event.target.value;
		this.setState({
			shareNum: shareNum,
		});
	}

	handleBuyShares() {
		this.props.onBuyShares(this.props.ticker, this.state.shareNum);
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
								name="shareBuyNum" onChange={this.handleShareNumChange} />
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