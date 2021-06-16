import React, { Component } from 'react';

class HoldingsSideBar extends Component {

	handleSellEvent(ticker, numShares) {
		this.props.onSellEvent(ticker, numShares);
	}

	render() {
		return (
			<>
			<h3>Investment Portfolio</h3>
			{this.props.holdings.map((holding, key) => {
				const {ticker, price, quantity, invested} = holding;
				return <HoldingCard ticker={ticker} price={price} key={key}
							lastPrice={lastPrice} quantity={quantity} 
							invested={invested} sellShares={this.handleSellEvent} />;
			})}
			</>
		);
	}
}

export default HoldingsSideBar;