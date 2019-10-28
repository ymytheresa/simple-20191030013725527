let jsyaml = require('js-yaml'),
  fs = require('fs'),
  json = require('../input/credit-card.json');

let result = jsyaml.dump(json);

fs.writeFileSync("credit-card.yaml", result, "utf8");
