import React, {Component} from 'react';
import {Container, Row, Col} from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import MainPage from './components/MainPage'
import APIService from './APIService'

function formatDate(date) {
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


class App extends Component {
  constructor(props) {

    super(props)
    var curr = new Date();
    curr.setDate(curr.getDate());
    this.state = {
      currentDate: formatDate(curr),
      stocks: [],
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
      numShares: 0,
      stocksHeld: []

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
  }


  // componentDidMount() {
  //   APIService.getPortfolio()
  //   .then(res => {
  //     const stocks = res.data;
  //     this.setState ({
  //       stocks
  //     })
  //   })
  // }
  

  handleNextDayChange = () => {
    // console.log('Changing next day')
    var date = new Date(this.state.currentDate);
    // console.log("Before: "+date)
    date.setDate(date.getDate()+1);
    // console.log("After: "+date)
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
  }
  
  handleSharesChange = (event) => {

    let inputName = event.target.name;
    let inputValue = event.target.value;
    let finalNumShares = this.state.numShares;
  
    console.log(`Something changed in ${inputName} : ${inputValue}`)
  
    if(finalNumShares.hasOwnProperty(inputName)){
        finalNumShares[inputName] = inputValue;
        this.setState({ numShares : finalNumShares })
    }
  }
  
  handleBuyShares = (currKey,currentNumShares) => {
    // TODO: add stock chosen to portfolio
    //       update stocksHeld
    console.log("INSIDE HANDLE BUY SHARES")
    this.state.stocksHeld.push(this.state.dummyStocks[currKey]) 
    console.log(this.state.stocksHeld)
    console.log(currentNumShares)
  }
  
  render() {
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
                <label for="inputDate">CHOOSE DATE</label>
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
            dummyStocks={this.state.dummyStocks}
            chosenDate = {this.state.currentDate}
            handleNextDay={this.handleNextDayChange}
            budget = {this.state.budget}
            profit = {this.state.profit}
            numShares = {this.state.numShares}
            handleSharesChange = {this.handleSharesChange}
            handleBuyShares = {this.handleBuyShares}
            stocksHeld = {this.state.stocksHeld}
        />
      </Container> : null}

      </div>

    );
  }
}

export default App;
