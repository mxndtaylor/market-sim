import React, {Component} from 'react';

const NAME = "piggy-bank";

const PiggyBank = ({ width, height }) => {
	const name = NAME;

	return <SvgIcon 
				name={name} 
				width={width} // 22
				height={height} // 22
			/>
}

export default PiggyBank;