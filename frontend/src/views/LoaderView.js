import React, {Component} from "react";
import APIService from "../APIService";

const LoadingSVG = () => {
	<svg class="svgLoader" style={{justifyContent:'center', alignItems:'center'}} 
			viewBox="0 0 100 100" width="100%" height="50em">
		<path stroke="none" d="M10 50A40 40 0 0 0 90 50A40 42 0 0 1 10 50" 
				fill="#51CACC" transform="rotate(179.719 50 51)">
			<animateTransform type="rotate" attributeName="transform" calcMode="linear" 
					values="0 50 51;360 50 51" keyTimes="0;1" dur="1s" begin="0s" 
					repeatCount="indefinite">
			</animateTransform>
		</path>
	</svg>
}

class LoaderView extends Component {
	constructor(props) {
		super(props)
	}

	componentDidMount() {
		this.load();
	}

	load() {
		APIService.getStocksByDate(this.props.location.state.portfolio.date)
			.then(response => {
				this.loaded();
			});
	}
	
	loaded() {
		let id = this.props.location.state.portfolio.id;
		this.redirectToGameView(id);
	}

	render() {
		return (
			<div class="divLoader" 
					style={{justifyContent:'center', alignItems:'center'}}>
				<LoadingSVG />
			</div>
		);
	}
};

export default LoaderView;