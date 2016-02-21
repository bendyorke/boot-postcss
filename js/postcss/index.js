var postcss = require('postcss')
var autoprefixer = require('autoprefixer')
var nested = require('postcss-nested')
var values = require('postcss-modules-values')
var scope = require('postcss-modules-scope')
var imports = require('postcss-modules-extract-imports')
var local = require('postcss-modules-local-by-default')

nashorn.process = function(css) {
  return postcss([nested, values, local, imports, scope, autoprefixer])
    .process(css)
    .css
}

nashorn.parse = function(css) {
  var res = postcss.parse(css)
  return JSON.stringify(res)
}

nashorn.stringify = function(json) {
  var css = JSON.parse(json)
  return postcss.root(css).toString()
}
