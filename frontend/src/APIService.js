import axios from 'axios';

const GET_API_PORTFOLIO = "http://localhost:8080/api/portfolios/";
const GET_API_STOCKS = "http://localhost:8080/api/tickers/"
const GET_API_CLOSINGS = "http://localhost:8080/api/closings/"

// TODO: move API endpoints to a file that the backend can also access
// have the front and back generate their endpoints from that file
// manually set the endpoints in that file (maybe a RAML file? parsing would be an issue)
// potentially in application.properties

class APIService {

	// TODO: make this unnecessary as it is not RESTful
	// ideally saved games have the relevant data in the table
	// old, unused stuff should be purged somehow
	initializeStocks(date) {
		return axios.get(GET_API_STOCKS + 'initDB/' + date);
	}

	getMarketByDate(date) {
		return axios.get(GET_API_CLOSINGS + date);
	}

	createPortfolio(date, principle) {
		if (principle == null || principle == 0) {
			principle = 10000;
		} else {
			principle = Math.abs(principle);
		}

		const portfolio = {
			date: date, 
			cash: principle, 
			startDate: date, 
			startCash: principle
		};

		return axios.post(
			GET_API_PORTFOLIO + "/member", 
			portfolio
		);
	}

	nextDay(portfolio) {
		portfolio.date.setDate(date.getDate() + 1);
		return axios.put(
			GET_API_PORTFOLIO + "/member", 
			portfolio
		);
	}

	buyShares(portfolioId, ticker, quantity) {
		const buyOrder = {
			ticker: ticker, 
			quantity: quantity,
		};
		return axios.post(
			GET_API_PORTFOLIO + "/member/" + portfolioId + "/holdings/member", 
			buyOrder
		);
	}

	sellShares(portfolioId, ticker, quantity) {
		const sellOrder = {
			ticker: ticker,
			quantity: quantity,
		};
		return axios.put(
			GET_API_PORTFOLIO + "/member/" + portfolioId + "/holdings/member", 
			sellOrder
		);
	}

	getTickerPriceForDate(date, ticker) {
		return axios.get(GET_API_CLOSINGS + "/price/" + date + "/" + ticker);
	}

	// TODO: figure out why this was done
	getTickerPforD (date, ticker) {
		return axios.get(GET_API_CLOSINGS + "/price/" + date + "/" + ticker)
			.then(response => {
			  this.response = response.data
			  return this.response
			});
		}
}

export default new APIService();