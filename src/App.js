import React, {Component} from 'react';
import {Container, Row, Col} from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import MainPage from './components/MainPage'
import APIService from './APIService'


const Loader = () => (
  <Container fluid style={{justifyContent:'center', alignItems:'center'}}>
  <div class="divLoader" style={{justifyContent:'center', alignItems:'center'}}>
    <svg class="svgLoader" style={{justifyContent:'center', alignItems:'center'}} viewBox="0 0 100 100" width="100%" height="50em">
      <path stroke="none" d="M10 50A40 40 0 0 0 90 50A40 42 0 0 1 10 50" fill="#51CACC" transform="rotate(179.719 50 51)"><animateTransform attributeName="transform" type="rotate" calcMode="linear" values="0 50 51;360 50 51" keyTimes="0;1" dur="1s" begin="0s" repeatCount="indefinite"></animateTransform></path>
    </svg>
  </div>
  </Container>
);

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

function findTicker (portfolio, ticker){
  if (portfolio.length === 0){
    return -1
  }
  for( var i = 0; i < portfolio.length; i++){
    let curStockTicker = portfolio[i][0].ticker
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
      memberId: 0,
      currentDate: formatDate(curr),
      stocks: [{
        ticker: "",
        ipo: null,
        price: 0  //represents the average price of the stock 
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
      profitLoss: 0,
      curPrice:0,
      loading: false
    }

  }

  //The Date Change From Game Start Calendar
  handleDateChange = (event) => {
    let inputName = event.target.name
    let inputValue = event.target.value
    let dateInfo = this.state.currentDate

  

    dateInfo.hasOwnProperty(inputName) 
      dateInfo = inputValue
      this.setState ({
        currentDate: dateInfo
      })
      
    }

  gameStart = () =>{
      this.setState({
        gameStart:true,
        loading: true
      })
      APIService.createPortfolio(this.state.currentDate).then(response => this.setState({ memberId: response.data.id, budget: response.data.cash }))
     
      // APIService.createPortfolio(this.state.currentDate).then(response=>{console.log(response)})
      APIService.initializeStocks(this.state.currentDate)
      .then(res => {
      this.setState ({
        stocks: res.data,
        loading:false
      })

      })
  }
  

  handleNextDayChange = () => {
    var date = new Date(this.state.currentDate);

    date.setDate(date.getDate()+1);

    this.setState({
      currentDate: date
    }, function(){
  
  }
    );

    APIService.getPricesByDate(formatDateForNext(this.state.currentDate))
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

    APIService.buyShares(this.state.memberId, curTicker, curDate, rNumShares)
    // console.log(curStock)

    let index = findTicker(this.state.portfolio, curTicker)
    if(index !== -1){
      tempPort[index][1] = this.state.portfolio[index][1] + rNumShares
    }
    else{
      tempPort.push([curStock,rNumShares]) 
    }

    let tempBudget = this.state.budget - curStock.price * rNumShares

    if(tempBudget < 0) {
      tempBudget = 0
    }

    this.setState({
      portfolio: tempPort,
      budget: tempBudget.toFixed(2)
    })

 

  }

  handleSellShares = (currKey) =>{

    let tempPort = this.state.portfolio
    let curStock = this.state.portfolio[currKey]
    let index = currKey
    let temp = this.state.portfolio[index][1] - rNumShares
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
    //   curPrice: response.data
    // })})
    
    // APIService.getTickerPriceForDate(this.state.currentDate, curStock[0].ticker).then(response=>{console.log(response)})
    // let tempBudget = Number(this.state.budget) + Number(curStockPrice) * Number(rNumShares)

    // this.setState({
    //   portfolio: tempPort,
    //   budget: tempBudget
    // })

  }
  
  render() {
    return (
      //Master Div
      <div>
        {this.state.loading ? <Loader /> : null}
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

      {this.state.gameStart && !this.state.loading ? <Container fluid>
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
            handleSellShares = {this.handleSellShares}
        />
      </Container> : null}
      </div>

    );
  }
}

export default App;
