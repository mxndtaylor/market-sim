import './App.css';
import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import {Container, Row, Col} from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css'
import MainPage from './components/MainPage'

class App extends Component {
  constructor(props) {
    super(props)
    this.state = {
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
      }
      ]
    }

  }

  render() {
    console.log(this.state.dummyStocks)
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
                <input id="inputDate" name="inputDate" type = "datetime-local"></input>
              </Col>
              <Col md={{ offset: 1 }}>
                GAME STATS
                {/* <div hidden>
                  <MainPage dummyStocks = {this.state.dummyStocks}/>
                </div> */}
                <Link to={{pathname: "/components/MainPage",
                            dummyStocks: this.state.dummyStocks}} >Continue Game</Link>
              </Col>
            </Row>
          </Col>
        </Row>
      </Container>
    );
  }
}

export default App;
