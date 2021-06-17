import React, {Component} from 'react';
import svgContent from './svg-content';

class SvgIcon extends Component {
	constructor(props) {
		super(props);
		const svg = svgContent.find(svg => svg.name === this.props.name);
		const svgData = svg.htmlData;
		this.setState({
			paths: svgData.paths,
			viewBox: svgData.viewBox,
		});
	}

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
				x={this.props.x ?? ""}
				y={this.props.y ?? ""}
				width={this.props.width ?? ""} 
				height={this.props.height ?? ""} 
				fill={this.props.color ?? "currentColor"}
				preserveAspectRatio={this.props.preserveAspectRatio ?? ""}
				viewBox={this.state.viewBox ?? ""}
				className={`bi bi-${this.props.name}`} 
			>
				{this.state.paths?.map(this.pathMapper) ?? ""}
			</svg>
		);
	}
}

export default SvgIcon;