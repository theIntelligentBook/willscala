const path = require('path');

module.exports = {
  entry: './target/compiled.js',
  output: {
    filename: 'packed.js',
    path: path.resolve(__dirname, 'packed'),
  },
};