import React, {Component} from 'react';
import './MainPage.css';
import {Container, Row, Col, Card, Button, Form} from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css'
import APIService from '../APIService'



const StocksHeld = () => {
    return (
        <Card id = "stockDisplay" name = "stockDisplay">
            <Card.Header>Ticker</Card.Header>
            <Card.Body>
                <Row>
                    <Col>
                        <Card.Text>
                            Price: $10 Number of shares: 1 
                        </Card.Text>
                    </Col>
                    <Col>
                        <Button variant="light">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-currency-dollar" viewBox="0 0 16 16">
                            <path d="M4 10.781c.148 1.667 1.513 2.85 3.591 3.003V15h1.043v-1.216c2.27-.179 3.678-1.438 3.678-3.3 0-1.59-.947-2.51-2.956-3.028l-.722-.187V3.467c1.122.11 1.879.714 2.07 1.616h1.47c-.166-1.6-1.54-2.748-3.54-2.875V1H7.591v1.233c-1.939.23-3.27 1.472-3.27 3.156 0 1.454.966 2.483 2.661 2.917l.61.162v4.031c-1.149-.17-1.94-.8-2.131-1.718H4zm3.391-3.836c-1.043-.263-1.6-.825-1.6-1.616 0-.944.704-1.641 1.8-1.828v3.495l-.2-.05zm1.591 1.872c1.287.323 1.852.859 1.852 1.769 0 1.097-.826 1.828-2.2 1.939V8.73l.348.086z"/>
                            </svg>
                        </Button>
                    </Col>
                </Row>
            </Card.Body>
        </Card>
    )
}

