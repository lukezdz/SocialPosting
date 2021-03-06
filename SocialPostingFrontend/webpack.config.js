const path = require('path');

module.exports = {
	entry: './typescript/socialPostingFrontend.ts',
	module: {
	  rules: [
		{
		  test: /\.tsx?$/,
		  use: 'ts-loader',
		  exclude: /node_modules/,
		},
	  ],
	},
	resolve: {
	  extensions: [ '.tsx', '.ts', '.js' ],
	},
	output: {
	  filename: 'socialPostingFrontendBundle.js',
	  path: path.resolve(__dirname, 'dist'),
	  libraryTarget: 'var',
	  library: 'SocialPostingFrontend',
	},
	mode: 'production'
  };