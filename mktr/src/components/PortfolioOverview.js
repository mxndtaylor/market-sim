import React, {Component} from 'react';

class PortfolioOverview extends Component {
	render() {
		return (
			<Row>
				<Col>
					<p>Total Assets</p>
					<p>${this.props.assetValue}</p>
				</Col>
				<Col>
					<p>Available Cash</p>
					<p>${this.props.cash}</p>
				</Col>
				<Col>
					<p>Today's Change</p>
					<p>{this.props.dailyChange}</p>
				</Col>
				<Col>
					<p>Total Change</p>
					<p>{this.props.lifeTimeChange}</p>
				</Col>
			</Row>
		);
	}
}

export default PortfolioOverview;