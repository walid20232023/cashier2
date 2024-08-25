/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
// generated on 2018-03-13 using generator-openmrs-owa 0.6.0
'use strict';
const webpack = require('webpack');
const path = require('path');
const fs = require('fs');
const chalk = require('chalk');
const target = require('yargs').argv.target;
const targetPort = require('yargs').argv.targetPort;

const UglifyPlugin = require('uglifyjs-webpack-plugin');
const CommonsChunkPlugin = webpack.optimize.CommonsChunkPlugin;
const HtmlWebpackPlugin = require('html-webpack-plugin');
const BrowserSyncPlugin = require('browser-sync-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const WebpackOnBuildPlugin = require('on-build-webpack');
const { UnusedFilesWebpackPlugin } = require("unused-files-webpack-plugin");

const autoprefixer = require('autoprefixer');

const env = process.env.NODE_ENV;

const packageJson = require('./package.json');

const THIS_APP_ID = 'orderentry';

var plugins = [];
const nodeModules = {};

let outputFile = `.bundle`;
let vendorOutputFile;
let outputPath;
let devtool;

const rules = [
	{
		test: /\.jsx?$/,
		loader: 'babel-loader',
		exclude: /node_modules/,
		query: {
			presets: ['env', 'react'],
			cacheDirectory: true,
			plugins: ['transform-class-properties', 'transform-object-rest-spread']
	}
}, {
		test: /\.(png|jpg|jpeg|gif|svg|eot|ttf|woff|woff2)$/,
		loader: 'url-loader'
}, {
		test: /\.s?css$/,
		include: [/node_modules/],
		use: [
		'style-loader',
		'css-loader'
		]
}, {
		test: /\.html$/,
		loader: 'html-loader'
}, {
		test: /\.s?css$/,
		exclude: [/node_modules/],
		use: [
			'style-loader?sourceMap',
			{
				loader: 'css-loader',
			},
			'postcss-loader',
			'sass-loader?sourcemap&sourceMapContents&outputStyle=expanded'
		]
}];

/** Minify for production */
if (env === 'production') {

	plugins.push(new webpack.DefinePlugin({
		'process.env.NODE_ENV': JSON.stringify('production')
	}));
	plugins.push(new UglifyPlugin());
	outputFile = `${outputFile}.min.[chunkhash].js`;
	vendorOutputFile = "vendor.bundle.[chunkhash].js";
	outputPath = `${__dirname}/dist/`;
	plugins.push(new WebpackOnBuildPlugin(function (stats) {
		//create zip file
		var archiver = require('archiver');
		var output = fs.createWriteStream(THIS_APP_ID + "-" + packageJson.version + '.zip');
		var archive = archiver('zip');

		output.on('close', function () {
			console.log('distributable has been zipped! size: ' + archive.pointer());
		});

		archive.on('error', function (err) {
			throw err;
		});

		archive.pipe(output);

		archive.directory(`${outputPath}`, '');

		archive.finalize();
	}));
}

if (env === 'deploy' || env === 'development') {
	var getConfig = function () {
		var config;
	
		try {
			// look for config file
			return require('./config.json');
		} catch (err) {
			// create file with defaults if not found
			config = {
				'LOCAL_OWA_FOLDER': '/path/to/your/server/owa/',
				'APP_ENTRY_POINT': 'http://localhost:8080/openmrs/owa/orderentry/index.html'
			};
			fs.writeFileSync('config.json', JSON.stringify(config, null, 2));
			console.log(chalk.yellow("No file 'config.json' found. Creating a default. Please fix the values and re-run."));
			process.exit(1);
		};
	}
	var config = getConfig();
	
	var resolveBrowserSyncTarget = function () {
		if (targetPort != null && targetPort != 'null') {
			return config.APP_ENTRY_POINT.substr(0, 'http://localhost:'.length)
				+ targetPort
				+ config.APP_ENTRY_POINT.substr('http://localhost:'.length + targetPort.toString().length, config.APP_ENTRY_POINT.length);
		}
		else {
			return config.APP_ENTRY_POINT
		}
	};
	var browserSyncTarget = resolveBrowserSyncTarget();

	if (env === 'deploy') {
		outputFile = `${outputFile}.js`;
		vendorOutputFile = "vendor.bundle.js";
		outputPath = `${config.LOCAL_OWA_FOLDER}${config.LOCAL_OWA_FOLDER.slice(-1) != '/' ? '/' : ''}${THIS_APP_ID}`;
		devtool = 'source-map';
	}

	plugins.push(new BrowserSyncPlugin({
		proxy: {
			target: browserSyncTarget
		}
	}));
}

if (env === 'development') {
	outputFile = `${outputFile}.js`;
	vendorOutputFile = "vendor.bundle.js";
	outputPath = `${__dirname}/dist/`;
	devtool = 'eval-source-map';
}

plugins.push(new CommonsChunkPlugin({
	name: 'vendor',
	filename: vendorOutputFile
}));

plugins.push(new HtmlWebpackPlugin({
	template: './app/index.html',
	inject: 'body'
}));

plugins.push(new CopyWebpackPlugin([{
	from: './app/manifest.webapp'
}]));

plugins.push(new CopyWebpackPlugin([{
	from: './app/img/omrs-button.png',
	to: 'img/omrs-button.png'
}]));

plugins.push(new webpack.ProvidePlugin({
    React: 'react',
}));

plugins.push(new webpack.LoaderOptionsPlugin({
    options: {
      postcss: [
        autoprefixer({
          browsers: ['last 3 version', 'ie >= 11']
        })
      ]
    }
}));

plugins.push(
    new UnusedFilesWebpackPlugin({
		patterns: ["app/**/*.*"]
	})
);

console.log('outputPath', outputPath);

var webpackConfig = {
	entry: {
		app: `${__dirname}/app/js/openmrs-owa-orderentry`,
		css: `${__dirname}/app/css/openmrs-owa-orderentry.scss`,
		vendor: [
			'react',
			'redux',
			'redux-promise-middleware',
			'redux-thunk',
			'react-redux',
			'redux-saga'
		]
	},
	devtool: devtool,
	target,
	output: {
		path: outputPath,
		filename: '[name]' + outputFile,
	},
	target: 'web',
	module: {
		rules
	},
	resolve: {
		modules: [path.resolve(__dirname), 'node_modules'],
		extensions: ['.js', '.jsx', '.css', '.scss'],
	},
	plugins,
	externals: nodeModules,
};

module.exports = webpackConfig;
