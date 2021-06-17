import React from 'react';
import SvgIcon from './SvgIcon';

const NAME = "arrow-left-circle";

const ArrowLeftCircle = ({ width, height }) => {
	const name = NAME;

	return <SvgIcon 
				name={name} 
				width={width} // 20
				height={height} // 20
			/>
}

export default ArrowLeftCircle;