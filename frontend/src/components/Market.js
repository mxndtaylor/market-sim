import React, {Component} from 'react';
import StockCard from './StockCard';
import {ArrowRightCircle, ArrowLeftCircle} from '../assets/svgs';

class Market extends Component {

	handleDateChange(goingForward) {
		if (goingForward) {
			this.props.onDateChange();
		} else {
			// TODO: make DaySummary component visible after 'End Day' press
			// and after 'review previous day' press
		}
	}

	handleBuyShares(ticker, quantity) {
		this.props.onBuyEvent(ticker, quantity);
	}

	render() {
		return (
			<>
			<Row>
				<Col>
					<Button className="btn-arrow-left" variant="secondary" size="lg" 
							onClick={() => this.handleDateChange(false)}>
						Review Prevous Day
						<ArrowLeftCircle />
					</Button>
				</Col>
				<Col>
					<h2>Market: {this.state.date}</h2>
				</Col>
				<Col>
					<Button className="btn-arrow-right" variant="primary" size="lg" 
							onClick={() => this.handleDateChange(true)}>
						End day
						<ArrowRightCircle />
					</Button>
				</Col>
			</Row>
			<Row className="row-col-1 row-col-md-4">
				{this.props.stocks.map((stock, key) => {
					const {ticker, price} = stock;
					return (
						<Col key={key}>
							<StockCard ticker={ticker} price={price} 
								onBuyShares={this.handleBuyShares} />
						</Col>
					);
				})}
			</Row>
			</>
		);
	}
}

export default Market;