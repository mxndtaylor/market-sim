import React, {Component} from 'react';
import PiggyBank from '../assets/svgs/PiggyBank';

class StockCard extends Component {	
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
								name="numShares" onChange={handleSharesChange} />
					</Form>
					<Button className="buy-button" variant="warning" 
							onClick={() => handleBuyShares(myKey)}>
						<PiggyBank />
					</Button>
				</Card.Body>
			</Card>
		)
	}
}

export default StockCard;