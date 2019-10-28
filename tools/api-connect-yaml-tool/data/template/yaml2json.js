let yaml = require('js-yaml'),
  fs = require('fs');

var doc = yaml.safeLoad(fs.readFileSync('./api-connect.yaml', 'utf8'));
fs.writeFileSync('api-connect.json', JSON.stringify(doc, null, 2), {encoding: 'utf8'});
