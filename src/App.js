import React, {Component} from 'react';
import {Container, Row, Col} from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import MainPage from './components/MainPage'
import APIService from './APIService'

function formatDate(date) {
  var d = new Date(date),
      month = '' + (d.getMonth()),
      day = '' + d.getUTCDate(),
      year = d.getFullYear();

  if (month.length < 2) 
      month = '0' + month;
  if (day.length < 2) 
      day = '0' + day;

  return [year, month, day].join('-');
}

function formatDateForNext(date) {
  var d = new Date(date),
      month = '' + (d.getMonth() + 1),
      day = '' + d.getUTCDate(),
      year = d.getFullYear();

  if (month.length < 2) 
      month = '0' + month;
  if (day.length < 2) 
      day = '0' + day;

  return [year, month, day].join('-');
}

function findTicker(stocksHeld, ticker){
  if(stocksHeld.length == 0){
    return -1
  }
  for( var i = 0; i < stocksHeld.length; i++){
    let curStockTicker = stocksHeld[i][0].ticker
    if(curStockTicker === ticker){ //where ticker is held
      return i;
    }
  }
  return -1
}

var rNumShares = 0

class App extends Component {
  constructor(props) {

    super(props)
    var curr = new Date();
    curr.setDate(curr.getDate());
    this.state = {
      currentDate: formatDate(curr),
      stocks: [{
        ticker: "",
        ipo: null,
        price: 0
      }],
      dummyStocks: [ {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      },
      {
        ticker: "TSLA",
        price: 200
      }
      ],
      gameStart:false,
      budget: 1000,
      profit: 0,
      portfolio: [],
      profitLoss: 0
    }

  }

  //The Date Change From Game Start Calendar
  handleDateChange = (event) => {
    let inputName = event.target.name
    let inputValue = event.target.value
    let dateInfo = this.state.currentDate

    console.log('Input date was changed')

    dateInfo.hasOwnProperty(inputName) 
      dateInfo = inputValue
      this.setState ({
        currentDate: dateInfo
      })
      
    }

  gameStart = () =>{
      this.setState({
        gameStart:true
      })

      APIService.initializeStocks(this.state.currentDate)
      .then(res => {
      console.log(res)
      this.setState ({
        stocks: res.data
      })
      console.log(this.state.stocks)
    })
  }
  

  handleNextDayChange = () => {
    var date = new Date(this.state.currentDate);
    console.log("Before: "+date)
    date.setDate(date.getDate()+1);
     console.log("After: "+date)
    let newProfit = this.state.profit + 3;
    let newBudget = this.state.budget + 5;
    this.setState({
      currentDate: date,
      profit: newProfit,
      budget: newBudget
    }, function(){
      console.log("new state check:" + this.state.currentDate)
  }
    );

    APIService.getPricesByDate(formatDateForNext(this.state.currentDate))
      .then(res => {
      console.log(res)
      this.setState ({
        stocks: res.data
      })
      console.log(this.state.stocks)
    })
  }
  
  handleSharesSell = (event) => {
    let inputName = event.target.name;
    let inputValue = event.target.value;
  
    console.log(`Something changed in ${inputName} : ${inputValue}`)
    rNumShares = Number(inputValue)
  }

  handleSharesChange = (event) => {

    let inputName = event.target.name;
    let inputValue = event.target.value;
  
    console.log(`Something changed in ${inputName} : ${inputValue}`)
    rNumShares = Number(inputValue)
  }
  
  handleBuyShares = (currKey) => {

    let curStock = this.state.stocks[currKey]
    console.log(curStock)

    let index = findTicker(this.state.portfolio, curStock.ticker)
    if(index !== -1){
      this.state.portfolio[index][1] = this.state.portfolio[index][1] + rNumShares
    }
    else{
      this.state.portfolio.push([curStock,rNumShares]) 
    }

    this.setState({
      portfolio: this.state.portfolio
    })

    console.log(this.state.portfolio)

  }
  
  render() {
    console.log(this.state.portfolio)
    return (
      //Master Div
      <div>
        
        {!this.state.gameStart ? <Container fluid>
        <Row>
          <Col>
              <h1 className = "text-center">Market Simulator</h1>
          </Col>
        </Row>
        <hr/>
        <Row>
          <Col sm={12}>
            <Row>
              <Col md={{ span: 3, offset: 3 }}>
                <label for="inputDate"></label>
                <input id="inputDate" name="currentDate" type = "date" 
                        value = {this.state.currentDate} onChange={this.handleDateChange}></input>
              </Col>
              <Col md={{ offset: 1 }}>
              <button onClick={this.gameStart}>
          START
        </button> 
              </Col>
            </Row>
          </Col>
        </Row>
      </Container> : null }

      {this.state.gameStart ? <Container fluid>
        <MainPage
            dummyStocks={this.state.stocks}
            chosenDate = {this.state.currentDate}
            handleNextDay={this.handleNextDayChange}
            budget = {this.state.budget}
            profit = {this.state.profit}
            handleSharesChange = {this.handleSharesChange}
            handleBuyShares = {this.handleBuyShares}
            portfolio = {this.state.portfolio}
            handleSharesSell = {this.handleSharesSell}
        />
      </Container> : null}

      </div>

    );
  }
}

export default App;
