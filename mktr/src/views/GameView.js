import React, {Component} from 'react';
import './GameView.css';
import {Row, Col, Card, Button, Form} from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import APIService from '../APIService';
import StockCard from '../components/StockCard';
import {
	HoldingsSideBar, 
	PortfolioOverview, 
	Market,
} from '../components/*';

function formatDate(date) {
	if(typeof(date) === 'string'){
		var month = '' + date.substring(5,7),
		day = '' + date.substring(8,10),
		year = '' + date.substring(0,4);
		return [month, day, year].join('-');
	}

	var d = new Date(date),
		month = '' + (d.getMonth() + 1),
		day = '' + d.getDate(),
		year = d.getFullYear();
	
	if (month.length < 2) 
		month = '0' + month;
	if (day.length < 2) 
		day = '0' + day;
	
	return [month, day, year].join('-');
}


class GameView extends Component {
	constructor(props) {
		super(props)
	}

	componentDidMount() {
		this.refreshPortfolio();
		this.refreshStocks();
	}

	refreshPortfolio() {
		let id = this.props.location.state.gameId;
		APIService.getPortfolio(id)
			.then(response => this.setState({
				cash: response.data.cash,
				date: response.data.date,
				startCash: response.data.startCash,
				startDate: response.data.startDate,
				assetValue: response.data.assetValue,
				holdings: response.data.holdings,
			}));
	}

	refreshStocks() {
		let date = this.state.date;
		APIService.getTickersByDate(date)
			.then(response => this.setState({
				stocks: response.data,
			}));
	}

	handleNextDayChange = () => {
		// console.log(typeof(this.state.currentDate))
		// console.log(date)
		var date
		if(typeof(this.state.currentDate) === 'string'){

			date = new Date(this.state.currentDate + "T00:00:00")
		}
		else{
			date = new Date(this.state.currentDate)
			date.setDate(date.getDate()+1);
		}
	
		this.setState({
			currentDate: date
		}, function(){
	
	}
		);

		APIService.getPricesByDate(formatDateForNext(date))
			.then(res => {
	 
			this.setState ({
				stocks: res.data
			})
 
		})
	}

	handleSharesSell = (event) => {

		let inputName = event.target.name;
		let inputValue = event.target.value;
	

		rNumShares = Number(inputValue)
		console.log('HSHARESSELL')
		console.log(rNumShares)
	}

	handleSharesChange = (event) => {
		let inputName = event.target.name;
		let inputValue = event.target.value;
		rNumShares = Number(inputValue)
	}
	
	handleBuyShares = (currKey) => {
		let tempPort = this.state.portfolio
		let curStock = this.state.stocks[currKey]
		let curTicker = curStock.ticker
		let curDate = this.state.currentDate
		let index = findTicker(this.state.portfolio, curTicker)
		if(index !== -1){
			tempPort[index][1] = this.state.portfolio[index][1] + rNumShares
		}
		else{
			tempPort.push([curStock,rNumShares]) 
		}

		let tempBudget = this.state.budget - curStock.price * rNumShares
		if(tempBudget < 0) {
			tempPort.splice(-1,1)
			alert("You can't afford that many!")
			rNumShares = 0
		return
		}
		APIService.buyShares(this.state.memberId, curTicker, curDate, rNumShares)
		this.setState({
			portfolio: tempPort,
			budget: tempBudget.toFixed(2)
		})
	}

	handleSellEvent = (ticker, numShares) =>{
		let curStock = ticker
		let temp = this.state.holdings.find((holding) => 
			holding.ticker === ticker
		).quantity - numShares;
		if(temp < 0){
			alert("You don't have enough shares!");
			return
		}
		else if(temp === 0){
			tempPort.splice(index,1)
		}
		else {
			tempPort[index][1] = this.state.portfolio[index][1] - rNumShares
		}
	 
		
		APIService.getTickerPforD(formatDateForNext(this.state.currentDate), curStock[0].ticker)
			.then(data => {
				let tempBudget = Number(this.state.budget) + Number(data) * Number(rNumShares)
				let curProfit = Number(this.state.profit) + ((Number(data) * Number(rNumShares)) - (Number(curStock[0].price) * Number(rNumShares)))
			
				this.setState({
					portfolio: tempPort,
					budget: tempBudget.toFixed(2),
					profit: curProfit.toFixed(2)
				})
			});
		// APIService.getTickerPriceForDate(this.state.currentDate, curStock[0].ticker).then(response=>{this.setState ({
		//	 curPrice: response.data
		// })})
		
		// APIService.getTickerPriceForDate(this.state.currentDate, curStock[0].ticker).then(response=>{console.log(response)})
		// let tempBudget = Number(this.state.budget) + Number(curStockPrice) * Number(rNumShares)

		// this.setState({
		//	 portfolio: tempPort,
		//	 budget: tempBudget
		// })
	}

	render() {
		const id = this.props.location.state.gameId;
		const lifeTimeChange = this.state.assetValue - this.state.startCash;
		return (
			<>
			<h2>Game #{id}</h2>
			<hr/>
			<Row>
				<Col>
					<HoldingsSideBar 
						holdings={this.state.holdings}
						onSellEvent={this.handleSellEvent}
					/>
				</Col>
				<Col>
					<PortfolioOverview 
						assetValue={this.state.assetValue} 
						cash={this.state.cash} 
						dailyChange={this.state.dailyChange}
						lifeTimeChange={lifeTimeChange} 
					/>
					<Market 
						stocks={this.state.stocks} 
						date={this.state.date} 
						onDateChange={this.handleNextDayChange} 
						onBuyEvent={this.handleBuyEvent} 
					/>
				</Col>
			</Row>
			<Row>
				<Col>
					<h5 className = "text-center">Portfolio</h5>
					{this.props.portfolio.map((inPortfolio,i) => {
						return <StocksHeld	inPortfolio = {inPortfolio} myKey = {i} key = {i}
											handleSharesSell = {this.props.handleSharesSell}	 
											handleSellShares = {this.props.handleSellShares} /> 
					})}
				</Col>
				<Col>
					<h2 align ="center">Stocks</h2>
					<div className="cards" id="box" name="box">
						{this.props.dummyStocks.length !== 0 ?<Row>
								{this.props.dummyStocks.map((stock,i) => {
									return <StockCard stock = {stock} myKey = {i} key = {i}
											handleSharesChange = {this.props.handleSharesChange}	
											handleBuyShares = {this.props.handleBuyShares} /> 
								})}
						</Row> : null}
						{this.props.dummyStocks.length === 0 ?<Row>
							<h1 align ="center">MARKET CLOSED</h1>
						</Row> : null}	 
					</div>
				</Col>
			</Row>
			</>
		)
	}
}

export default GameView;