import React, {Component} from 'react';
import svgContent from './svg-content';

const NAME = "currency-dollar";

const CurrencyDollar = ({ width, height }) => {
	const name = NAME;

	return <SvgIcon 
				name={name} 
				width={width} // 16
				height={height} // 16
			/>
}

export default CurrencyDollar;