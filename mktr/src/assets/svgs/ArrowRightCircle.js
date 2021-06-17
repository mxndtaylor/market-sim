import React, {Component} from 'react';

const NAME = "arrow-right-circle";

const ArrowRightCircle = ({ width, height }) => {
	const name = NAME;

	return <SvgIcon 
				name={name} 
				width={width} // 20
				height={height} // 20
			/>
}

export default ArrowRightCircle;