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

    createPortfolio(date){
        let portfolios = {date: date, cash: 10000, startDate: date, startCash: 10000}
        return axios.post(GET_API_PORTFOLIO+"/member", portfolios)
    }

    buyShares(memberId, ticker, purchaseDate, amount){
        let holdings = {portfolioId:memberId, ticker: ticker, purchaseDate:purchaseDate, shareQuantity:amount}
        return axios.post(GET_API_PORTFOLIO+"/member/"+memberId+"/holdings", holdings)
    }

    getTickerPriceForDate(date, ticker){
        return axios.get(GET_API_CLOSINGS+ "/price/"+date+"/"+ticker)
    }

    getTickerPforD (date, ticker) {
        return axios.get(GET_API_CLOSINGS+ "/price/"+date+"/"+ticker)
            .then(response => {
              this.response = response.data
              return this.response
            })
        }



}

export default new APIService();
