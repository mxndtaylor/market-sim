import React, { Component } from 'react';
import NewGame from '../components/NewGame';
import SavedGameList from '../components/SavedGameList';

class StartView extends Component {
	render() {
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
	}
}