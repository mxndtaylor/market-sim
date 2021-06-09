import './App.css';
import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import {Container, Row, Col} from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css'
import MainPage from './components/MainPage'

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

Date.prototype.addDays = function(days) {
  var date = new Date(this.valueOf());
  date.setDate(date.getDate() + days);
  return date;
}

class App extends Component {

  constructor(props) {
    super(props)
    var curr = new Date();
    curr.setDate(curr.getDate());
    this.state = {
      currentDate: formatDate(curr),
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
      ]
    }

  }

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
  
  handleNextDayChange = () => {
    console.log('Changing next day')

    var dateToBeChanged = new Date(this.state.currentDate)
    var date = dateToBeChanged.addDays(1)
    date = formatDate(date)
    console.log(date)
    this.setState({
      currentDate: date
    })
  }
  

  render() {
    return (
      <Container fluid>
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
                GAME STATS
                {/* <div hidden>
                  <MainPage dummyStocks = {this.state.dummyStocks}/>
                </div> */}
                <Link to={{pathname: "/components/MainPage",
                            dummyStocks: this.state.dummyStocks,
                            chosenDate: this.state.currentDate,
                            handleNextDay: this.handleNextDayChange}} >Start Game</Link>
              </Col>
            </Row>
          </Col>
        </Row>
      </Container>
    );
  }
}

export default App;
