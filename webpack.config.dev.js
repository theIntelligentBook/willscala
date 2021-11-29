const path = require('path');

module.exports = {
  mode: 'development',
  entry: './target/compiled.js',
  output: {
    filename: 'packed.js',
    path: path.resolve(__dirname, 'packed'),
  },
};