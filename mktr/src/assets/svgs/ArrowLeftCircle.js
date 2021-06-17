import React, {Component} from 'react';
import SvgIcon from './SvgIcon';

const NAME = "arrow-left-circle"

class ArrowLeftCircle extends Component {
	constructor(props) {
		props.svg = svgContent.svgs.find(svg => svg.name === "arrow-circle");

		super(props);
	}

	render() {
		return <SvgIcon name={NAME} width={this.props.width} height={this.props.height} 
					paths={this.props.svg.paths} transform={} />
		(
			<svg xmlns="http://www.w3.org/2000/svg" 
				width="20" height="20" fill="currentColor" 
				class="bi bi-arrow-left-circle" viewBox="0 0 16 16">
				<path fill-rule="evenodd" d={PATHS[0]} transform="scale(-1,1)" 
					transform-origin="center" />
			</svg>
		);
	}
}

export default ArrowLefttCircle;