function formatDate(date) {
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

  const StockCard = ({dummyStock, numShares, handleSharesChange, myKey, handleBuyShares}) => {
    return (
        <Card style={{ width: '18rem' }} id = "stockDisplay" name = "stockDisplay">
            <Card.Header>{dummyStock.ticker}</Card.Header>
            <Card.Body>
                <Row>
                    <Col>
                        <Card.Text>
                            ${dummyStock.price}
                        </Card.Text>
                    </Col>
                    <Col>
                        <Row>
                            <Form>
                            <Form.Control type="text" placeholder="# of shares" name="numShares"
                                value = {numShares} onChange={handleSharesChange} />
                            </Form>
                            <Button variant="light" onClick={() => handleBuyShares(myKey,numShares)}><svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor" class="bi bi-piggy-bank" viewBox="0 0 16 16">
                                <path d="M5 6.25a.75.75 0 1 1-1.5 0 .75.75 0 0 1 1.5 0zm1.138-1.496A6.613 6.613 0 0 1 7.964 4.5c.666 0 1.303.097 1.893.273a.5.5 0 0 0 .286-.958A7.602 7.602 0 0 0 7.964 3.5c-.734 0-1.441.103-2.102.292a.5.5 0 1 0 .276.962z"/>
                                <path fill-rule="evenodd" d="M7.964 1.527c-2.977 0-5.571 1.704-6.32 4.125h-.55A1 1 0 0 0 .11 6.824l.254 1.46a1.5 1.5 0 0 0 1.478 1.243h.263c.3.513.688.978 1.145 1.382l-.729 2.477a.5.5 0 0 0 .48.641h2a.5.5 0 0 0 .471-.332l.482-1.351c.635.173 1.31.267 2.011.267.707 0 1.388-.095 2.028-.272l.543 1.372a.5.5 0 0 0 .465.316h2a.5.5 0 0 0 .478-.645l-.761-2.506C13.81 9.895 14.5 8.559 14.5 7.069c0-.145-.007-.29-.02-.431.261-.11.508-.266.705-.444.315.306.815.306.815-.417 0 .223-.5.223-.461-.026a.95.95 0 0 0 .09-.255.7.7 0 0 0-.202-.645.58.58 0 0 0-.707-.098.735.735 0 0 0-.375.562c-.024.243.082.48.32.654a2.112 2.112 0 0 1-.259.153c-.534-2.664-3.284-4.595-6.442-4.595zM2.516 6.26c.455-2.066 2.667-3.733 5.448-3.733 3.146 0 5.536 2.114 5.536 4.542 0 1.254-.624 2.41-1.67 3.248a.5.5 0 0 0-.165.535l.66 2.175h-.985l-.59-1.487a.5.5 0 0 0-.629-.288c-.661.23-1.39.359-2.157.359a6.558 6.558 0 0 1-2.157-.359.5.5 0 0 0-.635.304l-.525 1.471h-.979l.633-2.15a.5.5 0 0 0-.17-.534 4.649 4.649 0 0 1-1.284-1.541.5.5 0 0 0-.446-.275h-.56a.5.5 0 0 1-.492-.414l-.254-1.46h.933a.5.5 0 0 0 .488-.393zm12.621-.857a.565.565 0 0 1-.098.21.704.704 0 0 1-.044-.025c-.146-.09-.157-.175-.152-.223a.236.236 0 0 1 .117-.173c.049-.027.08-.021.113.012a.202.202 0 0 1 .064.199z"/>
                                </svg>
                            </Button>
                        </Row>
                    </Col>
                </Row>
            </Card.Body>
        </Card>
    )
}

class MainPage extends Component {
    constructor(props) {
        super(props)
    }

    render() {
        console.log("Main page loaded")
        return (
            <Container fluid>
                <Row>
    
                    <h1 className = "text-center">{formatDate(this.props.chosenDate)}</h1>
                </Row>
                <hr/>
                <Row>
                    <Col sm = {2}>
                        <div className="cards" id = "collection" name = "collection">
                        <h5 className = "text-center">Portfolio</h5>
                            {/* {this.props.dummyStocks.map((dummyStock,i) => {
                                        return <StockCard dummyStock = {dummyStock} myKey = {i} key = {i}
                                                numshares = {this.props.numShares} handleSharesChange = {this.props.handleSharesChange}  
                                                handleBuyShares = {this.props.handleBuyShares}/> 
                                        })} */}
                            <StocksHeld />
                            <StocksHeld />
                            <StocksHeld />
                            <StocksHeld />
                            <StocksHeld />
                            <StocksHeld />
                            <StocksHeld />
                            <StocksHeld />
                        </div>
                    </Col>
                    <Col sm = {8}>
                        <h2 align ="center">Stocks</h2>
                        <hr />
                        <Row>
                            <Col>
                                <h3 align ="center">Budget:{'\u00A0'}{this.props.budget}</h3>
                            </Col>
                            <Col>
                                <h3 align ="center">Profit:{'\u00A0'}{this.props.profit} </h3>
                            </Col>
                        </Row>

                        <div className="cards" id = "box" name = "box">

                            <Row>
                                    {this.props.dummyStocks.map((dummyStock,i) => {
                                        return <StockCard dummyStock = {dummyStock} myKey = {i} key = {i}
                                                numshares = {this.props.numShares} handleSharesChange = {this.props.handleSharesChange}  
                                                handleBuyShares = {this.props.handleBuyShares} /> 
                                        })}
                            </Row>   

                        </div>
                    </Col>
                    <Col sm = {2}>
                        <div className = "btn-arrow" id="arrowButton" name="arrowButton">
                            <Button variant = "secondary" size="lg" onClick={this.props.handleNextDay}  >
                                Next Day
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-arrow-right-circle" viewBox="0 0 16 16">
                                    <path fill-rule="evenodd" d="M1 8a7 7 0 1 0 14 0A7 7 0 0 0 1 8zm15 0A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM4.5 7.5a.5.5 0 0 0 0 1h5.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3a.5.5 0 0 0 0-.708l-3-3a.5.5 0 1 0-.708.708L10.293 7.5H4.5z"/>
                                </svg>
                                
                            </Button>
                        </div>
                    </Col>
                </Row>
            </Container>
        )
    }
}

export default MainPage