import React, {Component} from 'react';
import './GameView.css';
import {Container, Row, Col, Card, Button, Form} from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import APIService from '../APIService';
import StockCard from '../components/StockCard';
import CurrencyDollar from '../assets/svgs/CurrencyDollar';
import ArrowRightCircle from '../assets/svgs/ArrowRightCircle';


const StocksHeld = ({inPortfolio, handleSharesSell, handleSellShares, myKey}) => {
	console.log(inPortfolio)
	return (
		<Card id = "stockDisplay" name = "stockDisplay">
			<Card.Header>{inPortfolio[0].ticker}</Card.Header>
			<Card.Body>
				<Row>
					<Col>
						<Card.Text>
							Bought At: ${inPortfolio[0].price} <br/># of shares: {inPortfolio[1]}
						</Card.Text>
					</Col>
					<Col>
						<Button className ="sell-button" variant="success" onClick={() => handleSellShares(myKey)}>
                            <CurrencyDollar />
					</Button>
					<Form>
						<Form.Control type="text" placeholder="# of shares" name="numSharesSell"
							onChange={handleSharesSell} />
						</Form>
					</Col>
				</Row>
			</Card.Body>
		</Card>
	)
}

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
	
	render() {
		return (
            <>
                <h1 className = "text-center">{formatDate(this.props.chosenDate)}</h1>
                <div className="cards" id = "collection" name = "collection">
                    <h5 className = "text-center">Portfolio</h5>
                    {this.props.portfolio.map((inPortfolio,i) => {
                        return <StocksHeld	inPortfolio = {inPortfolio} myKey = {i} key = {i}
                                            handleSharesSell = {this.props.handleSharesSell}	 
                                            handleSellShares = {this.props.handleSellShares} /> 
                    })}
                </div>
                <h2 align ="center">Stocks</h2>
                <hr />
                <h3 align ="center">Budget:{'\u00A0'}${this.props.budget}</h3>
                {this.props.profit < 0 ? <Col>
                    <h3 style={{ color: 'red' }} align ="center">
                        Loss:{'\u00A0'}${this.props.profit} 
                    </h3>
                </Col> : null}
                {this.props.profit >= 0 ?<Col>
                    <h3 style={{ color: 'green' }} align ="center">
                        Profit:{'\u00A0'}${this.props.profit} 
                    </h3>
                </Col> : null}

                <div className="cards" id = "box" name = "box">
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

                <div className = "btn-arrow" id="arrowButton" name="arrowButton">
                    <Button variant = "secondary" size="lg" onClick={this.props.handleNextDay}	>
                        Next Day
                        <ArrowRightCircle />
                    </Button>
                </div>
            </>
		)
	}
}

export default GameView