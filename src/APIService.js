import axios from 'axios';

const GET_API_PORTFOLIO = "http://localhost:8080/portfolios/";

class APIService {

    // gets all stocks in portfolio
    getPortfolio() {
        return axios.get(GET_API_PORTFOLIO);
    }

    // creates portfolio
    createPortfolio() {
        return axios.post(GET_API_PORTFOLIO + 'member')
    }

    // update portfolio
    updatePortfolio() {
        return axios.put(GET_API_PORTFOLIO + 'member')
    }

}

export default new APIService();