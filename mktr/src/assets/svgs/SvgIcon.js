import React, {Component} from 'react';
import svgContent from './svg-content';

class SvgIcon extends Component {

	pathMapper(path, key) {
		return <path 
					key={key} 
					fill-rule={path.fillRule ?? ""} 
					transform={this.props.transform ?? ""}
					transform-origin={this.props.transformOrigin ?? ""}
					d={path.d.join()}
				/>
	}

	render() {
		return (
			<svg 
				xmlns="http://www.w3.org/2000/svg" 
				width={this.props.width ?? ""} 
				height={this.props.height ?? ""} 
				fill={this.props.fill ?? "currentColor"}
				class={`bi bi-${this.props.name}`} 
				viewBox={this.props.viewBox ?? ""}
			>
				{this.props.paths?.map(this.pathMapper) ?? ""}
			</svg>
		);
	}
}

export default SvgIcon;