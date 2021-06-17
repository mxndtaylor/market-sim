import React, {Component} from 'react';
import svgContent from './svg-content';

class CurrencyDollar extends Component {
	render() {
		return (
			<svg xmlns="http://www.w3.org/2000/svg" 
					width="16" height="16" fill="currentColor" 
					class="bi bi-currency-dollar" viewBox="0 0 16 16">
				<path d={PATHS[0]}/>
			</svg>
		)
	}
}

export default CurrencyDollar;