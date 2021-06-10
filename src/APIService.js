import axios from 'axios';

const GET_API_PORTFOLIO = "http://localhost:8080/api/portfolios/";
const GET_API_STOCKS = "http://localhost:8080/api/tickers/"
const GET_API_CLOSINGS = "http://localhost:8080/api/closings/"

class APIService{

    initializeStocks(date) {
        return axios.get(GET_API_STOCKS + 'initDB/' + date)
    }

    getPricesByDate(date) {
        console.log("AXIOS GOT DATE: " + date)
        return axios.get(GET_API_CLOSINGS + date)
    }

}

export default new APIService();