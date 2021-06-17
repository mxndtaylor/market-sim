import React, {Component} from 'react';

const PiggyBank = () => {
	return (
		<svg xmlns="http://www.w3.org/2000/svg"
				width="22" height="22" fill="currentColor" 
				class="bi bi-piggy-bank" viewBox="0 0 16 16">
			<path d={PATHS[0]}/>
			<path fill-rule="evenodd" d={PATHS[1]}/>
		</svg>

	);
}

export default PiggyBank;